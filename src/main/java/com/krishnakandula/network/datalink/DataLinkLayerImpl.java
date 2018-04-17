package com.krishnakandula.network.datalink;

import com.krishnakandula.network.Node;
import com.krishnakandula.network.Reader;
import com.krishnakandula.network.Writer;
import com.krishnakandula.network.network.NetworkLayer;

import java.util.*;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public class DataLinkLayerImpl implements DataLinkLayer {

    private Node node;
    private NetworkLayer networkLayer;
    private Map<Integer, Reader> readers;
    private Map<Integer, String> msgs;                  // NeighborId -> Msg
    private Map<Integer, Channel> channels;             // NeighborId -> Channel
    private int seqNo = 0;
    private int time;
    private int timeout;

    public DataLinkLayerImpl(Node node, int numChannels, int timeout) {
        this.node = node;
        this.timeout = timeout;

        msgs = new HashMap<>();

        // Init readers
        this.readers = new HashMap<>();
        node.neighbors.forEach(neighbor -> readers.put(neighbor, new Reader(getFilePath(neighbor))));

        // Init channels
        this.channels = new HashMap<>();
        node.neighbors.forEach(neighbor -> channels.put(neighbor, new Channel(numChannels)));
    }

    @Override
    public void receiveFromChannel() {
        for (Integer neighbor : node.neighbors) {
            String msg = msgs.get(neighbor).concat(readers.get(neighbor).readFile());
            msgs.put(neighbor, msg);
        }

        //Check all msgs for completed frames
        node.neighbors.forEach(neighbor -> {
            String msg = msgs.get(neighbor);
            List<String> frames = getFrames(msg);
            msgs.put(neighbor, frames.get(frames.size() - 1));
            frames.remove(frames.size() - 1);
            frames.stream()
                    .forEach(frame -> {
                        if (isAcknowledgment(frame)) {
                            DataAck ack = DataAck.from(frame);
                            Channel channel = channels.get(neighbor);
                            channel.clearLogicalChannel(ack.seqNo);
                        } else {
                            DataFrame dataFrame = DataFrame.from(frame);
                            sendAck(dataFrame, neighbor);
                            networkLayer.receiveFromDataLinkLayer(dataFrame);
                        }
                    });
        });

        checkTimeouts();
    }

    @Override
    public void receiveFromNetwork(String msg, int nextHop) {

    }

    @Override
    public void setNetworkLayer(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
    }

    private void checkTimeouts() {
        channels.forEach((neighborId, channel) -> {
           List<DataFrame> timedOut = channel.getTimedOutFrames(time - timeout);
           timedOut.forEach(dataFrame -> channel.clearLogicalChannel(dataFrame.seqNo));
           timedOut.forEach(dataFrame -> sendData(dataFrame, neighborId));
        });
    }

    private void sendData(DataFrame dataFrame, int neighborId) {
        Integer clearChannel = channels.get(neighborId).getClearLogicalChannel();
        if (clearChannel != null) {
            dataFrame.channelNumber = clearChannel;
            channels.get(neighborId).addToLogicalChannel(dataFrame);
            Writer.writeFile(getFilePath(neighborId), dataFrame.toString());
        } else {
            // Drop the frame, transport layer will resend
        }
    }

    private void sendAck(DataFrame dataFrame, int neighborId) {
        DataAck ack = new DataAck(dataFrame.channelNumber, dataFrame.seqNo);
        Writer.writeFile(getFilePath(neighborId), ack.toString());
    }

    private boolean isAcknowledgment(String frame) {
        String[] splitFrame = frame.split(" ");
        return splitFrame.length > 1 && splitFrame[0].equals("ack");
    }

    /**
     * Retrieves all the frames, if there are any, from a message
     *
     * @param msg The String containing the message read from the channel
     * @return A list containing all the frames. The last element of the list will
     * contain the remainder of the message
     */
    protected List<String> getFrames(String msg) {
        List<String> frames = new ArrayList<>();
        Stack<Integer> s = new Stack<>();
        int index = 0;
        while (index < msg.length()) {
            if (msg.charAt(index) == 'F' && (index == 0 || msg.charAt(index - 1) != 'X')) {
                s.push(index);
            }
            if (msg.charAt(index) == 'E' && (index > 0 && msg.charAt(index - 1) != 'X') && !s.isEmpty()) {
                frames.add(msg.substring(s.pop(), index + 1));
            }
            index++;
        }
        //Add remainder
        if (!s.isEmpty()) frames.add(msg.substring(s.pop()));

        return frames;
    }

    private String getFilePath(int neighborId) {
        return "from" + node.id + "to" + neighborId;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }
}

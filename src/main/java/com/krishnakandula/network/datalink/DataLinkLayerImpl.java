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

        // Init msgs with empty strings
        msgs = new HashMap<>();
        node.neighbors.forEach(neighbor -> msgs.put(neighbor, ""));

        // Init readers
        this.readers = new HashMap<>();
        node.neighbors.forEach(neighbor -> readers.put(neighbor, new Reader(getReadFilePath(neighbor))));

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
            if (!frames.isEmpty()) {
                msgs.put(neighbor, frames.get(frames.size() - 1));
                frames.remove(frames.size() - 1);
                frames.forEach(frame -> {
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
            }
        });

        checkTimeouts();
    }

    @Override
    public void receiveFromNetwork(String msg, int nextHop) {
        DataFrame dataFrame = new DataFrame(++seqNo, msg);
        sendData(dataFrame, nextHop);
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

    void sendData(DataFrame dataFrame, int neighborId) {
        Integer clearChannel = channels.get(neighborId).getClearLogicalChannel();
        if (clearChannel != null) {
            dataFrame.channelNumber = clearChannel;
            channels.get(neighborId).addToLogicalChannel(dataFrame);
            System.out.println("Sending data: " + dataFrame);
            Writer.writeFile(getWriteFilePath(neighborId), dataFrame.toString());
        } else {
            // Drop the frame, transport layer will resend
        }
    }

    private void sendAck(DataFrame dataFrame, int neighborId) {
        DataAck ack = new DataAck(dataFrame.seqNo);
        ack.channelNumber = dataFrame.channelNumber;
        System.out.println("Sending ack: " + ack);
        Writer.writeFile(getWriteFilePath(neighborId), ack.toString());
    }

    private boolean isAcknowledgment(String frame) {
        String[] splitFrame = frame.split(" ");
        return splitFrame.length > 1 && splitFrame[0].equals("Fack");
    }

    /**
     * Retrieves all the frames, if there are any, from a message
     *
     * @param msg The String containing the message read from the channel
     * @return A list containing all the frames. The last element of the list will
     * contain the remainder of the message
     */
    private List<String> getFrames(String msg) {
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
        else frames.add("");
        return frames;
    }

    private String getWriteFilePath(int neighborId) {
        return "from" + node.id + "to" + neighborId;
    }

    private String getReadFilePath(int neighborId) {
        return "from" + neighborId + "to" + node.id;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }
}

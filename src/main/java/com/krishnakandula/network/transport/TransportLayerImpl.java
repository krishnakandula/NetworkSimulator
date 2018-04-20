package com.krishnakandula.network.transport;

import com.krishnakandula.network.Node;
import com.krishnakandula.network.network.NetworkLayer;
import com.krishnakandula.network.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class TransportLayerImpl implements TransportLayer {

    private Node node;
    private NetworkLayer networkLayer;
    private int time;
    private int timeOut;
    private int windowSize;
    private short maxMsgSize;
    private short sequenceNum = 0;
    private Map<Byte, Queue<TransportDataMsg>> msgQueues;                // NeighborId -> Queue of transport data msgs
    private Map<Byte, List<Pair<Integer, TransportDataMsg>>> sentMsgs;  // NeighborId -> List of sent data msgs

    public TransportLayerImpl(Node node, int timeOut, int windowSize, short maxMsgSize) {
        this.node = node;
        this.timeOut = timeOut;
        this.windowSize = windowSize;
        this.maxMsgSize = maxMsgSize;

        msgQueues = new HashMap<>();
        sentMsgs = new HashMap<>();

        node.neighbors.forEach(neighborId -> {
            msgQueues.put(neighborId, new LinkedList<>());
            sentMsgs.put(neighborId, new ArrayList<>());
        });
    }

    @Override
    public void sendMsg(String msg, byte destination) {
        if (msg != null) {
            splitMessage(msg, maxMsgSize).forEach(splitMsg -> {
                TransportDataMsg dataMsg = new TransportDataMsg(
                        node.id,
                        destination,
                        sequenceNum++,
                        splitMsg);
                msgQueues.get(destination).offer(dataMsg);
            });
        }

        node.neighbors.forEach(neighborId -> {
            if (sentMsgs.get(neighborId).size() <= windowSize && !msgQueues.get(neighborId).isEmpty()) {
                TransportDataMsg dataMsg = msgQueues.get(neighborId).poll();
                sendMsg(dataMsg, sentMsgs, time, neighborId);
            }
        });

        // Check timed out msgs and resend
        List<TransportDataMsg> timedOutMsgs = getTimedOutMessages(sentMsgs, time, timeOut);
        timedOutMsgs.forEach(timedOutMsg -> sendMsg(timedOutMsg, sentMsgs, time, timedOutMsg.destinationId));
    }

    @Override
    public void receiveFromNetwork(String msg) {

    }

    @Override
    public void outputAllReceived() {

    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void provideNetworkLayer(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
    }

    private List<String> splitMessage(String msg, int maxMsgSize) {
        List<String> splitMsgs = new ArrayList<>();
        for (int i = 0; i < msg.length(); i++) {
            StringBuilder splitMsg = new StringBuilder();
            for (int x = i; x < Math.min(i + maxMsgSize, msg.length()); x++) {
                splitMsg.append(msg.charAt(i));
            }
            splitMsgs.add(splitMsg.toString());
        }

        return splitMsgs;
    }

    private List<TransportDataMsg> getTimedOutMessages(Map<Byte, List<Pair<Integer, TransportDataMsg>>> sentMsgs,
                                                       int currentTime,
                                                       int timeOut) {
        int latestMsg = currentTime - timeOut;
        List<TransportDataMsg> timedOutMsgs = new ArrayList<>();
        sentMsgs.forEach((neighbor, msgs) -> {
            msgs.forEach(timeMsgPair -> {
                if (latestMsg > timeMsgPair.getKey()) {
                    timedOutMsgs.add(timeMsgPair.getValue());
                }
            });
        });

        return timedOutMsgs;
    }

    private void sendMsg(TransportDataMsg dataMsg,
                         Map<Byte, List<Pair<Integer, TransportDataMsg>>> sentMsgs,
                         int currentTime,
                         byte destination) {

        // Remove previously sent messages with same sequence id in sent msgs
        removeMsgFromSentMsgs(destination, dataMsg.sequenceNum, sentMsgs);
        sentMsgs.get(destination).add(new Pair<>(currentTime, dataMsg));
        networkLayer.receiveFromTransportLayer(dataMsg.toString(), dataMsg.getLength(), dataMsg.destinationId);
    }

    private void removeMsgFromSentMsgs(byte destination,
                                       short sequenceNum,
                                       Map<Byte, List<Pair<Integer, TransportDataMsg>>> sentMsgs) {
        sentMsgs.put(destination, sentMsgs.get(destination).stream()
                .filter(timeDataPair -> timeDataPair.getValue().sequenceNum != sequenceNum)
                .collect(Collectors.toList()));
    }
}

package com.krishnakandula.network.transport;

import com.krishnakandula.network.Node;
import com.krishnakandula.network.network.NetworkLayer;
import com.krishnakandula.network.util.Pair;
import com.krishnakandula.network.util.Writer;

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
    private Set<Byte> networkNodes;
    private Map<Byte, Queue<TransportDataMsg>> msgQueues;               // NodeId -> Queue of transport data msgs
    private Map<Byte, List<Pair<Integer, TransportDataMsg>>> sentMsgs;  // NodeId -> List of sent data msgs
    private Map<Byte, List<TransportDataMsg>> receivedMsgs;                       // NodeId -> List of received msgs

    public TransportLayerImpl(Node node, int timeOut, int windowSize, short maxMsgSize) {
        this.node = node;
        this.timeOut = timeOut;
        this.windowSize = windowSize;
        this.maxMsgSize = maxMsgSize;

        networkNodes = new HashSet<>();
        msgQueues = new HashMap<>();
        sentMsgs = new HashMap<>();

        node.neighbors.stream()
                .filter(neighborId -> neighborId != node.id)
                .forEach(neighborId -> networkNodes.add(neighborId));

        networkNodes.forEach(nodeId -> {
            msgQueues.put(nodeId, new LinkedList<>());
            sentMsgs.put(nodeId, new ArrayList<>());
        });
    }

    @Override
    public void sendMsg(String msg, byte destination) {
        if (!networkNodes.contains(destination)) {
            networkNodes.add(destination);
            msgQueues.put(destination, new LinkedList<>());
            sentMsgs.put(destination, new ArrayList<>());
        }

        if (msg != null) {
            splitMessage(msg, maxMsgSize).forEach(splitMsg -> {
                TransportDataMsg dataMsg = new TransportDataMsg(
                        node.id,
                        destination,
                        sequenceNum++,
                        splitMsg,
                        maxMsgSize);
                msgQueues.get(destination).offer(dataMsg);
            });
        }

        // Check if window is open for each network node and send
        networkNodes.forEach(nodeId -> {
            if (sentMsgs.get(nodeId).size() <= windowSize && !msgQueues.get(nodeId).isEmpty()) {
                TransportDataMsg dataMsg = msgQueues.get(nodeId).poll();
                sendDataToNetworkLayer(dataMsg, sentMsgs, time, nodeId);
            }
        });

        // Check timed out msgs and resend
        List<TransportDataMsg> timedOutMsgs = getTimedOutMessages(sentMsgs, time, timeOut);
        timedOutMsgs.forEach(timedOutMsg -> sendDataToNetworkLayer(timedOutMsg, sentMsgs, time, timedOutMsg.destinationId));
    }

    @Override
    public void receiveFromNetwork(String msg) {
        if (msg.charAt(0) == 'a') {
            // Acknowledgement
            TransportAckMsg ack = TransportAckMsg.from(msg);

            // Remove datamsg from sentMsgs
            if (sentMsgs.containsKey(ack.sourceId)) {
                sentMsgs.put(ack.sourceId, sentMsgs.get(ack.sourceId).stream()
                        .filter(nodeDataPair -> nodeDataPair.getValue().sequenceNum != ack.sequenceNum)
                        .collect(Collectors.toList()));
            }
        } else {
            // Data msg
            TransportDataMsg data = TransportDataMsg.from(msg, maxMsgSize);

            if (!networkNodes.contains(data.sourceId)) {
                networkNodes.add(data.sourceId);
                receivedMsgs.put(data.sourceId, new ArrayList<>());
                msgQueues.put(data.sourceId, new LinkedList<>());
            }

            receivedMsgs.get(data.sourceId).add(data);

            //Send ack
            TransportAckMsg ack = new TransportAckMsg(node.id, data.sourceId, sequenceNum++);
            networkLayer.receiveFromTransportLayer(ack.toString(), ack.getLength(), ack.destinationId);
        }
    }

    @Override
    public void outputAllReceived() {
        receivedMsgs.entrySet().stream()
                .filter(nodeMsgsPair -> !nodeMsgsPair.getValue().isEmpty())
                .forEach(nodeMsgsPair -> {
                    nodeMsgsPair.getValue()
                            .forEach(msg -> Writer.writeFile(getOutputFilePath(nodeMsgsPair.getKey()), msg.toString()));
                });
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
        sentMsgs.forEach((neighbor, msgs) -> msgs.forEach(timeMsgPair -> {
            if (latestMsg > timeMsgPair.getKey()) {
                timedOutMsgs.add(timeMsgPair.getValue());
            }
        }));

        return timedOutMsgs;
    }

    private void sendDataToNetworkLayer(TransportDataMsg dataMsg,
                                        Map<Byte, List<Pair<Integer, TransportDataMsg>>> sentMsgs,
                                        int currentTime,
                                        byte destination) {

        if (!sentMsgs.containsKey(destination)) {
            sentMsgs.put(destination, new ArrayList<>());
        }

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

    private static String getOutputFilePath(byte nodeId) {
        return String.format("node%dreceived", nodeId);
    }

}

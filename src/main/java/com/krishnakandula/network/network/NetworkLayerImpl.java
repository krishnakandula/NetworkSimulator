package com.krishnakandula.network.network;

import com.krishnakandula.network.Node;
import com.krishnakandula.network.datalink.DataLinkLayer;
import com.krishnakandula.network.transport.TransportLayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Krishna Chaitanya Kandula on 4/16/2018.
 */
public class NetworkLayerImpl implements NetworkLayer {

    private Node node;
    private DataLinkLayer dataLinkLayer;
    private TransportLayer transportLayer;
    private Map<Byte, Short> greatestReceivedMsgs;     //NeighborId -> largestReceivedMessage
    private short msgId = 0;

    public NetworkLayerImpl(Node n) {
        this.node = n;

        greatestReceivedMsgs = new HashMap<>();
        node.neighbors.forEach(neighborId -> greatestReceivedMsgs.put(neighborId.byteValue(), Short.MIN_VALUE));
    }

    @Override
    public void receiveFromDataLinkLayer(String msg) {
        System.out.println(String.format("NetworkLayer: Received msg %s from data link layer", msg));
        NetworkMsg networkMsg = NetworkMsg.fromNetworkMsgString(msg);
        if (!greatestReceivedMsgs.containsKey(networkMsg.sourceId)) {
            greatestReceivedMsgs.put(networkMsg.sourceId, Short.MIN_VALUE);
        }
        if (networkMsg.messageId > greatestReceivedMsgs.get(networkMsg.sourceId)) {
            greatestReceivedMsgs.put(networkMsg.sourceId, networkMsg.messageId);

            if (networkMsg.destinationId == node.id) {
                transportLayer.receiveFromNetwork(networkMsg.transportMsg);
            } else {
                //Flood network
                node.neighbors.forEach(neighborId -> dataLinkLayer.receiveFromNetwork(msg, neighborId));
            }
        }
    }

    @Override
    public void receiveFromTransportLayer(String msg, byte length, byte destination) {
        NetworkMsg networkMsg = new NetworkMsg(node.id, destination, ++msgId, length, msg);
        // Flood network
        node.neighbors.forEach(neighborId -> dataLinkLayer.receiveFromNetwork(networkMsg.toString(), neighborId));
    }

    @Override
    public void provideDataLinkLayer(DataLinkLayer dataLinkLayer) {
        this.dataLinkLayer = dataLinkLayer;
    }

    @Override
    public void provideTransportLayer(TransportLayer transportLayer) {
        this.transportLayer = transportLayer;
    }
}

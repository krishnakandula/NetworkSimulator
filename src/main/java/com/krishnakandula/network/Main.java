package com.krishnakandula.network;

import com.krishnakandula.network.datalink.DataLinkLayer;
import com.krishnakandula.network.datalink.DataLinkLayerImpl;
import com.krishnakandula.network.network.NetworkLayer;
import com.krishnakandula.network.network.NetworkLayerImpl;
import com.krishnakandula.network.transport.TransportLayer;
import com.krishnakandula.network.transport.TransportLayerImpl;

import java.util.ArrayList;

public class Main {

    public static void main(String... args) throws InterruptedException {
        Node node = parseInput(args);
        DataLinkLayer dataLinkLayer = new DataLinkLayerImpl(node,
                2,
                5);
        NetworkLayer networkLayer = new NetworkLayerImpl(node);
        TransportLayer transportLayer = new TransportLayerImpl(
                node,
                20,
                3,
                (short) 5);

        dataLinkLayer.provideNetworkLayer(networkLayer);
        networkLayer.provideDataLinkLayer(dataLinkLayer);
        networkLayer.provideTransportLayer(transportLayer);
        transportLayer.provideNetworkLayer(networkLayer);

        for (int time = 0; time < node.lifeTime; time++) {
            System.out.println(String.format("Time: %d", time));
            //Update times
            dataLinkLayer.setTime(time);
            transportLayer.setTime(time);

            dataLinkLayer.receiveFromChannel();
            transportLayer.sendMsg();

            Thread.sleep(1000);
        }

        transportLayer.outputAllReceived();
    }

    private static Node parseInput(String... args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Input must contain node id, duration, and destination node");
        }

        Node node = new Node();
        node.id = Byte.parseByte(args[0]);
        node.lifeTime = Integer.parseInt(args[1]);
        node.destination = Byte.parseByte(args[2]);
        node.neighbors = new ArrayList<>();

        if (args.length > 3) {
            int neighbor = 3;
            if (node.id != node.destination) {
                // args[3] is msg, all other fields are neighbors
                node.msg = args[3];
                neighbor++;
            }
            while (neighbor < args.length) {
                node.neighbors.add(Byte.parseByte(args[neighbor++]));
            }
        }

        return node;
    }
}

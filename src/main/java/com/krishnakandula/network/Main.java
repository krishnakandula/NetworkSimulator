package com.krishnakandula.network;

import com.krishnakandula.network.datalink.DataLinkLayer;
import com.krishnakandula.network.datalink.DataLinkLayerImpl;
import com.krishnakandula.network.network.NetworkLayer;
import com.krishnakandula.network.network.NetworkLayerImpl;

import java.util.ArrayList;

public class Main {

    public static void main(String... args) throws InterruptedException {
        Node node = parseInput(args);
        DataLinkLayer dataLinkLayer = new DataLinkLayerImpl(node, 2, 5);
        NetworkLayer networkLayer = new NetworkLayerImpl();

        dataLinkLayer.setNetworkLayer(networkLayer);
        for (int time = 0; time < node.lifeTime; time++) {
            //Update all times
            dataLinkLayer.setTime(time);

            dataLinkLayer.receiveFromChannel();
            dataLinkLayer.receiveFromNetwork(node.msg, node.destination);
            Thread.sleep(1000);
        }
    }

    private static Node parseInput(String... args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Input must contain node id, duration, and destination node");
        }

        Node node = new Node();
        node.id = Integer.parseInt(args[0]);
        node.lifeTime = Integer.parseInt(args[1]);
        node.destination = Integer.parseInt(args[2]);
        node.neighbors = new ArrayList<>();

        if (args.length > 3) {
            int neighbor = 3;
            if (args[3].charAt(0) == '"') {
                node.msg = args[3];
                neighbor++;
            }
            while (neighbor < args.length) {
                node.neighbors.add(Integer.parseInt(args[neighbor++]));
            }
        }

        return node;
    }
}

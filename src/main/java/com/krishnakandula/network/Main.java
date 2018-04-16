package com.krishnakandula.network;

import com.krishnakandula.network.datalink.DataLinkLayer;
import com.krishnakandula.network.datalink.DataLinkLayerImpl;
import java.util.ArrayList;

public class Main {

    public static void main(String... args) throws InterruptedException {
        Node node = parseInput(args);
        System.out.println(node);
        DataLinkLayer dataLinkLayer = new DataLinkLayerImpl();
        for (int time = 0; time < node.lifeTime; time++) {
            dataLinkLayer.receiveFromChannel();
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
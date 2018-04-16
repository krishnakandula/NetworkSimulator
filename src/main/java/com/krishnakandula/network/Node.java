package com.krishnakandula.network;

import java.util.Arrays;
import java.util.List;

public class Node {
    public int id;
    public int lifeTime;
    public int destination;
    public String msg;
    public List<Integer> neighbors;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id = ").append(id).append("\n");
        builder.append("life = ").append(lifeTime).append("\n");
        builder.append("destination = ").append(destination).append("\n");
        if (msg != null) {
            builder.append("msg = ").append(msg).append("\n");
        }
        if (neighbors != null && !neighbors.isEmpty()) {
            builder.append("neighbors = ").append(Arrays.toString(neighbors.toArray()));
        }
        return builder.toString();

    }
}

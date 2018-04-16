package com.krishnakandula.network.datalink;

import com.krishnakandula.network.Node;
import com.krishnakandula.network.Reader;
import com.krishnakandula.network.network.NetworkLayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public class DataLinkLayerImpl implements DataLinkLayer {

    private Node node;
    private NetworkLayer networkLayer;
    private Map<Integer, Reader> readers;
    private Map<Integer, String> msgs;

    public DataLinkLayerImpl(Node node) {
        this.node = node;

        //Init readers
        this.readers = new HashMap<>();
        node.neighbors.forEach(neighbor -> readers.put(neighbor, new Reader(getFilePath(neighbor))));

    }

    @Override
    public void receiveFromChannel() {
        for (Integer neighbor: node.neighbors) {
            String msg = msgs.get(neighbor).concat(readers.get(neighbor).readFile());
            msgs.put(neighbor, msg);
        }

        //Check all msgs for completed frames
        node.neighbors.forEach(neighbor -> {
            String msg = msgs.get(neighbor);

        });
    }

    @Override
    public void receiveFromNetwork(String msg, int nextHop) {

    }

    private String getFilePath(int neighborId) {
        return "from" + node.id + "to" + neighborId;
    }

    @Override
    public void setNetworkLayer(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
    }
}

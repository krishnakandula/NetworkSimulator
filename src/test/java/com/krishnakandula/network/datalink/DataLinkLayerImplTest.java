package com.krishnakandula.network.datalink;

import com.krishnakandula.network.Node;
import com.sun.tools.javac.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataLinkLayerImplTest {

    @Test
    public void sendData() throws Exception {
        Node node = new Node();
        node.neighbors = List.of(1);
        node.id = 2;
        node.msg = "Testing";
        node.destination = 1;
        node.lifeTime = 100;

        DataLinkLayerImpl dataLinkLayer = new DataLinkLayerImpl(node, 2, 5);
        DataFrame frame = new DataFrame(1, node.msg);
        dataLinkLayer.sendData(frame, node.destination);
    }

}
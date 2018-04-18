package com.krishnakandula.network.datalink;

import com.krishnakandula.network.Node;
import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataLinkLayerImplTest {

    @Test
    public void sendData() throws Exception {
        Node node = new Node();
        List<Integer> neighborsList = new ArrayList<>();
        neighborsList.add(1);
        node.neighbors = neighborsList;
        node.id = 2;
        node.msg = "Testing";
        node.destination = 1;
        node.lifeTime = 100;

        DataLinkLayerImpl dataLinkLayer = new DataLinkLayerImpl(node, 2, 5);
        DataFrame frame = new DataFrame(1, node.msg);
        dataLinkLayer.sendData(frame, node.destination);
    }

}
package com.krishnakandula.network.datalink;

import com.krishnakandula.network.network.NetworkLayer;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface DataLinkLayer {

    void receiveFromChannel();

    void receiveFromNetwork(String msg, int nextHop);

    void setNetworkLayer(NetworkLayer networkLayer);

    void setTime(int time);
}

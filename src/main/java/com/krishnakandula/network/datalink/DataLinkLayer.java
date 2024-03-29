package com.krishnakandula.network.datalink;

import com.krishnakandula.network.network.NetworkLayer;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface DataLinkLayer {

    void receiveFromChannel();

    void receiveFromNetwork(String msg, byte nextHop);

    void provideNetworkLayer(NetworkLayer networkLayer);

    void setTime(int time);
}

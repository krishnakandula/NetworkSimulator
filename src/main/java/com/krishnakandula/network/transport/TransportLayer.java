package com.krishnakandula.network.transport;

import com.krishnakandula.network.network.NetworkLayer;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface TransportLayer {

    void sendMsg();

    void receiveFromNetwork(String msg);

    void outputAllReceived();

    void setTime(int time);

    void provideNetworkLayer(NetworkLayer networkLayer);
}

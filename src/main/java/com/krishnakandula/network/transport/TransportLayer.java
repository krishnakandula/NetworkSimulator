package com.krishnakandula.network.transport;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface TransportLayer {

    void receiveFromNetwork(String msg);

    void outputAllReceived();
}

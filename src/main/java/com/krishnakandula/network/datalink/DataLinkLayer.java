package com.krishnakandula.network.datalink;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface DataLinkLayer {

    void receiveFromChannel();

    void receiveFromNetwork(String msg, int nextHop);
}

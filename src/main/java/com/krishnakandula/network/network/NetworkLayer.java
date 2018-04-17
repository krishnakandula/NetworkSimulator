package com.krishnakandula.network.network;

import com.krishnakandula.network.datalink.DataFrame;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface NetworkLayer {

    void receiveFromDataLinkLayer(DataFrame frame);

    void receiveFromTransportLayer(String msg);
}

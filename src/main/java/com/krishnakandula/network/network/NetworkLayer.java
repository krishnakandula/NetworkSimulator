package com.krishnakandula.network.network;

import com.krishnakandula.network.datalink.DataLinkLayer;
import com.krishnakandula.network.transport.TransportLayer;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface NetworkLayer {

    void receiveFromDataLinkLayer(String msg);

    void receiveFromTransportLayer(String msg);

    void provideDataLinkLayer(DataLinkLayer dataLinkLayer);

    void provideTransportLayer(TransportLayer transportLayer);
}

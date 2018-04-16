package network;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public interface NetworkLayer {

    void receiveFromDataLinkLayer(String msg);

    void receiveFromTransportLayer(String msg);
}

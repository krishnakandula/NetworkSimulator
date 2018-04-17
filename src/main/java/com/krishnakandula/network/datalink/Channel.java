package com.krishnakandula.network.datalink;

/**
 * Created by Krishna Chaitanya Kandula on 4/17/2018.
 */
public class Channel {
    private DataFrame[] logicalChannels;

    Channel(int numChannels) {
        logicalChannels = new DataFrame[numChannels];
    }

    public void clearLogicalChannel(int seqNo) {
        for (int i = 0; i < logicalChannels.length; i++) {
             if (seqNo == logicalChannels[i].seqNo) {
                 logicalChannels[i] = null;
             }
        }
    }
}

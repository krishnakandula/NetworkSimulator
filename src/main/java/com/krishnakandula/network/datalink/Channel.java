package com.krishnakandula.network.datalink;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna Chaitanya Kandula on 4/17/2018.
 */
public class Channel {
    private DataFrame[] logicalChannels;

    Channel(int numChannels) {
        logicalChannels = new DataFrame[numChannels];
    }

    void addToLogicalChannel(DataFrame dataFrame) {
        int channelNo = dataFrame.channelNumber;
        if (channelNo >= 0 && channelNo < logicalChannels.length && logicalChannels[channelNo] == null) {
            logicalChannels[channelNo] = dataFrame;
        }
    }

    Integer getClearLogicalChannel() {
        for (int i = 0; i < logicalChannels.length; i++) {
            if (logicalChannels[i] == null) return i;
        }
        return null;
    }

    void clearLogicalChannel(int seqNo) {
        for (int i = 0; i < logicalChannels.length; i++) {
             if (logicalChannels[i] != null && logicalChannels[i].seqNo == seqNo) {
                 logicalChannels[i] = null;
                 break;
             }
        }
    }

    List<DataFrame> getTimedOutFrames(int earliestValidTime) {
        List<DataFrame> timedOutFrames = new ArrayList<>();
        for (int i = 0; i < logicalChannels.length; i++) {
            DataFrame lastSent = logicalChannels[i];
            if (lastSent != null && lastSent.timeSent != null && lastSent.timeSent < earliestValidTime) {
                timedOutFrames.add(lastSent);
            }
        }

        return timedOutFrames;
    }
}

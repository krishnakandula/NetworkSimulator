package com.krishnakandula.network.datalink;

/**
 * Created by Krishna Chaitanya Kandula on 4/17/2018.
 */
public class DataAck {
    int channelNumber = -1;
    int seqNo;

    DataAck(int seqNo) {
        this.seqNo = seqNo;
    }

    public static DataAck from(String frame) {
        frame = frame.substring(1, frame.length() - 1);
        int channelNum = Integer.parseInt(frame.substring(4, 5));
        int seqNo = Integer.parseInt(frame.substring(6, 7));
        return new DataAck(seqNo);
    }

    @Override
    public String toString() {
        return new StringBuilder("F")
                .append("ack ")
                .append(channelNumber)
                .append(" ")
                .append(seqNo)
                .append("E")
                .toString();
    }
}

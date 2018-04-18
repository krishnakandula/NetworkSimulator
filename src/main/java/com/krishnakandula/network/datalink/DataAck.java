package com.krishnakandula.network.datalink;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String regex = "Fack (\\d+) (\\d+)E$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(frame);
        m.find();

        int channelNum = Integer.parseInt(m.group(1));
        int seqNo = Integer.parseInt(m.group(2));
        DataAck ack = new DataAck(seqNo);
        ack.channelNumber = channelNum;

        return ack;
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

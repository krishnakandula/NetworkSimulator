package com.krishnakandula.network.transport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TransportDataMsg extends TransportMsg {
    String data;            // Can only be up to 5 chars long

    TransportDataMsg(byte sourceId, byte destinationId, short sequenceNum, String data) {
        if (data != null && data.length() > 5)
            throw new IllegalArgumentException("Data can only be up to 5 chars long");
        this.msgType = 'd';
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.sequenceNum = sequenceNum;
        this.data = data;
    }

    byte getLength() {
        return (byte) (5 + data.length());
    }

    static TransportDataMsg from(String msg) {
        String regex = "d (\\d) (\\d) (\\d+) (.*)$";
        Pattern msgPattern = Pattern.compile(regex);
        Matcher matcher = msgPattern.matcher(msg);
        matcher.find();

        return new TransportDataMsg(
                Byte.parseByte(matcher.group(1)),   // sourceId
                Byte.parseByte(matcher.group(2)),   // destinationId
                Short.parseShort(matcher.group(3)), // sequenceNum
                matcher.group(4));                  // data
    }
}

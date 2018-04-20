package com.krishnakandula.network.transport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TransportDataMsg extends TransportMsg {
    String data;            // Can only be up to 5 chars long
    private short maxMsgSize;

    TransportDataMsg(byte sourceId,
                     byte destinationId,
                     short sequenceNum,
                     String data,
                     short maxMsgSize) {

        if (data != null && data.length() > maxMsgSize)
            throw new IllegalArgumentException(String.format("Data can only be up to %d chars long", maxMsgSize));

        this.msgType = 'd';
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.sequenceNum = sequenceNum;
        this.data = data;
        this.maxMsgSize = maxMsgSize;
    }

    @Override
    byte getLength() {
        return (byte) (5 + data.length());
    }

    static TransportDataMsg from(String msg, short maxMsgSize) {
        String regex = "d (\\d) (\\d) (\\d+) (.*)$";
        Pattern msgPattern = Pattern.compile(regex);
        Matcher matcher = msgPattern.matcher(msg);
        matcher.find();

        return new TransportDataMsg(
                Byte.parseByte(matcher.group(1)),   // sourceId
                Byte.parseByte(matcher.group(2)),   // destinationId
                Short.parseShort(matcher.group(3)), // sequenceNum
                matcher.group(4),                   // data
                maxMsgSize);
    }
}

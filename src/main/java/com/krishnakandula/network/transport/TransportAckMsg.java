package com.krishnakandula.network.transport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TransportAckMsg extends TransportMsg {

    TransportAckMsg(byte sourceId, byte destinationId, short sequenceNum) {
        this.msgType = 'a';
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.sequenceNum = sequenceNum;
    }

    @Override
    byte getLength() {
        return 5 - 1;
    }       // Subtract 1 because it's in the description of the project

    static TransportAckMsg from(String msg) {
        String regex = "a (\\d) (\\d) (\\d+)$";
        Pattern msgPattern = Pattern.compile(regex);
        Matcher matcher = msgPattern.matcher(msg);
        matcher.find();

        return new TransportAckMsg(
                Byte.parseByte(matcher.group(1)),       // sourceId
                Byte.parseByte(matcher.group(2)),       // destinationId
                Short.parseShort(matcher.group(3)));    // sequenceNum
    }

    @Override
    public String toString() {
        return String.format("%c %d %d %d", msgType, sourceId, destinationId, sequenceNum);
    }
}

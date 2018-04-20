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

    static TransportAckMsg from(String msg) {
        String regex = "a (\\d) (\\d) (\\d+)$";
        Pattern msgPattern = Pattern.compile(regex);
        Matcher matcher = msgPattern.matcher(msg);
        matcher.find();

        return new TransportAckMsg(
                Byte.parseByte(matcher.group(1)),       // sourceId
                Byte.parseByte(matcher.group(2)),       // destinationId
                Short.parseShort(matcher.group(3)));    // sequenceNum                 // data
    }
}

package com.krishnakandula.network.network;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Krishna Chaitanya Kandula on 4/18/2018.
 */
public class NetworkMsg {
    byte sourceId;
    byte destinationId;
    short messageId;
    byte length;
    String transportMsg;

    NetworkMsg(byte sourceId, byte destinationId, short messageId, byte length, String transportMsg) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.messageId = messageId;
        this.length = length;
        this.transportMsg = transportMsg;
    }

    public static NetworkMsg fromNetworkMsgString(String msg) {
        // Format: "sourceId destinationId messageId length transportMsg"
        String regex = "(\\d) (\\d) (\\d+) (\\d) (.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msg);
        matcher.find();

        byte sourceId = Byte.parseByte(matcher.group(1));
        byte destinationId = Byte.parseByte(matcher.group(2));
        short messageId = Short.parseShort(matcher.group(3));
        byte length = Byte.parseByte(matcher.group(4));
        String transportMsg = matcher.group(5);

        return new NetworkMsg(sourceId, destinationId, messageId, length, transportMsg);
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %d %s", sourceId, destinationId, messageId, length, transportMsg);
    }
}

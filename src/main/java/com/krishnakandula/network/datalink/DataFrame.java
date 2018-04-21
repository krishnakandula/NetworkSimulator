package com.krishnakandula.network.datalink;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Krishna Chaitanya Kandula on 4/16/2018.
 */
public class DataFrame {
    int seqNo;
    int channelNumber = -1;
    String networkLayerMessage;
    Integer timeSent = null;

    DataFrame(int seqNo, String networkLayerMessage) {
        this.seqNo = seqNo;
        this.networkLayerMessage = networkLayerMessage;
    }

    public static DataFrame from(String frame) {
        String regex = "Fdata (\\d+) (\\d+) (.*)E$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(frame);

        m.find();
        int channelNum = Integer.parseInt(m.group(1));
        int seqNo = Integer.parseInt(m.group(2));
        String unstuffedMsg = unstuffMessage(m.group(3));

        DataFrame dataFrame = new DataFrame(seqNo, unstuffedMsg);
        dataFrame.channelNumber = channelNum;
        return dataFrame;
    }

    private static String unstuffMessage(String msg) {
        StringBuilder unstuffedMsg = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            char currentChar = msg.charAt(i);
            if (currentChar == 'X' && i < msg.length() - 1) {
                unstuffedMsg.append(msg.charAt(i + 1));
                i++;
            } else {
                unstuffedMsg.append(msg.charAt(i));
            }
        }
        return unstuffedMsg.toString();
    }

    @Override
    public String toString() {
        //Escape all F's, E's, and X's inside networkLayerMessage
        StringBuilder escapedMsg = new StringBuilder();
        for (int i = 0; i < networkLayerMessage.length(); i++) {
            char currentChar = networkLayerMessage.charAt(i);
            if (currentChar == 'F' || currentChar == 'E' || currentChar == 'X') {
                escapedMsg.append('X');
            }
            escapedMsg.append(currentChar);
        }

        String formattedMsg = String.format("Fdata %d %d %sE",
                channelNumber,
                seqNo,
                escapedMsg.toString());
        return formattedMsg;
    }
}

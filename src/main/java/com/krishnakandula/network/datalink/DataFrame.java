package com.krishnakandula.network.datalink;

/**
 * Created by Krishna Chaitanya Kandula on 4/16/2018.
 */
public class DataFrame {
    int seqNo;
    int channelNumber;
    String networkLayerMessage;

    DataFrame(int seqNo, int channelNumber, String networkLayerMessage) {
        this.seqNo = seqNo;
        this.channelNumber = channelNumber;
        this.networkLayerMessage = networkLayerMessage;
    }

    public static DataFrame from(String frame) {
        //Remove starting F and E
        frame = frame.substring(1, frame.length() - 1);
        String unstuffedMsg = unstuffMessage(frame.substring(10));
        int channelNum = Integer.parseInt(frame.substring(5, 6));
        int seqNo = Integer.parseInt(frame.substring(7, 8));

        return new DataFrame(seqNo, channelNum, unstuffedMsg);
    }

    private static String unstuffMessage(String msg) {
        StringBuilder unstuffedMsg = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            char currentChar = msg.charAt(i);
            if (currentChar == 'X' && i < msg.length() - 2) {
                unstuffedMsg.append(msg.charAt(i + 1));
                i++;
            }
        }

        return unstuffedMsg.toString();
    }

    @Override
    public String toString() {
        //Escape all F's, E's, and X's inside networkLayerMessage
        StringBuilder escapedMsg = new StringBuilder("Fdata ");
        escapedMsg.append(channelNumber).append(" ").append(seqNo).append(" ");
        for (int i = 0; i < networkLayerMessage.length(); i++) {
            char currentChar = escapedMsg.charAt(i);
            if (currentChar == 'F' || currentChar == 'E' || currentChar == 'X') {
                escapedMsg.append('X');
            }
            escapedMsg.append(currentChar);
        }
        escapedMsg.append('E');
        return escapedMsg.toString();
    }
}

package com.krishnakandula.network.datalink;

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
        //Remove starting F and E
        frame = frame.substring(1, frame.length() - 1);
        String unstuffedMsg = unstuffMessage(frame.substring(9));
        int channelNum = Integer.parseInt(frame.substring(5, 6));
        int seqNo = Integer.parseInt(frame.substring(7, 8));

        DataFrame dataFrame = new DataFrame(seqNo, unstuffedMsg);
        dataFrame.channelNumber = channelNum;
        return dataFrame;
    }

    private static String unstuffMessage(String msg) {
        StringBuilder unstuffedMsg = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            char currentChar = msg.charAt(i);
            if (currentChar == 'X' && i < msg.length() - 2) {
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
        return String.format("Fdata %d %d %sE",
                channelNumber,
                seqNo,
                escapedMsg.toString());
    }
}

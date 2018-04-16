package com.krishnakandula.network;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public class Writer {

    public static void writeFile(String filePath, String msg) {
        try {
            BufferedWriter writeFile = openFile(filePath);
            if (writeFile != null) {
                writeFile.write(msg);
                writeFile.write("\n");
                writeFile.close();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static BufferedWriter openFile(String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            return bufferedWriter;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }

}

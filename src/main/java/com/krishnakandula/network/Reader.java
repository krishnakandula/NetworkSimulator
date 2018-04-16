package com.krishnakandula.network;

import java.io.*;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public class Reader {

    private String filePath;
    private int count;

    public Reader(String filePath) {
        this.filePath = filePath;
        try {
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public String readFile() {
        try {
            BufferedReader reader = openFile(filePath);
            if (reader != null) {
                StringBuilder input = new StringBuilder();
                String str;
                int temp = 0;
                while ((str = reader.readLine()) != null) {
                    // new msg
                    if (++temp > count) {
                        input.append(str);
                    }
                }
                count = temp;
                return input.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            System.err.println(e);
            return "";
        }
    }

    private static BufferedReader openFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(fileReader);
            return reader;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }

}

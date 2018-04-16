package com.krishnakandula.network;

import java.io.*;

/**
 * Created by Krishna Chaitanya Kandula on 4/15/2018.
 */
public class Reader {

    public String readFile(String filePath) {
        try {
            BufferedReader reader = openFile(filePath);
            if (reader != null) {
                StringBuilder input = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    input.append(str);
                }
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

package com.krishnakandula.network;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Krishna Chaitanya Kandula on 4/16/2018.
 */
public class ReaderTest {

    @Test
    public void testReadFile() {
        Reader reader = new Reader("test");
        System.out.println(reader.readFile());
    }

}
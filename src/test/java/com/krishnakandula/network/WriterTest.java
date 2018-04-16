package com.krishnakandula.network;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Krishna Chaitanya Kandula on 4/16/2018.
 */
public class WriterTest {

    @Test
    public void testWriteFile() {
        Writer.writeFile("test", "Hello there");
        Writer.writeFile("test", "Hello there2");
    }
}
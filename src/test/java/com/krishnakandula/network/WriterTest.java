package com.krishnakandula.network;

import com.krishnakandula.network.util.Writer;
import org.junit.Test;

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
package com.krishnakandula.network;

import com.krishnakandula.network.util.Reader;
import org.junit.Test;

/**
 * Created by Krishna Chaitanya Kandula on 4/16/2018.
 */
public class ReaderTest {

    @Test
    public void testReadFile() {
        Reader reader = new Reader("from2to1");
        String read = reader.readFile();
        System.out.println(read);
    }

}
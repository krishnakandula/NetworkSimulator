package com.krishnakandula.network.datalink;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataFrameTest {

    @Test
    public void testToString() {
        DataFrame frame = DataFrame.from("Fdata 1 12 Testing5E");
        System.out.println(frame);
    }

}
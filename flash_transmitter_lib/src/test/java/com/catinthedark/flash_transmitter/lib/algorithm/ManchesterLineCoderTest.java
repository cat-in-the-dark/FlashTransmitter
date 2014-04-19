package com.catinthedark.flash_transmitter.lib.algorithm;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Ilya on 16.04.2014.
 */
public class ManchesterLineCoderTest {
    private final Byte[] infoData = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1};
    private final Byte[] rawData = {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0};
    private ManchesterLineCoder lineCoder;

    @Before
    public void setup() {
        lineCoder = new ManchesterLineCoder();
    }

    @Test
    public void testPacking() {
        Byte[] raw = lineCoder.pack(infoData);

        Assert.assertEquals(Arrays.asList(rawData), Arrays.asList(raw));
    }

    @Test
    public void testUnpacking() {
        Byte[] info = lineCoder.unpack(rawData);

        Assert.assertEquals(Arrays.asList(infoData), Arrays.asList(info));
    }
}

package com.catinthedark.flash_transmitter.lib.algorithm;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Ilya on 18.04.2014.
 */
public class ConverterTest {
    private Converter converter;
    private final String hello_world = "hello world!?.\n";

    @Test
    public void testTransitionOfASCIISchemeAndManchester() {
        ASCIIScheme scheme = new ASCIIScheme();
        ManchesterLineCoder lineCoder = new ManchesterLineCoder();
        converter = new Converter(scheme, lineCoder);

        Assert.assertEquals(converter.makeString(converter.makeBits(hello_world)), hello_world);
    }

    @Test
    public void testTransitionOfCompressedSchemeAndManchester() {
        CompressedScheme scheme = new CompressedScheme();
        ManchesterLineCoder lineCoder = new ManchesterLineCoder();
        converter = new Converter(scheme, lineCoder);

        Assert.assertEquals(converter.makeString(converter.makeBits(hello_world)), hello_world.toUpperCase());
    }
}

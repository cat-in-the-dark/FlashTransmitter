package com.catinthedark.flash_transmitter.lib.algorithm;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ilya on 16.04.2014.
 */
public class ASCIISchemeTest {
    ASCIIScheme scheme;
    String hello_world = "hello world!?.\n";
    final Byte[] hello_world_codes = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33, 63, 46, 10};
    final Byte[] hello_world_codes_bits = {0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0};

    @Before
    public void setup() {
        scheme = new ASCIIScheme();
    }

    @Test
    public void testGetCodes() {
        Byte[] codes = scheme.getCodes(hello_world);
        ArrayList<Byte> res = new ArrayList<Byte>(Arrays.asList(codes));

        Assert.assertEquals(Arrays.asList(hello_world_codes), res);
    }

    @Test
    public void testBuildString() {
        String str = scheme.getSymbols(hello_world_codes);

        Assert.assertEquals(hello_world, str);
    }

    @Test
    public void testGetBitCodes() {
        Byte[] bits = scheme.getBitCodes(hello_world);

        Assert.assertEquals(Arrays.asList(hello_world_codes_bits), Arrays.asList(bits));
    }

    @Test
    public void testGetSymbolsByBits() {
        String str = scheme.getSymbolsByBits(hello_world_codes_bits);

        Assert.assertEquals(hello_world, str);
    }
}

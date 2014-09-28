package com.catinthedark.flash_transmitter.lib.algorithm;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ilya on 15.04.2014.
 */
public class ASCIIScheme implements EncodingScheme {
    public final int BLOCK_SIZE = 8;

    @Override
    public Byte[] getCodes(String str) {
        byte[] codes = str.getBytes();
        Byte[] wrapped_codes = new Byte[codes.length];
        int i = 0;
        for (byte b: codes) {
            wrapped_codes[i++] = b;
        }

        return wrapped_codes;
    }

    @Override
    public String getSymbols(Byte[] codesObjects) {
        byte[] codes = new byte[codesObjects.length];
        int i = 0;
        for (Byte b: codesObjects) {
            codes[i++] = b;
        }

        return new String(codes);
    }

    @Override
    public Byte[] getBitCodes(String str) {
        Byte[] codes = getCodes(str);
        ArrayList<Byte> bitCodes = new ArrayList<Byte>(codes.length * BLOCK_SIZE);
        for (Byte code: codes) {
            bitCodes.addAll(Arrays.asList(codeToBits(code)));
        }

        return bitCodes.toArray(new Byte[bitCodes.size()]);
    }

    private Byte[] codeToBits(Byte code) {
        Byte[] bits = new Byte[BLOCK_SIZE];

        for (byte i = 0; i < BLOCK_SIZE; i++) {
            bits[i] = (byte)((code >> i) & 1);
        }

        return bits;
    }

    @Override
    public String getSymbolsByBits(Byte[] bitCodes) {
        Byte[] codes = new Byte[bitCodes.length / BLOCK_SIZE]; // but we need bitCodes.length / BLOCK_SIZE memory. LOL

        for (byte i = 0; i < bitCodes.length / BLOCK_SIZE; ++i) {
            codes[i] = 0;
        }

        byte aByte = 0;
        for (byte i = 0; i < bitCodes.length; i++) {
            aByte += (byte)(bitCodes[i] << (i % BLOCK_SIZE));
            if (i % BLOCK_SIZE == BLOCK_SIZE - 1) {
                codes[i / BLOCK_SIZE] = aByte;
                aByte = 0;
            }
        }

        return getSymbols(codes);
    }
}

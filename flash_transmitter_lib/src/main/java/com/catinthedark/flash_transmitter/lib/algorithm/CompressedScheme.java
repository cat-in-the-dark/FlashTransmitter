package com.catinthedark.flash_transmitter.lib.algorithm;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Ilya on 15.04.2014.
 */

// Compressed Scheme based only on latin alphabet. Each symbol has 5-bit notation.
public class CompressedScheme implements EncodingScheme {
    public final int BLOCK_SIZE = 5;

    static final Map<Character, Byte> charToByte = ImmutableMap.<Character, Byte>builder()
            .put('\0', (byte) 0)
            .put('A', (byte) 1)
            .put('B', (byte) 2)
            .put('C', (byte) 3)
            .put('D', (byte) 4)
            .put('E', (byte) 5)
            .put('F', (byte) 6)
            .put('G', (byte) 7)
            .put('H', (byte) 8)
            .put('I', (byte) 9)
            .put('J', (byte) 10)
            .put('K', (byte) 11)
            .put('L', (byte) 12)
            .put('M', (byte) 13)
            .put('N', (byte) 14)
            .put('O', (byte) 15)
            .put('P', (byte) 16)
            .put('Q', (byte) 17)
            .put('R', (byte) 18)
            .put('S', (byte) 19)
            .put('T', (byte) 20)
            .put('U', (byte) 21)
            .put('V', (byte) 22)
            .put('W', (byte) 23)
            .put('X', (byte) 24)
            .put('Y', (byte) 25)
            .put('Z', (byte) 26)
            .put(' ', (byte) 27)
            .put('!', (byte) 28)
            .put('\n', (byte) 29)
            .put('.', (byte) 30)
            .put('?', (byte) 31)
            .build();

    static final Map<Byte, Character> byteToChar = ImmutableMap.<Byte, Character>builder()
            .put((byte)0, '\0')
            .put((byte)1, 'A')
            .put((byte)2, 'B')
            .put((byte)3, 'C')
            .put((byte)4, 'D')
            .put((byte)5, 'E')
            .put((byte)6, 'F')
            .put((byte)7, 'G')
            .put((byte)8, 'H')
            .put((byte)9, 'I')
            .put((byte)10, 'J')
            .put((byte)11, 'K')
            .put((byte)12, 'L')
            .put((byte)13, 'M')
            .put((byte)14, 'N')
            .put((byte)15, 'O')
            .put((byte)16, 'P')
            .put((byte)17, 'Q')
            .put((byte)18, 'R')
            .put((byte)19, 'S')
            .put((byte)20, 'T')
            .put((byte)21, 'U')
            .put((byte)22, 'V')
            .put((byte)23, 'W')
            .put((byte)24, 'X')
            .put((byte)25, 'Y')
            .put((byte)26, 'Z')
            .put((byte)27, ' ')
            .put((byte)28, '!')
            .put((byte)29, '\n')
            .put((byte)30, '.')
            .put((byte)31, '?')
            .build();


    @Override
    public Byte[] getCodes(String str) {
        ArrayList<Byte> codes = new ArrayList<Byte>(str.length());
        for(char ch: str.toUpperCase().toCharArray()) {
            codes.add(charToByte.get(ch));
        }

        return codes.toArray(new Byte[0]);
    }

    @Override
    public String getSymbols(Byte[] codes) {
        StringBuilder builder = new StringBuilder(codes.length);
        for (byte b: codes) {
            builder.append(byteToChar.get(b));
        }
        return builder.toString();
    }

    @Override
    public Byte[] getBitCodes(String str) {
        Byte[] codes = getCodes(str);
        ArrayList<Byte> bitCodes = new ArrayList<Byte>(codes.length * BLOCK_SIZE);
        for (Byte code: codes) {
            bitCodes.addAll(Arrays.asList(codeToBits(code)));
        }

        return bitCodes.toArray(new Byte[0]);
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

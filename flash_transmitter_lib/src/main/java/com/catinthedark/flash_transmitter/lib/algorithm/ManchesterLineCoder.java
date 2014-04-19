package com.catinthedark.flash_transmitter.lib.algorithm;

import java.util.ArrayList;

/**
 * Created by Ilya on 15.04.2014.
 */
public class ManchesterLineCoder implements LineCoder {
    @Override
    public Byte[] pack(final Byte[] bits) {
        ArrayList<Byte> rawData = new ArrayList<Byte>(bits.length * 2);

        for (byte b: bits) {
            if (b == 0) {
                rawData.add((byte) 0);
                rawData.add((byte) 1);
            } else {
                rawData.add((byte) 1);
                rawData.add((byte) 0);
            }
        }

        return rawData.toArray(new Byte[0]);
    }

    @Override
    public Byte[] unpack(final Byte[] bits) {
        ArrayList<Byte> data = new ArrayList<Byte>();

        for (int i = 0; i < bits.length; i += 2) {
            if (bits[i] == (bits[i + 1] ^ 1)) {
                data.add(bits[i]);
            }
        }

        return data.toArray(new Byte[0]);
    }
}
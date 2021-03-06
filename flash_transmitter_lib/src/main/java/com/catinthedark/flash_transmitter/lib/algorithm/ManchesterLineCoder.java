package com.catinthedark.flash_transmitter.lib.algorithm;

import java.util.ArrayList;

/**
 * Created by Ilya on 15.04.2014.
 */
public class ManchesterLineCoder implements LineCoder {
    private final Synchronizer manchesterSynchronizer = new ManchesterSynchronizer();

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

        return manchesterSynchronizer.addSynchroImpuls(rawData.toArray(new Byte[rawData.size()]));
    }

    @Override
    public Byte[] unpack(final Byte[] bits) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        Byte[] synchBits = manchesterSynchronizer.removeSynchroImpuls(bits);

        ArrayList<Byte> manchesterizedData = new ArrayList<Byte>();
        for (int i = 0; i < synchBits.length - 1; i += 2) {
            if (!synchBits[i].equals(synchBits[i + 1])) {
                manchesterizedData.add(synchBits[i]);
                manchesterizedData.add(synchBits[i + 1]);
            } else {
                i -= 1;
            }
        }

        for (int i = 0; i < manchesterizedData.size(); i += 2) {
            if (manchesterizedData.get(i) == (manchesterizedData.get(i + 1) ^ 1)) {
                data.add(manchesterizedData.get(i));
            }
        }

        return data.toArray(new Byte[data.size()]);
    }
}

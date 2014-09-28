package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * Created by Ilya on 28.09.2014.
 */
public class ManchesterSynchronizer implements Synchronizer {
    /**
     * ManchesterSynchronizer is necessary only for manchester code. So, it can be part the manchester.
     * But it make line code not symmetric.
     */
    private int synchroCount = 2;

    public Byte[] addSynchroImpuls(final Byte[] bits) {
        Byte[] res = new Byte[bits.length + synchroCount];
        Byte[] s = {0, 1};
        System.arraycopy(s, 0, res, 0, s.length);
        System.arraycopy(bits, 0, res, s.length, bits.length);

        return res;
    }

    public Byte[] removeSynchroImpuls(final Byte[] bits) {
        Byte[] res = new Byte[bits.length - synchroCount];
        System.arraycopy(bits, synchroCount, res, 0, bits.length - synchroCount);

        return res;
    }
}

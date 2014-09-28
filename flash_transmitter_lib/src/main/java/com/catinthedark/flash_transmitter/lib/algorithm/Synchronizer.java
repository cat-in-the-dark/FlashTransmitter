package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * Depends on line coder algorithms synchronizer can be different.
 * Created by Ilya on 28.09.2014.
 */
public interface Synchronizer {
    /**
     * Add some synchro signals to raw bits.
     * @param bits
     * @return
     */
    public Byte[] addSynchroImpuls(final Byte[] bits);

    /**
     * Remove special synchro bits from raw data
     * @param bits
     * @return
     */
    public Byte[] removeSynchroImpuls(final Byte[] bits);
}

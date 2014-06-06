package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * Created by Ilya on 06.06.2014.
 */
public interface ErrorCorrectionLayer {
    /**
     *
     * @param outputBits
     * @return
     */
    public Byte[] addRedundancy(final Byte[] outputBits);

    /**
     *
     * @param inputBits
     * @return
     */
    public Byte[] recoverData(final Byte[] inputBits);
}

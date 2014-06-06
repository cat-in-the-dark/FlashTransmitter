package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * Created by Ilya on 06.06.2014.
 */
public interface LogicalCodeLayer {
    /**
     * Apply logical code on info bits
     * @param bits
     * @return sequence of processed bits
     */
    public Byte[] pack(final Byte[] bits);

    /**
     * Unpack logical bits into info bits
     * @param bits
     * @return sequence of info bits
     */
    public Byte[] unpack(final Byte[] bits);
}

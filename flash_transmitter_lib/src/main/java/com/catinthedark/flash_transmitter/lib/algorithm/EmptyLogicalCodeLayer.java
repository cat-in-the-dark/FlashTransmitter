package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * Created by Ilya on 06.06.2014.
 */
public class EmptyLogicalCodeLayer implements LogicalCodeLayer {
    @Override
    public Byte[] pack(Byte[] bits) {
        return bits;
    }

    @Override
    public Byte[] unpack(Byte[] bits) {
        return bits;
    }
}

package com.catinthedark.flash_transmitter.lib.algorithm;

import java.util.ArrayList;

/**
 * Created by Ilya on 15.04.2014.
 */
public interface LineCoder {
    /**
     * Convert array of real info-bits into bits that will be transmitted. Depends on type of line code.
     * @param bits Array of info-bits.
     * @return Array of bits that will be sent over channel.
     */
    public Byte[] pack(final Byte[] bits);

    /**
     * Convert received bits into array of info-bits.
     * @param bits Array of received bits.
     * @return Array of info-bits.
     */
    public Byte[] unpack(final Byte[] bits);
}

package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * This class is empty algorithm and do nothing. Create only for compatibility.
 * Created by Ilya on 06.06.2014.
 */
public class EmptyErrorCorrectionLayer implements ErrorCorrectionLayer {
    @Override
    public Byte[] addRedundancy(Byte[] outputBits) {
        return outputBits;
    }

    @Override
    public Byte[] recoverData(Byte[] inputBits) {
        return inputBits;
    }
}

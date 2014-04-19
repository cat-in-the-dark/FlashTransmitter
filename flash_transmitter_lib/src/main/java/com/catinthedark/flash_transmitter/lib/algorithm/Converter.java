package com.catinthedark.flash_transmitter.lib.algorithm;

import java.util.ArrayList;

/**
 * Created by Ilya on 15.04.2014.
 */

/**
 * Convert text into array of transmitting-bits.
 * Convert received-bits into text.
 */
public class Converter {
    private EncodingScheme encodingScheme;
    private LineCoder lineCoder;

    public Converter(EncodingScheme scheme, LineCoder coder) {
        this.encodingScheme = scheme;
        this.lineCoder = coder;
    }

    public Byte[] makeBits(String text){
        return lineCoder.pack(encodingScheme.getBitCodes(text));
    }

    public String makeString(Byte[] bits) {
        return encodingScheme.getSymbolsByBits(lineCoder.unpack(bits));
    }
}

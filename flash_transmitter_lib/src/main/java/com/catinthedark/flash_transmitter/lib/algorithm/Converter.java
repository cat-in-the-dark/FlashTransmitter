package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * Created by Ilya on 15.04.2014.
 */

/**
 * Convert text into array of transmitting-bits.
 * Convert received-bits into text.
 */
public class Converter {
    private EncodingScheme encodingScheme;
    private ErrorCorrectionLayer errorCorrectionLayer;
    private LogicalCodeLayer logicalCodeLayer;
    private LineCoder lineCoder;

    public Converter(EncodingScheme scheme, LineCoder coder, ErrorCorrectionLayer correction, LogicalCodeLayer logical) {
        this.encodingScheme = scheme;
        this.lineCoder = coder;
        this.errorCorrectionLayer = correction;
        this.logicalCodeLayer = logical;
    }

    public Byte[] makeBits(String text){
        return lineCoder.pack(
                logicalCodeLayer.pack(
                        errorCorrectionLayer.addRedundancy(
                                encodingScheme.getBitCodes(text))));
    }

    public String makeString(Byte[] bits) {
        return encodingScheme.getSymbolsByBits(
                errorCorrectionLayer.recoverData(
                        logicalCodeLayer.unpack(
                                lineCoder.unpack(bits))));
    }
}

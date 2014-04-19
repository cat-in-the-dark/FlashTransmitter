package com.catinthedark.flash_transmitter.lib.algorithm;

/**
 * Created by Ilya on 15.04.2014.
 */
public interface EncodingScheme {
    /**
     * Convert each char form input string to byte.
     * @param str Input string.
     * @return Array of 'chars' in byte notation.
     */
    public Byte[] getCodes(String str);

    /**
     * Convert array of 'char' codes into string.
     * @param codes Array of 'char' codes.
     * @return String.
     */
    public String getSymbols(Byte[] codes);

    /**
     * Convert each char in bits.
     * @param str Input string.
     * @return Array of bits.
     */
    public Byte[] getBitCodes(String str);

    /**
     * Convert array of bits in string depends on encoding scheme.
     * @param bitCodes Array of bits
     * @return String
     */
    public String getSymbolsByBits(Byte[] bitCodes);
}

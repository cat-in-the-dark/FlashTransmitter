package com.catinthedark.flash_transmitter.lib.algorithm;

import java.util.ArrayList;

/**
 * Created by Ilya on 15.04.2014.
 */
public class RawDataTranslator {

    /**
     * Translate array of light changes' timestamps into array of bites.
     * @param timestamps Array of light changes'.
     * @return Array of received bits
     */
    public ArrayList<Byte> translate(ArrayList<Long> timestamps) {
        ArrayList<Long> intervals = new ArrayList<Long>();
        long lastTimestamp = timestamps.get(0);
        long maxInterval = Long.MIN_VALUE;
        long minInterval = Long.MAX_VALUE;

        for (int i = 1; i < timestamps.size(); i++) {
            long currentInterval = timestamps.get(i) - lastTimestamp;
            intervals.add(currentInterval);
            if (currentInterval > maxInterval) {
                maxInterval = currentInterval;
            }
            if (currentInterval < minInterval) {
                minInterval = currentInterval;
            }
            lastTimestamp = timestamps.get(i);
        }

        ArrayList<Byte> bits = new ArrayList<Byte>();
        Byte lastCode;
        bits.add((byte) 0);
        bits.add((byte) 1);

        for (int i = 1; i < timestamps.size(); ++i) {//really from 1
            lastCode = bits.get(bits.size() - 1);
            if (Math.abs(intervals.get(i-1) - maxInterval) < Math.abs(intervals.get(i-1)-minInterval)) {
                //current interval is full period
                bits.add(lastCode);//duplicate last code
            }
            bits.add((byte) (lastCode ^ 1));
        }
        if (bits.size() % 2 == 1) {
            bits.add((byte) 0); // alignment
        }

        return bits;
    }
}

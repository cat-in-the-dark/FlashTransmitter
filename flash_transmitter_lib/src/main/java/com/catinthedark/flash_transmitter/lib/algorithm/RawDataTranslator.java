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
    public Byte[] translate(ArrayList<Long> timestamps) {
        ArrayList<Long> intervals = new ArrayList<Long>();
        long lastTimestamp = timestamps.get(0);
        long maxIntervalHigh = Long.MIN_VALUE;
        long minIntervalHigh = Long.MAX_VALUE;

        long maxIntervalLow = Long.MIN_VALUE;
        long minIntervalLow = Long.MAX_VALUE;

        for (int i = 1; i < timestamps.size(); i++) {
            long currentInterval = timestamps.get(i) - lastTimestamp;
            intervals.add(currentInterval);
            if (i % 2 == 1) {
                if (currentInterval > maxIntervalHigh) {
                    maxIntervalHigh = currentInterval;
                }
                if (currentInterval < minIntervalHigh) {
                    minIntervalHigh = currentInterval;
                }
            } else {
                if (currentInterval > maxIntervalLow) {
                    maxIntervalLow = currentInterval;
                }
                if (currentInterval < minIntervalLow) {
                    minIntervalLow = currentInterval;
                }
            }
            lastTimestamp = timestamps.get(i);
        }

        ArrayList<Byte> bits = new ArrayList<Byte>();
        Byte lastCode;
        bits.add((byte) 0);
        bits.add((byte) 1);

        for (int i = 1; i < timestamps.size(); ++i) {//really from 1
            lastCode = bits.get(bits.size() - 1);
            if (i % 2 == 1) {
                if (Math.abs(intervals.get(i - 1) - maxIntervalHigh) < Math.abs(intervals.get(i - 1) - minIntervalHigh)) {
                    //current interval is full period
                    bits.add(lastCode);//duplicate last code
                }
            } else {
                if (Math.abs(intervals.get(i - 1) - maxIntervalLow) < Math.abs(intervals.get(i - 1) - minIntervalLow)) {
                    //current interval is full period
                    bits.add(lastCode);//duplicate last code
                }
            }
            bits.add((byte) (lastCode ^ 1));
        }
        if (bits.size() % 2 == 1) {
            bits.add((byte) 0); // alignment
        }

        return bits.toArray(new Byte[bits.size()]);
    }
}

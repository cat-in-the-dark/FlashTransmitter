package com.catinthedark.flash_transmitter.lib.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sun.reflect.generics.tree.Tree;

/**
 * Created by kirill on 24.09.14.
 */
public class Filter {
    public static TreeMap<Long, Float> filter(TreeMap<Long, Float> graph) {

        int threshold = 5;

        ArrayList<Float> brightnesses = new ArrayList<Float>(graph.values());
        Collections.sort(brightnesses);

        int brightestIndex = -1;

        for (int i = 1; i < brightnesses.size() - 1; i++) {
            if (brightnesses.get(i) / brightnesses.get(i - 1) > threshold) {
                brightestIndex = i;
                break;
            }
        }

        List<Float> noiseBrightnesses = brightnesses.subList(0, brightestIndex);
        List<Float> significantBrightnesses = brightnesses.subList(brightestIndex + 1, brightnesses.size());

        float avgNoiseBrightness = avg(noiseBrightnesses);
        float averageBrightness = avg(significantBrightnesses) - avgNoiseBrightness;

        TreeMap<Long, Float> filteredGraph = new TreeMap<Long, Float>();

        int lastBrightness = 0;
        for (Map.Entry<Long, Float> graphPoint: graph.entrySet()) {
            if (graphPoint.getValue() < averageBrightness) {
                if (lastBrightness == (int) averageBrightness) {
                    filteredGraph.put(graphPoint.getKey(), 0f);
                    lastBrightness = 0;
                }
            } else {
                if (lastBrightness == 0) {
                    filteredGraph.put(graphPoint.getKey(), averageBrightness);
                    lastBrightness = (int)averageBrightness;
                }
            }
        }

        return filteredGraph;
    }

    private static float avg(List<Float> data) {
        float totalData = 0;
        for (float el : data) {
            totalData += el;
        }
        return totalData / data.size();
    }
}

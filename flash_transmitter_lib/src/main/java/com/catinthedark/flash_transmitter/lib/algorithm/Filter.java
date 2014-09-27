package com.catinthedark.flash_transmitter.lib.algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import sun.reflect.generics.tree.Tree;

/**
 * Created by kirill on 24.09.14.
 */
public class Filter {
    public static TreeMap<Long, Float> filter(TreeMap<Long, Float> graph) {

        int threshold = 5;

        final ArrayList<Float> brightnesses = new ArrayList<Float>(graph.values());
        ArrayList<Float> newBrightness = new ArrayList<Float>(brightnesses);
        Collections.sort(newBrightness);

        int brightestIndex = -1;
        float totalBrightness = 0;

        for (int i = 1; i < brightnesses.size() - 1; i++) {
            if (brightestIndex != -1) {
                totalBrightness += brightnesses.get(i);
            } else if (brightnesses.get(i) / brightnesses.get(i - 1) > threshold) {
                brightestIndex = i;
            }
            // other brightnesses are not significant and should be omitted
        }

        float significantBrightnessesCount = brightnesses.size() - brightestIndex;

        float averageBrightness = totalBrightness / significantBrightnessesCount;

        TreeMap<Long, Float> filteredGraph = new TreeMap<Long, Float>();

        int lastBrightness = -1;
        for (Map.Entry<Long, Float> graphPoint: graph.entrySet()) {
            if (graphPoint.getValue() < averageBrightness
                    && (lastBrightness != 0)) {
                filteredGraph.put(graphPoint.getKey(), 0f);
                lastBrightness = 0;
            } else if (lastBrightness != (int)averageBrightness) {
                filteredGraph.put(graphPoint.getKey(), averageBrightness);
                lastBrightness = (int)averageBrightness;
            }
        }

        return filteredGraph;
    }
}

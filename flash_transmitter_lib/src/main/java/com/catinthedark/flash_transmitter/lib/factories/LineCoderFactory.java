package com.catinthedark.flash_transmitter.lib.factories;

import com.catinthedark.flash_transmitter.lib.algorithm.LineCoder;
import com.catinthedark.flash_transmitter.lib.algorithm.ManchesterLineCoder;
import com.google.common.collect.ImmutableMap;
import java.lang.UnsupportedOperationException;

import java.util.Map;
import java.util.Set;

/**
 * Created by ilyab_000 on 20.04.2014.
 */
public class LineCoderFactory {
    public static final String defaultCoder = "Manchester 802.3";
    public static final Map<String, Class> CODERS_LIST = ImmutableMap.<String, Class>builder()
            .put("Manchester 802.3", ManchesterLineCoder.class)
            .build();

    public static LineCoder build(String klass) throws UnsupportedOperationException {
        Class scheme = CODERS_LIST.get(klass);
        if (scheme != null) {
            try {
                return (LineCoder) scheme.newInstance();
            } catch (InstantiationException e) {
                throw new UnsupportedOperationException();
            } catch (IllegalAccessException e) {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
    public static String[] getCodersNames() {
        Set<String> keys = CODERS_LIST.keySet();
        return keys.toArray(new String[keys.size()]);
    }

}

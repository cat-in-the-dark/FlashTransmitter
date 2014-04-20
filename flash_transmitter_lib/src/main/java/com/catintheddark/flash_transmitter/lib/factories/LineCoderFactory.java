package com.catintheddark.flash_transmitter.lib.factories;

import com.catinthedark.flash_transmitter.lib.algorithm.LineCoder;
import com.catinthedark.flash_transmitter.lib.algorithm.ManchesterLineCoder;
import com.google.common.collect.ImmutableMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public static LineCoder build(String klass) throws NotImplementedException {
        Class scheme = CODERS_LIST.get(klass);
        if (scheme != null) {
            try {
                return (LineCoder) scheme.newInstance();
            } catch (InstantiationException e) {
                throw new NotImplementedException();
            } catch (IllegalAccessException e) {
                throw new NotImplementedException();
            }
        } else {
            throw new NotImplementedException();
        }
    }
    public static String[] getCodersNames() {
        Set<String> keys = CODERS_LIST.keySet();
        return keys.toArray(new String[keys.size()]);
    }

}

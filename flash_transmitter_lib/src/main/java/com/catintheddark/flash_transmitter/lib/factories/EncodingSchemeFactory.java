package com.catintheddark.flash_transmitter.lib.factories;

import com.catinthedark.flash_transmitter.lib.algorithm.ASCIIScheme;
import com.catinthedark.flash_transmitter.lib.algorithm.CompressedScheme;
import com.catinthedark.flash_transmitter.lib.algorithm.EncodingScheme;
import com.google.common.collect.ImmutableMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.Set;

/**
 * Created by ilyab_000 on 20.04.2014.
 */
public class EncodingSchemeFactory {
    public static final String defaultScheme = "8-bit ASCII";
    public static final Map<String, Class> SCHEMES_LIST = ImmutableMap.<String, Class>builder()
            .put("8-bit ASCII", ASCIIScheme.class)
            .put("5-bit char table", CompressedScheme.class)
            .build();

    public static EncodingScheme build(String klass) throws NotImplementedException {
        Class scheme = SCHEMES_LIST.get(klass);
        if (scheme != null) {
            try {
                return (EncodingScheme) scheme.newInstance();
            } catch (InstantiationException e) {
                throw new NotImplementedException();
            } catch (IllegalAccessException e) {
                throw new NotImplementedException();
            }
        } else {
            throw new NotImplementedException();
        }
    }

    public static String[] getSchemesNames() {
        Set<String> keys = SCHEMES_LIST.keySet();
        return keys.toArray(new String[keys.size()]);
    }
}

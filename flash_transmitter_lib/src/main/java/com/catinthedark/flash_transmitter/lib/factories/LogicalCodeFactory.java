package com.catinthedark.flash_transmitter.lib.factories;

import com.catinthedark.flash_transmitter.lib.algorithm.EmptyLogicalCodeLayer;
import com.catinthedark.flash_transmitter.lib.algorithm.LogicalCodeLayer;
import com.google.common.collect.ImmutableMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ilya on 06.06.2014.
 */
public class LogicalCodeFactory {
    public static final String defaultLogicalCode = "None";
    public static final Map<String, Class> LOGICAL_CODES_LIST = ImmutableMap.<String, Class>builder()
            .put("None", EmptyLogicalCodeLayer.class)
            .build();

    public static LogicalCodeLayer build(String klass) throws NotImplementedException {
        Class scheme = LOGICAL_CODES_LIST.get(klass);
        if (scheme != null) {
            try {
                return (LogicalCodeLayer) scheme.newInstance();
            } catch (InstantiationException e) {
                throw new NotImplementedException();
            } catch (IllegalAccessException e) {
                throw new NotImplementedException();
            }
        } else {
            throw new NotImplementedException();
        }
    }
    public static String[] getLogicalCodesNames() {
        Set<String> keys = LOGICAL_CODES_LIST.keySet();
        return keys.toArray(new String[keys.size()]);
    }
}

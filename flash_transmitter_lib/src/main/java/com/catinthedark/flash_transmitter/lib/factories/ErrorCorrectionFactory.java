package com.catinthedark.flash_transmitter.lib.factories;

import com.catinthedark.flash_transmitter.lib.algorithm.EmptyErrorCorrectionLayer;
import com.catinthedark.flash_transmitter.lib.algorithm.ErrorCorrectionLayer;
import com.google.common.collect.ImmutableMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ilya on 06.06.2014.
 */
public class ErrorCorrectionFactory {
    public static final String defaultErrorCorrection = "None";
    public static final Map<String, Class> ERROR_CORRECTION_LIST = ImmutableMap.<String, Class>builder()
            .put("None", EmptyErrorCorrectionLayer.class)
            .build();

    public static ErrorCorrectionLayer build(String klass) throws NotImplementedException {
        Class scheme = ERROR_CORRECTION_LIST.get(klass);
        if (scheme != null) {
            try {
                return (ErrorCorrectionLayer) scheme.newInstance();
            } catch (InstantiationException e) {
                throw new NotImplementedException();
            } catch (IllegalAccessException e) {
                throw new NotImplementedException();
            }
        } else {
            throw new NotImplementedException();
        }
    }
    public static String[] getErrorCorrectionNames() {
        Set<String> keys = ERROR_CORRECTION_LIST.keySet();
        return keys.toArray(new String[keys.size()]);
    }
}

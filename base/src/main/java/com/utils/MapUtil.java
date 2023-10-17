package com.utils;

import java.util.Map;

public class MapUtil {

    public static <T, K> T getMapValue(Map<K, T> map, K key, T defaultValue) {
        if (key == null || map == null)
            return defaultValue;

        if (map.containsKey(key))
            return map.get(key);
        else
            return defaultValue;
    }
}

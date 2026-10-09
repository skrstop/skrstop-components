package dev.morphia.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2025-09-23 14:10:01
 * @since 1.0.0
 */
public class CollectionUtil {

    public static <T> List<T> asList(T... a) {
        if (a == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(a).collect(Collectors.toList());
    }

    public static <T> Set<T> asSet(T... a) {
        if (a == null) {
            return new HashSet<>();
        }
        return Arrays.stream(a).collect(Collectors.toSet());
    }

    public static <K, V> Map<K, V> asMap(K key, V value) {
        HashMap<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2) {
        HashMap<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }


}

package fr.esgi.musteat.backend.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    private MapUtil() {
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new HashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
}

package org.benben.common.util;

import java.util.*;

public class  sortMapByValue{

    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
            Map<K, V> sortMap = new LinkedHashMap<>();
             map.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).forEach(entry -> sortMap.put(entry.getKey(), entry.getValue()));
            return sortMap;
        }
    public static <K, V extends Comparable<? super V>> Map<K, V> sortToList(Map<K, V> map,String flag) {
            Map<K, V> sortMap = new LinkedHashMap<>();
            if("asc".equals(flag)) {
                map.entrySet().stream()
                        .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                        .forEach(entry -> sortMap.put(entry.getKey(), entry.getValue()));
            } else {
                map.entrySet().stream()
                        .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                        .forEach(entry -> sortMap.put(entry.getKey(), entry.getValue()));
            }
            return sortMap;
        }
}

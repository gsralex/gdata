package com.gsralex.gdata;

import java.util.List;

/**
 * @author gsralex
 *         date: 2018/3/13
 */
public class TypeUtils {

    public static <T> Class<T> getType(List<T> list) {
        return (Class<T>) list.get(0).getClass();
    }
}

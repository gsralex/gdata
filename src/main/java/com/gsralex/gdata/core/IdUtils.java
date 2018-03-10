package com.gsralex.gdata.core;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author gsralex
 * @date 2018/3/3
 */
public class IdUtils {

    public static String longToInt(List<Long> idList) {
        StringBuilder ids = new StringBuilder();
        for (Long id : idList) {
            ids.append(id).append(",");
        }
        return StringUtils.removeEnd(ids.toString(), ",");
    }
}

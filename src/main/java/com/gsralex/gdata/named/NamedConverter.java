package com.gsralex.gdata.named;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public interface NamedConverter {

    <T> String convert(String namedSql, Class<T> type);

}

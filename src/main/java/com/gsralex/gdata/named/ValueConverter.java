package com.gsralex.gdata.named;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public interface ValueConverter {

    <T> SqlObject convert(String namedSql, Class<T> type, T t, Object[] objects);

}

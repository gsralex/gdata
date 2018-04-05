package com.gsralex.gdata.bean.placeholder;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public interface PConverter {

    <T> String convert(String namedSql, Class<T> type);

}

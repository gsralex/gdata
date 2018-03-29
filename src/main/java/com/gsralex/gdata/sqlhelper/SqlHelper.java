package com.gsralex.gdata.sqlhelper;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public interface SqlHelper {

    <T> boolean checkValid(Class<T> type);

    <T> String getSql(Class<T> type);

    <T> Object[] getObjects(T t);

}

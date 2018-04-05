package com.gsralex.gdata.bean.sqlstatement;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public interface SqlStatement {

    <T> boolean checkValid(Class<T> type);

    <T> String getSql(Class<T> type);

    <T> Object[] getObjects(T t);

}

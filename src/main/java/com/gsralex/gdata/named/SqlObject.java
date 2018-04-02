package com.gsralex.gdata.named;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class SqlObject {

    private String sql;
    private Object[] objects;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}

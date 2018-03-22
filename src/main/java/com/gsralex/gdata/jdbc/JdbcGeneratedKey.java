package com.gsralex.gdata.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class JdbcGeneratedKey {

    private List<Map<String, Object>> keyList;
    private int r;

    public JdbcGeneratedKey(int r) {
        this.r = r;
        keyList = new ArrayList<>();
    }

    public int getR() {
        return r;
    }

    public List<Map<String, Object>> getKeyList() {
        return keyList;
    }

    public Map<String, Object> get(int row) {
        return keyList.get(row);
    }

}

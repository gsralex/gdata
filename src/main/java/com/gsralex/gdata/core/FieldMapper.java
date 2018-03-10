package com.gsralex.gdata.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gsralex
 * @date 2018/2/18
 */
public class FieldMapper {

    private String tableName;
    private Map<String, FieldColumn> mapper;

    public FieldMapper() {
        mapper = new HashMap<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, FieldColumn> getMapper() {
        return mapper;
    }

    public void setMapper(Map<String, FieldColumn> mapper) {
        this.mapper = mapper;
    }
}

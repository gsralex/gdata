package com.gsralex.gdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @date 2018/2/18
 */
public class ModelMap {

    private String tableName;
    private Map<String, FieldColumn> mapper;
    private Map<FieldEnum, List<FieldColumn>> fieldMapper;

    public ModelMap() {
        mapper = new HashMap<>();
        fieldMapper = new HashMap<>();
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

    public Map<FieldEnum, List<FieldColumn>> getFieldMapper() {
        return fieldMapper;
    }

}

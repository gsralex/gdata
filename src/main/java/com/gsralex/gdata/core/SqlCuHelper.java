package com.gsralex.gdata.core;

import com.gsralex.gdata.core.annotation.IdField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @date 2018/3/10
 */
public class SqlCuHelper {


    public <T> Object[] getInsertObjects(T t) {
        ModelMap mapper = ModelMapper.getMapperCache(t.getClass());
        FieldValue fieldValue = new FieldValue(t, t.getClass());
        List<Object> objects = new ArrayList<>();
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                Object value = fieldValue.getValue(entry.getValue().getClass(), entry.getKey());
                objects.add(value);
            }
        }
        Object[] objArray = new Object[objects.size()];
        objects.toArray(objArray);
        return objArray;
    }


    public <T> boolean isAutoGenerate(Class<T> type) {
        ModelMap mapper = ModelMapper.getMapperCache(type);
        return mapper.getFieldMapper().get(FieldEnum.Id).size() != 0 ? true : false;
    }

    public <T> List<FieldColumn> getColumns(Class<T> type, FieldEnum fieldEnum) {
        ModelMap mapper = ModelMapper.getMapperCache(type);
        return mapper.getFieldMapper().get(FieldEnum.Id);
    }

    public <T> String getInsertSql(Class<T> type) {
        ModelMap mapper = ModelMapper.getMapperCache(type);
        String sql = String.format("insert into `%s`", mapper.getTableName());
        String insertSql = "(";
        String valueSql = " values(";
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                insertSql += String.format("`%s`,", column.getAliasName());
                valueSql += "?,";
            }
        }
        insertSql = StringUtils.removeEnd(insertSql, ",");
        insertSql += ")";
        valueSql = StringUtils.removeEnd(valueSql, ",");
        valueSql += ")";
        return sql + insertSql + valueSql;
    }


    public <T> String getUpdateSql(Class<T> type) {
        ModelMap mapper = ModelMapper.getMapperCache(type);
        String sql = String.format("update `%s` set ", mapper.getTableName());
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                sql += String.format("`%s`=?,", column.getAliasName());
            }
        }
        sql = StringUtils.removeEnd(sql, ",");
        sql += " where 1=1 ";
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                sql += String.format("and %s=?,", column.getAliasName());
            }
        }
        sql = StringUtils.removeEnd(sql, ",");
        return sql;
    }

    public <T> Object[] getUpdateObjects(T t) {
        ModelMap mapper = ModelMapper.getMapperCache(t.getClass());
        FieldValue fieldValue = new FieldValue(t, t.getClass());
        List<Object> objects = new ArrayList<>();

        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                Object value = fieldValue.getValue(entry.getValue().getClass(), entry.getKey());
                objects.add(value);
            }
        }
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                Object value = fieldValue.getValue(entry.getValue().getClass(), entry.getKey());
                objects.add(value);
            }
        }
        Object[] objArray = new Object[objects.size()];
        objects.toArray(objArray);
        return objArray;
    }
}

package com.gsralex.gdata;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 *         date: 2018/3/15
 */
public class SqlUpdateHelper {

    public <T> String getUpdateSql(Class<T> type) {
        ModelMapper mapper = ModelMapperHolder.getMapperCache(type);
        String sql = String.format("update `%s` set ", mapper.getTableName());
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                sql += String.format("`%s`=?,", column.getLabel());
            }
        }
        sql = StringUtils.removeEnd(sql, ",");
        sql += " where 1=1 ";
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                sql += String.format("and %s=?,", column.getLabel());
            }
        }
        sql = StringUtils.removeEnd(sql, ",");
        return sql;
    }

    public <T> Object[] getUpdateObjects(T t) {
        ModelMapper modelMapper = ModelMapperHolder.getMapperCache(t.getClass());
        FieldValue fieldValue = new FieldValue(t);
        List<Object> objects = new ArrayList<>();

        for (Map.Entry<String, FieldColumn> entry : modelMapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                Object value = fieldValue.getValue(column.getType(),entry.getKey());
                objects.add(value);
            }
        }
        for (Map.Entry<String, FieldColumn> entry : modelMapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                Object value = fieldValue.getValue(column.getType(),entry.getKey());
                objects.add(value);
            }
        }
        Object[] objArray = new Object[objects.size()];
        objects.toArray(objArray);
        return objArray;
    }
}

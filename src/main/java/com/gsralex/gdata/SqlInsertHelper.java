package com.gsralex.gdata;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 *         date: 2018/3/15
 */
public class SqlInsertHelper {

    public <T> Object[] getInsertObjects(T t) {
        ModelMapper modelMapper = ModelMapperHolder.getMapperCache(t.getClass());
        FieldValue fieldValue = new FieldValue(t);
        List<Object> objects = new ArrayList<>();
        for (Map.Entry<String, FieldColumn> entry : modelMapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                Object value = fieldValue.getValue(column.getType(), entry.getKey());
                objects.add(value);
            }
        }
        Object[] objArray = new Object[objects.size()];
        objects.toArray(objArray);
        return objArray;
    }

    public <T> List<FieldColumn> getColumns(Class<T> type, FieldEnum fieldEnum) {
        ModelMapper mapper = ModelMapperHolder.getMapperCache(type);
        return mapper.getFieldMapper().get(FieldEnum.Id);
    }

    public <T> String getInsertSql(Class<T> type) {
        ModelMapper mapper = ModelMapperHolder.getMapperCache(type);
        String sql = String.format("insert into `%s`", mapper.getTableName());
        String insertSql = "(";
        String valueSql = " values(";
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                insertSql += String.format("`%s`,", column.getLabel());
                valueSql += "?,";
            }
        }
        insertSql = StringUtils.removeEnd(insertSql, ",");
        insertSql += ")";
        valueSql = StringUtils.removeEnd(valueSql, ",");
        valueSql += ")";
        return sql + insertSql + valueSql;
    }
}

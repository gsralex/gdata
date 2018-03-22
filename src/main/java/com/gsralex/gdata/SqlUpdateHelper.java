package com.gsralex.gdata;

import com.gsralex.gdata.exception.GdataException;
import com.gsralex.gdata.exception.GdataMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class SqlUpdateHelper {

    public <T> boolean checkValid(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        if (mapper.getFieldMapper().get(FieldEnum.Id).size() == 0) {
            throw new GdataException(GdataMessage.NOTID_FORUPDATE);
        }
        return true;
    }

    public <T> String getUpdateSql(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        String sql = String.format("update `%s` set ", mapper.getTableName());
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                sql += String.format("`%s`=?,", column.getLabel());
            }
        }
        sql = StringUtils.removeEnd(sql, ",");
        sql += " where ";
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                sql += String.format(" `%s`=? and", column.getLabel());
            }
        }
        sql = StringUtils.removeEnd(sql, "and");
        return sql;
    }

    public <T> Object[] getUpdateObjects(T t) {
        Mapper mapper = MapperHolder.getMapperCache(t.getClass());
        FieldValue fieldValue = new FieldValue(t);
        List<Object> objects = new ArrayList<>();

        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                Object value = fieldValue.getValue(column.getType(), entry.getKey());
                objects.add(value);
            }
        }
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                Object value = fieldValue.getValue(column.getType(), entry.getKey());
                objects.add(value);
            }
        }
        Object[] objArray = new Object[objects.size()];
        objects.toArray(objArray);
        return objArray;
    }
}

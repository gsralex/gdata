package com.gsralex.gdata;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

/**
 * @author gsralex
 *         2018/3/10
 */
public class SqlMapperHelper {


    public <T> T mapperEntity(ResultSet resultSet, Class<T> type) {
        T instance = null;
        try {
            instance = type.newInstance();
            Mapper mapper = MapperHolder.getMapperCache(type);
            FieldValue fieldValue = new FieldValue(instance);
            mapper(resultSet, mapper, fieldValue);
            return (T) fieldValue.getInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return instance;
    }


    public void mapper(ResultSet rs, Mapper mapper, FieldValue fieldValue) {
        for (Map.Entry<String, FieldColumn> item : mapper.getMapper().entrySet()) {
            setRsValue(rs, item.getValue(), fieldValue);
        }
    }


    public void setRsValue(ResultSet rs, FieldColumn column, FieldValue fieldValue) {
        try {
            String fieldName = column.getName();
            String label = column.getLabel();
            Class type = column.getType();
            if (type == int.class || type == Integer.class) {
                fieldValue.setValue(type, fieldName, rs.getInt(label));
            } else if (type == float.class || type == Float.class) {
                fieldValue.setValue(type, fieldName, rs.getFloat(label));
            } else if (type == double.class || type == Double.class) {
                fieldValue.setValue(type, fieldName, rs.getDouble(label));
            } else if (type == long.class || type == Long.class) {
                fieldValue.setValue(type, fieldName, rs.getLong(label));
            } else if (type == String.class) {
                fieldValue.setValue(type, fieldName, rs.getString(label));
            } else if (type == Date.class) {
                fieldValue.setValue(type, fieldName, rs.getDate(label));
            } else if (type == boolean.class || type == Boolean.class) {
                fieldValue.setValue(type, fieldName, rs.getBoolean(label));
            } else if (type == byte.class || type == Byte.class) {
                fieldValue.setValue(type, fieldName, rs.getByte(label));
            } else if (type == short.class || type == Short.class) {
                fieldValue.setValue(type, fieldName, rs.getShort(label));
            } else if (type == BigDecimal.class) {
                fieldValue.setValue(type, fieldName, rs.getBigDecimal(label));
            } else {
                fieldValue.setValue(type, fieldName, rs.getObject(label));
            }
        } catch (Throwable e) {
        }
    }
}

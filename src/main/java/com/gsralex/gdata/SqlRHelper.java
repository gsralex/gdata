package com.gsralex.gdata;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

/**
 * @author gsralex
 * @date 2018/3/10
 */
public class SqlRHelper {


    public <T> T mapperEntity(ResultSet resultSet, Class<T> type) {
        T instance = null;
        try {
            instance = type.newInstance();
            ModelMap modelMap = ModelMapper.getMapperCache(type);
            FieldValue fieldValue = new FieldValue(instance, modelMap);
            return mapper(resultSet, modelMap, fieldValue);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public <T> T mapper(ResultSet rs, ModelMap fieldMapper, FieldValue fieldValue) {
        try {
            for (Map.Entry<String, FieldColumn> item : fieldMapper.getMapper().entrySet()) {
                String fieldName = item.getKey();
                String columnName = item.getValue().getAliasName();
                Class type = item.getValue().getType();
                if (type == int.class || type == Integer.class) {
                    fieldValue.setValue(fieldName, rs.getInt(columnName));
                } else if (type == float.class || type == Float.class) {
                    fieldValue.setValue(fieldName, rs.getFloat(columnName));
                } else if (type == double.class || type == Double.class) {
                    fieldValue.setValue(fieldName, rs.getDouble(columnName));
                } else if (type == long.class || type == Long.class) {
                    fieldValue.setValue(fieldName, rs.getLong(columnName));
                } else if (type == String.class) {
                    fieldValue.setValue(fieldName, rs.getString(columnName));
                } else if (type == Date.class) {
                    fieldValue.setValue(fieldName, rs.getDate(columnName));
                } else if (type == boolean.class || type == Boolean.class) {
                    fieldValue.setValue(fieldName, rs.getBoolean(columnName));
                } else if (type == byte.class || type == Byte.class) {
                    fieldValue.setValue(fieldName, rs.getByte(columnName));
                } else if (type == short.class || type == Short.class) {
                    fieldValue.setValue(fieldName, rs.getShort(columnName));
                } else if (type == BigDecimal.class) {
                    fieldValue.setValue(fieldName, rs.getBigDecimal(columnName));
                } else {
                    fieldValue.setValue(fieldName, rs.getObject(columnName));
                }
            }
            return (T) fieldValue.getInstance();
        } catch (Throwable e) {
            return null;
        }
    }
}

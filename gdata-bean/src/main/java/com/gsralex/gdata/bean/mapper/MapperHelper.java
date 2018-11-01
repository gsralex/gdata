package com.gsralex.gdata.bean.mapper;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class MapperHelper {


    public <T> T mapperEntity(ResultSet resultSet, Set<String> columnSet, Class<T> type) {
        if (isSimple(type)) {
            return mapperSimpleEntity(resultSet, type);
        } else {
            return mapperComplexEntity(resultSet, columnSet, type);
        }
    }

    private <T> T mapperSimpleEntity(ResultSet resultSet, Class<T> type) {
        try {
            return (T) getRsValue(resultSet, 1, null, type);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    private <T> T mapperComplexEntity(ResultSet resultSet, Set<String> columnSet, Class<T> type) {
        T instance = null;
        try {
            instance = type.newInstance();
            Mapper mapper = MapperHolder.getMapperCache(type);
            FieldValue fieldValue = new FieldValue(instance);
            for (Map.Entry<String, FieldColumn> item : mapper.getMapper().entrySet()) {
                String label = StringUtils.lowerCase(item.getValue().getLabel());
                if (columnSet.contains(label)) {
                    Class fieldType = item.getValue().getType();
                    Object object = getRsValue(resultSet, 0, label, fieldType);
                    setRsValue(object, item.getValue(), fieldValue);
                }
            }
            return (T) fieldValue.getInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public boolean isSimple(Class type) {
        if (type == int.class || type == Integer.class
                || type == float.class || type == Float.class
                || type == double.class || type == Double.class
                || type == long.class || type == Long.class
                || type == String.class
                || type == Date.class
                || type == boolean.class || type == Boolean.class
                || type == byte.class || type == Byte.class
                || type == short.class || type == Short.class
                || type == BigDecimal.class) {
            return true;
        }
        return false;
    }

    public Object getRsValue(ResultSet rs, int columnIndex, String label, Class type) {
        boolean isEmptyLabel = StringUtils.isEmpty(label);
        Object result;
        try {
            if (type == int.class || type == Integer.class) {
                result = isEmptyLabel ? rs.getInt(columnIndex) : rs.getInt(label);
            } else if (type == float.class || type == Float.class) {
                result = isEmptyLabel ? rs.getFloat(columnIndex) : rs.getFloat(label);
            } else if (type == double.class || type == Double.class) {
                result = isEmptyLabel ? rs.getDouble(columnIndex) : rs.getDouble(label);
            } else if (type == long.class || type == Long.class) {
                result = isEmptyLabel ? rs.getLong(columnIndex) : rs.getLong(label);
            } else if (type == String.class) {
                result = isEmptyLabel ? rs.getString(columnIndex) : rs.getString(label);
            } else if (type == Date.class) {
                result = isEmptyLabel ? rs.getTimestamp(columnIndex) : rs.getTimestamp(label);
            } else if (type == boolean.class || type == Boolean.class) {
                result = isEmptyLabel ? rs.getBoolean(columnIndex) : rs.getBoolean(label);
            } else if (type == byte.class || type == Byte.class) {
                result = isEmptyLabel ? rs.getByte(columnIndex) : rs.getByte(label);
            } else if (type == short.class || type == Short.class) {
                result = isEmptyLabel ? rs.getShort(columnIndex) : rs.getShort(label);
            } else if (type == BigDecimal.class) {
                result = isEmptyLabel ? rs.getBigDecimal(columnIndex) : rs.getBigDecimal(label);
            } else {
                result = isEmptyLabel ? rs.getObject(columnIndex) : rs.getObject(label);
            }
            if (rs.wasNull()) {
                return null;
            } else {
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setRsValue(Object value, FieldColumn column, FieldValue fieldValue) {
        try {
            String fieldName = column.getName();
            Class type = column.getType();
            fieldValue.setValue(type, fieldName, value);
        } catch (Throwable e) {
        }
    }
}

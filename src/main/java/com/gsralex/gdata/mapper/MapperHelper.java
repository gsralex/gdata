package com.gsralex.gdata.mapper;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class MapperHelper {


    public <T> T mapperEntity(ResultSet resultSet, Class<T> type) {
        if (isSimple(type)) {
            return mapperSimpleEntity(resultSet, type);
        } else {
            return mapperComplexEntity(resultSet, type);
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


    private <T> T mapperComplexEntity(ResultSet resultSet, Class<T> type) {
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
        try {
            if (type == int.class || type == Integer.class) {
                return isEmptyLabel ? rs.getInt(columnIndex) : rs.getInt(label);
            } else if (type == float.class || type == Float.class) {
                return isEmptyLabel ? rs.getFloat(columnIndex) : rs.getFloat(label);
            } else if (type == double.class || type == Double.class) {
                return isEmptyLabel ? rs.getDouble(columnIndex) : rs.getDouble(label);
            } else if (type == long.class || type == Long.class) {
                return isEmptyLabel ? rs.getLong(columnIndex) : rs.getLong(label);
            } else if (type == String.class) {
                return isEmptyLabel ? rs.getString(columnIndex) : rs.getString(label);
            } else if (type == Date.class) {
                return isEmptyLabel ? rs.getDate(columnIndex) : rs.getDate(label);
            } else if (type == boolean.class || type == Boolean.class) {
                return isEmptyLabel ? rs.getBoolean(columnIndex) : rs.getBoolean(label);
            } else if (type == byte.class || type == Byte.class) {
                return isEmptyLabel ? rs.getByte(columnIndex) : rs.getByte(label);
            } else if (type == short.class || type == Short.class) {
                return isEmptyLabel ? rs.getShort(columnIndex) : rs.getShort(label);
            } else if (type == BigDecimal.class) {
                return isEmptyLabel ? rs.getBigDecimal(columnIndex) : rs.getBigDecimal(label);
            } else {
                return rs.getObject(columnIndex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setRsValue(ResultSet rs, FieldColumn column, FieldValue fieldValue) {
        try {
            String fieldName = column.getName();
            String label = column.getLabel();
            Class type = column.getType();
            Object value = getRsValue(rs, 0, label, type);
            fieldValue.setValue(type, fieldName, value);
        } catch (Throwable e) {
        }
    }
}

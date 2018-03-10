package com.gsralex.gdata;

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
            ModelMap fieldMapper = ModelMapper.getMapperCache(type);
            FieldValue fieldValue = new FieldValue(instance, type);
            return mapper(resultSet, fieldMapper, fieldValue);

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
                String name = item.getKey();
                String columnName = item.getValue().getAliasName();
                switch (item.getValue().getTypeName()) {
                    case "java.lang.String": {
                        fieldValue.setValue(String.class, name, rs.getString(columnName));
                        break;
                    }
                    case "int": {
                        fieldValue.setValue(Integer.class, name, rs.getInt(name));
                        break;
                    }
                    case "long": {
                        fieldValue.setValue(Long.class, name, rs.getLong(name));
                        break;
                    }
                    case "double": {
                        fieldValue.setValue(Double.class, name, rs.getDouble(name));
                        break;
                    }
                    case "float": {
                        fieldValue.setValue(Float.class, name, rs.getFloat(name));
                        break;
                    }
                    case "boolean": {
                        fieldValue.setValue(Boolean.class, name, rs.getBoolean(name));
                        break;
                    }
                    case "java.util.Date": {
                        fieldValue.setValue(Date.class, name, rs.getDate(name));
                        break;
                    }
                }
            }
            return (T) fieldValue.getInstance();
        } catch (Throwable e) {
            return null;
        }
    }
}

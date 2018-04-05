package com.gsralex.gdata.bean.placeholder;

import com.gsralex.gdata.bean.exception.DataException;
import com.gsralex.gdata.bean.exception.ExceptionMessage;
import com.gsralex.gdata.bean.mapper.Mapper;
import com.gsralex.gdata.bean.mapper.FieldColumn;
import com.gsralex.gdata.bean.mapper.FieldValue;
import com.gsralex.gdata.bean.mapper.MapperHolder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class ValueConverterImpl implements ValueConverter {


    private static final Pattern valuePattern = Pattern.compile("\\:(?<value>(\\w+))", Pattern.CASE_INSENSITIVE);

    @Override
    public SqlObject convertBeanSource(String pSql, BeanSource beanSource) {
        SqlObject sqlObject = new SqlObject();
        Object object = beanSource.getValue();
        if (object != null) {
            List<Object> objectsList = new ArrayList<>();
            String sql = pSql;
            Class type = object.getClass();
            FieldValue fieldValue = new FieldValue(object);
            Mapper mapper = MapperHolder.getMapperCache(type);
            Matcher matcher = valuePattern.matcher(sql);
            while (matcher.find()) {
                String key = matcher.group("value");
                FieldColumn column = mapper.getMapper().get(key);
                if (column == null) {
                    throw new DataException(ExceptionMessage.PLH_NOTPROPBEANSOURCE);
                }
                sql = matcher.replaceFirst("?");
                Class fieldType = column.getType();
                String fieldName = key;
                objectsList.add(fieldValue.getValue(fieldType, fieldName));
                matcher = valuePattern.matcher(sql);
            }
            sqlObject.setSql(sql);
            Object[] objects = new Object[objectsList.size()];
            objectsList.toArray(objects);
            sqlObject.setObjects(objects);
        }
        return sqlObject;
    }

    @Override
    public SqlObject convertMap(String pSql, Map<String, Object> paramMap) {
        SqlObject sqlObject = new SqlObject();
        if (paramMap != null) {
            //map key lowercase
            Map<String, Object> newParamMap = mapKeyToLowerCase(paramMap);
            List<Object> objectsList = new ArrayList<>();
            String sql = pSql;
            Matcher matcher = valuePattern.matcher(sql);
            while (matcher.find()) {
                String key = matcher.group("value");
                key = key.toLowerCase();
                Object value = newParamMap.get(key);
                if (value == null) {
                    throw new DataException(ExceptionMessage.PLH_NOTKEYMAP);
                }
                sql = matcher.replaceFirst("?");
                objectsList.add(value);
                matcher = valuePattern.matcher(sql);
            }
            sqlObject.setSql(sql);
            Object[] objects = new Object[objectsList.size()];
            objectsList.toArray(objects);
            sqlObject.setObjects(objects);
        }
        return sqlObject;
    }

    private Map<String, Object> mapKeyToLowerCase(Map<String, Object> paramMap) {
        Map<String, Object> newParamMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            newParamMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return newParamMap;
    }

    private static class ValueConverterImplHolder {
        private final static ValueConverter instance = new ValueConverterImpl();
    }

    public static ValueConverter getInstance() {
        return ValueConverterImplHolder.instance;
    }
}

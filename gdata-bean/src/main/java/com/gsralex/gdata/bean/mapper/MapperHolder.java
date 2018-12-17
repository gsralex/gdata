package com.gsralex.gdata.bean.mapper;


import com.gsralex.gdata.bean.annotation.Column;
import com.gsralex.gdata.bean.annotation.Id;
import com.gsralex.gdata.bean.annotation.Ignore;
import com.gsralex.gdata.bean.annotation.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/2/18
 */
public class MapperHolder {

    private static Map<String, Mapper> cacheMapper = new HashMap<>();
    private static Object sync = new Object();


    public static Mapper getMapperCache(Class type) {
        String className = type.getName();
        Mapper mapper = cacheMapper.get(className);
        if (mapper != null)
            return mapper;

        synchronized (sync) {
            mapper = cacheMapper.get(className);
            if (mapper != null)
                return mapper;
            Mapper fieldMapper = getMapper(type);
            cacheMapper.put(className, fieldMapper);
            return fieldMapper;
        }
    }


    private static Mapper getMapper(Class type) {
        String tableName;
        Table tbName = (Table) type.getAnnotation(Table.class);
        if (tbName != null) {
            tableName = tbName.value();
        } else {
            tableName = type.getSimpleName();
        }

        Mapper mapper = new Mapper();
        mapper.setType(type);
        mapper.setTableName(tableName);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String filedName = field.getName();
            Ignore ignore = field.getAnnotation(Ignore.class);
            if (ignore != null) {
                continue;
            }
            FieldColumn column = new FieldColumn();
            column.setType(field.getType());
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                column.setId(true);
                column.setGeneratedKey(id.generatedKey());
                mapper.getIdColumns().add(column);
            }
            Column aliasField = field.getAnnotation(Column.class);
            if (aliasField != null) {
                column.setName(field.getName());
                column.setLabel(aliasField.value());
            }


            if (column.getLabel()==null || column.getLabel().length()==0) {
                column.setName(field.getName());
                column.setLabel(field.getName());
            }

            mapper.getMapper().put(filedName, column);
        }
        return mapper;
    }


}
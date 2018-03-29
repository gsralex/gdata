package com.gsralex.gdata.mapper;


import com.gsralex.gdata.annotation.LabelField;
import com.gsralex.gdata.annotation.IdField;
import com.gsralex.gdata.annotation.IgnoreField;
import com.gsralex.gdata.annotation.Table;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
        String className = type.getName();
        String tableName;
        Table tbName = (Table) type.getAnnotation(Table.class);
        if (tbName != null) {
            tableName = tbName.name();
        } else {
            tableName = className;
        }

        Mapper mapper = new Mapper();
        mapper.setType(type);
        mapper.setTableName(tableName);
        initFieldMap(mapper);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String filedName = field.getName();
            IgnoreField ignoreField = field.getAnnotation(IgnoreField.class);
            if (ignoreField != null) {
                continue;
            }
            FieldColumn column = new FieldColumn();
            column.setType(field.getType());
            IdField idField = field.getAnnotation(IdField.class);
            if (idField != null) {
                column.setId(true);
                column.setGeneratedKey(idField.generatedKey());
                column.setName(field.getName());
                if (!StringUtils.isEmpty(idField.name())) {
                    column.setLabel(idField.name());
                } else {
                    column.setLabel(field.getName());
                }

                addFieldMap(FieldEnum.Id, column, mapper);
            }
            LabelField aliasField = field.getAnnotation(LabelField.class);
            if (aliasField != null) {
                column.setName(field.getName());
                column.setLabel(aliasField.name());
                addFieldMap(FieldEnum.Label, column, mapper);
            }

            if (!column.isId()) {
                if (StringUtils.isEmpty(column.getLabel())) {
                    column.setName(field.getName());
                    column.setLabel(field.getName());
                    addFieldMap(FieldEnum.Label, column, mapper);
                }
            }
            mapper.getMapper().put(filedName, column);
        }
        return mapper;
    }

    private static void initFieldMap(Mapper mapper) {
        for (FieldEnum fieldEnum : FieldEnum.values()) {
            mapper.getFieldMapper().put(fieldEnum, new ArrayList<>());
        }
    }

    private static void addFieldMap(FieldEnum fieldEnum, FieldColumn column, Mapper mapper) {
        mapper.getFieldMapper().get(fieldEnum).add(column);
    }

}
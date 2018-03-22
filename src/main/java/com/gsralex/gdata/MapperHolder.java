package com.gsralex.gdata;


import com.gsralex.gdata.annotation.LabelField;
import com.gsralex.gdata.annotation.IdField;
import com.gsralex.gdata.annotation.IgnoreField;
import com.gsralex.gdata.annotation.Table;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/2/18
 */
public class MapperHolder {

    private static Map<String, Mapper> cacheMapper = new HashMap<>();

    public static Mapper getMapperCache(Class type) {
        String className = type.getName();
        if (cacheMapper.containsKey(className)) {
            return cacheMapper.get(className);
        }
        Mapper fieldMapper = getMapper(type);
        cacheMapper.put(className, fieldMapper);
        return fieldMapper;
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

    private static void addFieldMap(FieldEnum fieldEnum, FieldColumn column, Mapper mapper) {
        if (mapper.getFieldMapper().containsKey(fieldEnum)) {
            mapper.getFieldMapper().get(fieldEnum).add(column);
        } else {
            List<FieldColumn> list = new ArrayList<>();
            list.add(column);
            mapper.getFieldMapper().put(fieldEnum, list);
        }
    }

}
package com.gsralex.gdata;


import com.gsralex.gdata.annotation.LabelField;
import com.gsralex.gdata.annotation.IdField;
import com.gsralex.gdata.annotation.IgnoreField;
import com.gsralex.gdata.annotation.TbName;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * 2018/2/18
 */
public class ModelMapperHolder {

    private static Map<String, ModelMapper> cacheMapper = new HashMap<>();

    public static ModelMapper getMapperCache(Class type) {
        String className = type.getName();
        if (cacheMapper.containsKey(className)) {
            return cacheMapper.get(className);
        }
        ModelMapper fieldMapper = getMapper(type);
        cacheMapper.put(className, fieldMapper);
        return fieldMapper;
    }

    private static ModelMapper getMapper(Class type) {
        String className = type.getName();
        String tableName;
        TbName tbName = (TbName) type.getAnnotation(TbName.class);
        if (tbName != null) {
            tableName = tbName.name();
        } else {
            tableName = className;
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.setType(type);
        modelMapper.setTableName(tableName);
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

                addFieldMap(FieldEnum.Id, column, modelMapper);
            }
            LabelField aliasField = field.getAnnotation(LabelField.class);
            if (aliasField != null) {
                column.setName(field.getName());
                column.setLabel(aliasField.name());
                addFieldMap(FieldEnum.Label, column, modelMapper);
            }

            if (!column.isId()) {
                if (StringUtils.isEmpty(column.getLabel())) {
                    column.setName(field.getName());
                    column.setLabel(field.getName());
                    addFieldMap(FieldEnum.Label, column, modelMapper);
                }
            }
            modelMapper.getMapper().put(filedName, column);
        }
        return modelMapper;
    }

    private static void addFieldMap(FieldEnum fieldEnum, FieldColumn column, ModelMapper modelMapper) {
        if (modelMapper.getFieldMapper().containsKey(fieldEnum)) {
            modelMapper.getFieldMapper().get(fieldEnum).add(column);
        } else {
            List<FieldColumn> list = new ArrayList<>();
            list.add(column);
            modelMapper.getFieldMapper().put(fieldEnum, list);
        }
    }

}
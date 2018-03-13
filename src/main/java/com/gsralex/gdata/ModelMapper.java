package com.gsralex.gdata;


import com.gsralex.gdata.annotation.AliasField;
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
public class ModelMapper {

    private static Map<String, ModelMap> cacheMapper = new HashMap<>();

    public static ModelMap getMapperCache(Class type) {
        String className = type.getName();
        if (cacheMapper.containsKey(className)) {
            return cacheMapper.get(className);
        }
        ModelMap fieldMapper = getMapper(type);
        cacheMapper.put(className, fieldMapper);
        return fieldMapper;
    }

    private static ModelMap getMapper(Class type) {
        String className = type.getName();
        String tableName;
        TbName tbName = (TbName) type.getAnnotation(TbName.class);
        if (tbName != null) {
            tableName = tbName.name();
        } else {
            tableName = className;
        }

        ModelMap modelMap = new ModelMap();
        modelMap.setType(type);
        modelMap.setTableName(tableName);
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
                    column.setAliasName(idField.name());
                } else {
                    column.setAliasName(field.getName());
                }

                addFieldMap(FieldEnum.Id, column, modelMap);
            }
            AliasField aliasField = field.getAnnotation(AliasField.class);
            if (aliasField != null) {
                column.setAlias(true);
                column.setName(field.getName());
                column.setAliasName(aliasField.name());
                addFieldMap(FieldEnum.Alias, column, modelMap);
            }

            if (!column.isId()) {
                if (!column.isAlias()) {
                    column.setName(field.getName());
                    column.setAliasName(field.getName());
                    addFieldMap(FieldEnum.Alias, column, modelMap);
                }
            }
            modelMap.getMapper().put(filedName, column);
        }
        return modelMap;
    }

    private static void addFieldMap(FieldEnum fieldEnum, FieldColumn column, ModelMap modelMap) {
        if (modelMap.getFieldMapper().containsKey(fieldEnum)) {
            modelMap.getFieldMapper().get(fieldEnum).add(column);
        } else {
            List<FieldColumn> list = new ArrayList<>();
            list.add(column);
            modelMap.getFieldMapper().put(fieldEnum, list);
        }
    }

}
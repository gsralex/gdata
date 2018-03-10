package com.gsralex.gdata.core;


import com.gsralex.gdata.core.annotation.AliasField;
import com.gsralex.gdata.core.annotation.IdField;
import com.gsralex.gdata.core.annotation.IgnoreField;
import com.gsralex.gdata.core.annotation.TbName;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gsralex
 * @date 2018/2/18
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

        ModelMap fieldMapper = new ModelMap();
        fieldMapper.setTableName(tableName);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String filedName = field.getName();
            IgnoreField ignoreField = field.getAnnotation(IgnoreField.class);
            if (ignoreField != null) {
                continue;
            }
            FieldColumn column = new FieldColumn();
            IdField idField = field.getAnnotation(IdField.class);
            if (idField != null) {
                column.setId(true);
                column.setName(field.getName());
                if (!StringUtils.isEmpty(idField.name())) {
                    column.setAliasName(idField.name());
                } else {
                    column.setAliasName(field.getName());
                }
            }
            AliasField aliasField = field.getAnnotation(AliasField.class);
            if (aliasField != null) {
                column.setAlias(true);
                column.setName(field.getName());
                column.setAliasName(aliasField.name());
            }

            if (!column.isId()) {
                if (!column.isAlias()) {
                    column.setName(field.getName());
                    column.setAliasName(field.getName());
                }
            }
            fieldMapper.getMapper().put(filedName, column);
        }
        return fieldMapper;
    }

}

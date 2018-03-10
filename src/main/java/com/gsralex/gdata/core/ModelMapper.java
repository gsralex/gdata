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

    private static Map<String, FieldMapper> cacheMapper = new HashMap<>();

    public static FieldMapper getMapper(Class type) {
        String className = type.getName();
        if (cacheMapper.containsKey(className)) {
            return cacheMapper.get(className);
        }
        String tableName;
        TbName tbName = (TbName) type.getAnnotation(TbName.class);
        if (tbName != null) {
            tableName = tbName.name();
        } else {
            tableName = className;
        }

        FieldMapper fieldMapper = new FieldMapper();
        fieldMapper.setTableName(tableName);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String filedName = field.getName();
            String typeName = field.getGenericType().getTypeName();
            IgnoreField ignoreField = field.getAnnotation(IgnoreField.class);
            if (ignoreField != null) {
                continue;
            }

            FieldColumn column = new FieldColumn();
            IdField idField = field.getAnnotation(IdField.class);
            if (idField != null) {
                column.setId(true);
                column.setName(field.getName());
                if(!StringUtils.isEmpty(idField.name())){
                    column.setAliasName(idField.name());
                }else{
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
        cacheMapper.put(tableName, fieldMapper);
        return fieldMapper;
    }

}

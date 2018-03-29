package com.gsralex.gdata.sqlhelper;

import com.gsralex.gdata.mapper.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class SqlInsertHelper implements SqlHelper {

    public <T> boolean existsGenerateKey(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        int generatedKeyCnt = 0;
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isGeneratedKey()) {
                generatedKeyCnt++;
            }
        }
        if (generatedKeyCnt == 0) {
            return false;
        }
        return true;
    }

    public <T> List<FieldColumn> getIdColumns(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        return mapper.getFieldMapper().get(FieldEnum.Id);

    }

    @Override
    public <T> boolean checkValid(Class<T> type) {
        return true;
    }

    @Override
    public <T> String getSql(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        String sql = String.format("insert into `%s`", mapper.getTableName());
        String insertSql = "(";
        String valueSql = " values(";
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!(column.isId() && column.isGeneratedKey())) {
                insertSql += String.format("`%s`,", column.getLabel());
                valueSql += "?,";
            }
        }
        insertSql = StringUtils.removeEnd(insertSql, ",");
        insertSql += ")";
        valueSql = StringUtils.removeEnd(valueSql, ",");
        valueSql += ")";
        return sql + insertSql + valueSql;
    }

    @Override
    public <T> Object[] getObjects(T t) {
        Mapper mapper = MapperHolder.getMapperCache(t.getClass());
        FieldValue fieldValue = new FieldValue(t);
        List<Object> objects = new ArrayList<>();
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!(column.isId() && column.isGeneratedKey())) {
                Object value = fieldValue.getValue(column.getType(), entry.getKey());
                objects.add(value);
            }
        }
        Object[] objArray = new Object[objects.size()];
        objects.toArray(objArray);
        return objArray;
    }
}

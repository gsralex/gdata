package com.gsralex.gdata.sqlstatement;

import com.gsralex.gdata.jdbc.JdbcGeneratedKey;
import com.gsralex.gdata.mapper.*;
import com.gsralex.gdata.result.DataRowSet;
import com.gsralex.gdata.utils.TypeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class SqlInsertStatement implements SqlStatement {

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


    public <T> void setIdValue(JdbcGeneratedKey generatedKey, T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        this.setIdValue(generatedKey, list);
    }


    public <T> void setIdValue(JdbcGeneratedKey generatedKey, List<T> list) {
        List<Object> keyList = new ArrayList<>();
        for (DataRowSet row : generatedKey.getDataSet().getRows()) {
            keyList.add(row.getObject(1));
        }
        setIdValue(keyList, list);
    }

    public <T> void setIdValue(Object key, T t) {
        List<Object> keyList = new ArrayList<>();
        keyList.add(key);
        List<T> list = new ArrayList<>();
        list.add(t);
        setIdValue(keyList, list);
    }

    public <T> void setIdValue(List<Object> keyList, List<T> list) {
        List<FieldColumn> columnList = getIdColumns(TypeUtils.getType(list));
        if (columnList != null && columnList.size() != 0) {
            int row = 0;
            for (T t : list) {
                FieldValue fieldValue = new FieldValue(t);
                for (FieldColumn column : columnList) {
                    Object value = keyList.get(row++);
                    fieldValue.setValue(column.getType(), column.getName(), value);
                }
            }
        }

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

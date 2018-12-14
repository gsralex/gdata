package com.gsralex.gdata.bean.sqlstatement;

import com.gsralex.gdata.bean.exception.DataException;
import com.gsralex.gdata.bean.exception.ExceptionMessage;
import com.gsralex.gdata.bean.mapper.FieldColumn;
import com.gsralex.gdata.bean.mapper.FieldValue;
import com.gsralex.gdata.bean.mapper.Mapper;
import com.gsralex.gdata.bean.mapper.MapperHolder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class SqlUpdateStatement implements SqlStatement {

    private String productName;

    public SqlUpdateStatement(DataSource dataSource) {
        productName = JdbcHelper.getProductName(dataSource);
    }

    @Override
    public boolean checkValid(Class type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        if (mapper.getIdColumns().size() == 0) {
            throw new DataException(ExceptionMessage.NOTID_FORUPDATE);
        }
        return true;
    }

    @Override
    public <T> String getSql(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        StringBuilder sql = new StringBuilder();
        String tableName = String.format(SqlAlias.getAliasFormat(productName), mapper.getTableName());

        sql.append(String.format("update %s ", tableName));
        sql.append("set ");
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                String label = String.format(SqlAlias.getAliasFormat(productName), column.getLabel());
                sql.append(String.format("%s=?,", label));
            }
        }
        sql = sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ");
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                sql.append(String.format(" `%s`=? and", column.getLabel()));
            }
        }
        return sql.delete(sql.length()-3,sql.length()).toString();
    }

    @Override
    public <T> Object[] getObjects(T t) {
        Mapper mapper = MapperHolder.getMapperCache(t.getClass());
        FieldValue fieldValue = new FieldValue(t);
        List<Object> objects = new ArrayList<>();

        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!column.isId()) {
                Object value = fieldValue.getValue(column.getType(), entry.getKey());
                objects.add(value);
            }
        }
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                Object value = fieldValue.getValue(column.getType(), entry.getKey());
                objects.add(value);
            }
        }
        Object[] objArray = new Object[objects.size()];
        objects.toArray(objArray);
        return objArray;
    }
}

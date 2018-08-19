package com.gsralex.gdata.bean.sqlstatement;

import com.gsralex.gdata.bean.jdbc.JdbcGeneratedKey;
import com.gsralex.gdata.bean.mapper.FieldColumn;
import com.gsralex.gdata.bean.mapper.FieldValue;
import com.gsralex.gdata.bean.mapper.Mapper;
import com.gsralex.gdata.bean.mapper.MapperHolder;
import com.gsralex.gdata.bean.result.DataRowSet;
import com.gsralex.gdata.bean.mapper.TypeUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class SqlInsertStatement implements SqlStatement {

    private String productName;

    public SqlInsertStatement(DataSource dataSource) {
        productName = JdbcHelper.getProductName(dataSource);
    }

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
                    Long longValue = (Long) keyList.get(row++);//key default long value
                    if (longValue != null) {
                        Object value = null;
                        if (column.getType() == Integer.class || column.getType() == int.class) {
                            value = longValue.intValue();
                        } else if (column.getType() == Short.class || column.getType() == short.class) {
                            value = longValue.shortValue();
                        } else if (column.getType() == Double.class || column.getType() == double.class) {
                            value = longValue.doubleValue();
                        } else if (column.getType() == Float.class || column.getType() == float.class) {
                            value = longValue.floatValue();
                        } else if (column.getType() == Byte.class || column.getType() == byte.class) {
                            value = longValue.byteValue();
                        } else if (column.getType() == Long.class || column.getType() == long.class) {
                            value = longValue;
                        }
                        fieldValue.setValue(column.getType(), column.getName(), value);
                    }
                }
            }
        }

    }

    public <T> List<FieldColumn> getIdColumns(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        return mapper.getIdColumns();

    }

    @Override
    public <T> boolean checkValid(Class<T> type) {
        return true;
    }

    @Override
    public <T> String getSql(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        StringBuilder sql = new StringBuilder();

        String tableName = String.format(SqlAlias.getAliasFormat(productName), mapper.getTableName());
        sql.append(String.format("insert into %s", tableName));
        sql.append("(");
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!(column.isId() && column.isGeneratedKey())) {
                String label = String.format(SqlAlias.getAliasFormat(productName), column.getLabel());
                sql.append(String.format("%s,", label));
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");

        sql.append(" values(");
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (!(column.isId() && column.isGeneratedKey())) {
                sql.append("?,");
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
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

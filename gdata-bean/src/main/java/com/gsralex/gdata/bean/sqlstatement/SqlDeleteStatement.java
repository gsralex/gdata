package com.gsralex.gdata.bean.sqlstatement;

import com.gsralex.gdata.bean.exception.DataException;
import com.gsralex.gdata.bean.exception.ExceptionMessage;
import com.gsralex.gdata.bean.mapper.FieldColumn;
import com.gsralex.gdata.bean.mapper.FieldValue;
import com.gsralex.gdata.bean.mapper.Mapper;
import com.gsralex.gdata.bean.mapper.MapperHolder;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author gsralex
 * @version 2018/3/28
 */
public class SqlDeleteStatement implements SqlStatement {

    private String productName;

    public SqlDeleteStatement(DataSource dataSource) {
        productName = JdbcHelper.getProductName(dataSource);
    }

    @Override
    public <T> boolean checkValid(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        if (mapper.getIdColumns().size() == 0) {
            throw new DataException(ExceptionMessage.NOTID_FORDELETE);
        }
        return true;
    }

    @Override
    public <T> String getSql(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        StringBuilder sql = new StringBuilder();

        String tableName = String.format(SqlAlias.getAliasFormat(productName), mapper.getTableName());
        sql.append(String.format("delete from %s ", tableName));
        sql.append("where");
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                String label = String.format(SqlAlias.getAliasFormat(productName), column.getLabel());
                sql.append(String.format(" %s=? and", label));
            }
        }
        return StringUtils.removeEnd(sql.toString(), "and");
    }

    @Override
    public <T> Object[] getObjects(T t) {
        List<Object> objects = new ArrayList<>();
        FieldValue fieldValue = new FieldValue(t);
        Mapper mapper = MapperHolder.getMapperCache(t.getClass());
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

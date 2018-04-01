package com.gsralex.gdata.sqlstatement;

import com.gsralex.gdata.exception.DataException;
import com.gsralex.gdata.exception.ExceptionMessage;
import com.gsralex.gdata.mapper.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author gsralex
 * @version 2018/3/28
 */
public class SqlDeleteStatement implements SqlStatement {

    @Override
    public <T> boolean checkValid(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        if (mapper.getFieldMapper().get(FieldEnum.Id).size() == 0) {
            throw new DataException(ExceptionMessage.NOTID_FORDELETE);
        }
        return true;
    }

    @Override
    public <T> String getSql(Class<T> type) {
        Mapper mapper = MapperHolder.getMapperCache(type);
        String sql = String.format("delete from `%s` ", mapper.getTableName());

        sql += "where";
        for (Map.Entry<String, FieldColumn> entry : mapper.getMapper().entrySet()) {
            FieldColumn column = entry.getValue();
            if (column.isId()) {
                sql += String.format(" `%s`=? and", column.getLabel());
            }
        }
        sql = StringUtils.removeEnd(sql, "and");
        return sql;
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

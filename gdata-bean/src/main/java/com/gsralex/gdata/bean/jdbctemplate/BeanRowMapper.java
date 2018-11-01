package com.gsralex.gdata.bean.jdbctemplate;

import com.gsralex.gdata.bean.mapper.MapperHelper;
import com.gsralex.gdata.bean.sqlstatement.JdbcHelper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class BeanRowMapper<T> implements RowMapper<T> {

    private Class<T> type;
    private MapperHelper mapperHelper;

    public BeanRowMapper(Class<T> type) {
        this.type = type;
        this.mapperHelper = new MapperHelper();
    }

    @Override
    public T mapRow(ResultSet resultSet, int i) throws SQLException {
        Set<String> columnSet = JdbcHelper.getColumnLabelSet(resultSet.getMetaData());
        return mapperHelper.mapperEntity(resultSet, columnSet, type);
    }
}

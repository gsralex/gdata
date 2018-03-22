package com.gsralex.gdata.jdbctemplate;

import com.gsralex.gdata.SqlMapperHelper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class BeanRowMapper<T> implements RowMapper<T> {

    private Class<T> type;
    private SqlMapperHelper sqlMapper;

    public BeanRowMapper(Class<T> type) {
        this.type = type;
        this.sqlMapper = new SqlMapperHelper();
    }

    @Override
    public T mapRow(ResultSet resultSet, int i) throws SQLException {
        return sqlMapper.mapperEntity(resultSet, type);
    }
}

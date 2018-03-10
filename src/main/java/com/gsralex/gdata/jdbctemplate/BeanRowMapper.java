package com.gsralex.gdata.jdbctemplate;

import com.gsralex.gdata.SqlRHelper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author gsralex
 * @date 2018/3/10
 */
public class BeanRowMapper<T> implements RowMapper<T> {

    private Class<T> type;
    private SqlRHelper sqlMapper;

    public BeanRowMapper(Class<T> type) {
        this.type = type;
        this.sqlMapper = new SqlRHelper();
    }

    @Override
    public T mapRow(ResultSet resultSet, int i) throws SQLException {
        return sqlMapper.mapperEntity(resultSet, type);
    }
}

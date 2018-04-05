package com.gsralex.gdata.bean.jdbctemplate;

import com.gsralex.gdata.bean.mapper.MapperHelper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        return mapperHelper.mapperEntity(resultSet, type);
    }
}

package com.gsralex.gdata.jdbctemplate;

import com.gsralex.gdata.*;
import com.gsralex.gdata.constant.JdbcConstants;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class JdbcTemplateUtils {


    private JdbcTemplate jdbcTemplate;
    private SqlInsertHelper insertHelper;
    private SqlUpdateHelper updateHelper;


    public JdbcTemplateUtils(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertHelper = new SqlInsertHelper();
        this.updateHelper = new SqlUpdateHelper();
    }


    public <T> boolean insert(T t) {
        return insert(t, false);
    }

    public <T> boolean insert(T t, boolean generatedKey) {
        if (t == null) {
            return false;
        }
        if (generatedKey) {
            return insertGeneratedKey(t);
        } else {
            return insertBean(t);
        }
    }

    private <T> boolean insertBean(T t) {
        String sql = insertHelper.getInsertSql(t.getClass());
        Object[] objects = insertHelper.getInsertObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    private <T> boolean insertGeneratedKey(T t) {
        String sql = insertHelper.getInsertSql(t.getClass());
        Object[] objects = insertHelper.getInsertObjects(t);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int r = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0, length = objects.length; i < length; i++) {
                ps.setObject(i + 1, objects[i]);
            }
            return ps;
        }, keyHolder);


        List<FieldColumn> columnList = insertHelper.getColumns(t.getClass(), FieldEnum.Id);
        if (columnList.size() != 0) {
            Object key = keyHolder.getKeyList().get(0).get(JdbcConstants.GENERATED_KEY);
            FieldValue fieldValue = new FieldValue(t);
            for (FieldColumn column : columnList) {
                fieldValue.setValue(column.getType(), column.getName(), key);
            }
        }
        return r != 0 ? true : false;
    }

    public <T> int batchInsert(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        String sql = insertHelper.getInsertSql(TypeUtils.getType(list));
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(insertHelper.getInsertObjects(item));
        }
        return JdbcHelper.getBatchResult(jdbcTemplate.batchUpdate(sql, argList));
    }


    public <T> boolean update(T t) {
        if (t == null) {
            return false;
        }
        String sql = updateHelper.getUpdateSql(t.getClass());
        Object[] objects = updateHelper.getUpdateObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    public <T> int batchUpdate(List<T> list) {
        if (list == null || list.size() == 0)
            return 0;
        Class type = TypeUtils.getType(list);
        String sql = updateHelper.getUpdateSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(updateHelper.getUpdateObjects(item));
        }
        return JdbcHelper.getBatchResult(jdbcTemplate.batchUpdate(sql, argList));
    }


    public <T> T get(String sql, Object[] args, Class<T> type) {
        List<T> list = jdbcTemplate.query(sql, args, new BeanRowMapper<>(type));
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> List<T> getList(String sql, Object[] args, Class<T> type) {
        return jdbcTemplate.query(sql, args, new BeanRowMapper<>(type));
    }

    public int update(String sql, Object[] args, int[] argTypes) {
        return jdbcTemplate.update(sql, args, argTypes);
    }

    public int update(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}

package com.gsralex.gdata.jdbctemplate;

import com.gsralex.gdata.sqlhelper.*;
import com.gsralex.gdata.result.DataRow;
import com.gsralex.gdata.result.DataSet;
import com.gsralex.gdata.result.DataRowImpl;
import com.gsralex.gdata.result.DataSetImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class JdbcTemplateUtils {


    private JdbcTemplate jdbcTemplate;
    private SqlInsertHelper insertHelper;
    private SqlUpdateHelper updateHelper;
    private SqlDeleteHelper deleteHelper;


    public JdbcTemplateUtils(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertHelper = new SqlInsertHelper();
        this.updateHelper = new SqlUpdateHelper();
        this.deleteHelper = new SqlDeleteHelper();
    }


    public <T> boolean insert(T t) {
        return insert(t, false);
    }

    public <T> boolean insert(T t, boolean generatedKey) {
        if (t == null) {
            return false;
        }
        if (generatedKey) {
            if (insertHelper.existsGenerateKey(t.getClass())) {
                return insertGeneratedKey(t);
            } else {
                return insertBean(t);
            }
        } else {
            return insertBean(t);
        }
    }

    private <T> boolean insertBean(T t) {
        Class type = t.getClass();
        String sql = insertHelper.getSql(t.getClass());
        Object[] objects = insertHelper.getObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    private <T> boolean insertGeneratedKey(T t) {
        Class type = t.getClass();
        String sql = insertHelper.getSql(type);
        Object[] objects = insertHelper.getObjects(t);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int r = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0, length = objects.length; i < length; i++) {
                ps.setObject(i + 1, objects[i]);
            }
            return ps;
        }, keyHolder);
        insertHelper.setIdValue(keyHolder.getKeyList().get(0).get("GENERATED_KEY"), t);
        return r != 0 ? true : false;
    }

    public  <T> int batchInsert(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Class type = TypeUtils.getType(list);
        String sql = insertHelper.getSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(insertHelper.getObjects(item));
        }
        return JdbcHelper.getBatchResult(jdbcTemplate.batchUpdate(sql, argList));
    }


    public <T> boolean update(T t) {
        if (t == null) {
            return false;
        }
        Class type = t.getClass();
        if (!updateHelper.checkValid(type)) {
            return false;
        }
        String sql = updateHelper.getSql(type);
        Object[] objects = updateHelper.getObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    public <T> int batchUpdate(List<T> list) {
        if (list == null || list.size() == 0)
            return 0;
        Class type = TypeUtils.getType(list);
        if (!updateHelper.checkValid(type)) {
            return 0;
        }
        String sql = updateHelper.getSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(updateHelper.getObjects(item));
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


    public DataSet queryForDataSet(String sql, Object... args) {
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, args);
        SqlRowSetMetaData rowSetMetaData = set.getMetaData();
        String[] labels = rowSetMetaData.getColumnNames();

        List<DataRow> itemList = new ArrayList<>();
        while (set.next()) {
            Map<String, Object> map = new HashMap<>();
            for (String label : labels) {
                map.put(label, set.getObject(label));
            }
            itemList.add(new DataRowImpl(labels, map));
        }
        return new DataSetImpl(itemList);
    }

    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        return jdbcTemplate.queryForList(sql, args);
    }

    public int update(String sql, Object[] args, int[] argTypes) {
        return jdbcTemplate.update(sql, args, argTypes);
    }

    public int update(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }


    public <T> boolean delete(T t) {
        if (t == null) {
            return false;
        }
        Class type = t.getClass();
        if (!deleteHelper.checkValid(type)) {
            return false;
        }
        String sql = deleteHelper.getSql(type);
        Object[] objects = deleteHelper.getObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    public <T> int batchDelete(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Class type = TypeUtils.getType(list);
        if (!deleteHelper.checkValid(type)) {
            return 0;
        }
        String sql = deleteHelper.getSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T t : list) {
            Object[] objects = deleteHelper.getObjects(t);
            argList.add(objects);
        }
        return JdbcHelper.getBatchResult(jdbcTemplate.batchUpdate(sql, argList));
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}

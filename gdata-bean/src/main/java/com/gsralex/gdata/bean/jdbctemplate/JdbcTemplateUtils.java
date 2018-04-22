package com.gsralex.gdata.bean.jdbctemplate;

import com.gsralex.gdata.bean.placeholder.BeanSource;
import com.gsralex.gdata.bean.placeholder.SqlObject;
import com.gsralex.gdata.bean.placeholder.ValueConverterImpl;
import com.gsralex.gdata.bean.result.DataRowSet;
import com.gsralex.gdata.bean.result.DataRowSetImpl;
import com.gsralex.gdata.bean.result.DataSet;
import com.gsralex.gdata.bean.result.DataSetImpl;
import com.gsralex.gdata.bean.sqlstatement.JdbcHelper;
import com.gsralex.gdata.bean.sqlstatement.SqlDeleteStatement;
import com.gsralex.gdata.bean.sqlstatement.SqlInsertStatement;
import com.gsralex.gdata.bean.sqlstatement.SqlUpdateStatement;
import com.gsralex.gdata.bean.mapper.TypeUtils;
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
    private SqlInsertStatement insertStatement;
    private SqlUpdateStatement updateStatement;
    private SqlDeleteStatement deleteStatement;


    public JdbcTemplateUtils(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertStatement = new SqlInsertStatement(dataSource);
        this.updateStatement = new SqlUpdateStatement(dataSource);
        this.deleteStatement = new SqlDeleteStatement(dataSource);
    }


    public <T> boolean insert(T t) {
        return insert(t, false);
    }

    public <T> boolean insert(T t, boolean generatedKey) {
        if (t == null) {
            return false;
        }
        if (generatedKey) {
            if (insertStatement.existsGenerateKey(t.getClass())) {
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
        String sql = insertStatement.getSql(t.getClass());
        Object[] objects = insertStatement.getObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    private <T> boolean insertGeneratedKey(T t) {
        Class type = t.getClass();
        String sql = insertStatement.getSql(type);
        Object[] objects = insertStatement.getObjects(t);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int r = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0, length = objects.length; i < length; i++) {
                ps.setObject(i + 1, objects[i]);
            }
            return ps;
        }, keyHolder);
        insertStatement.setIdValue(keyHolder.getKeyList().get(0).get("GENERATED_KEY"), t);
        return r != 0 ? true : false;
    }

    public <T> int batchInsert(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Class type = TypeUtils.getType(list);
        String sql = insertStatement.getSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(insertStatement.getObjects(item));
        }
        return JdbcHelper.getBatchResult(jdbcTemplate.batchUpdate(sql, argList));
    }


    public <T> boolean update(T t) {
        if (t == null) {
            return false;
        }
        Class type = t.getClass();
        if (!updateStatement.checkValid(type)) {
            return false;
        }
        String sql = updateStatement.getSql(type);
        Object[] objects = updateStatement.getObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    public <T> int batchUpdate(List<T> list) {
        if (list == null || list.size() == 0)
            return 0;
        Class type = TypeUtils.getType(list);
        if (!updateStatement.checkValid(type)) {
            return 0;
        }
        String sql = updateStatement.getSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(updateStatement.getObjects(item));
        }
        return JdbcHelper.getBatchResult(jdbcTemplate.batchUpdate(sql, argList));
    }


    public <T> T queryForObject(String sql, Object[] args, Class<T> type) {
        List<T> list = queryForList(sql, args, type);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> T queryForObjectP(String pSql, Map<String, Object> paramMap, Class<T> type) {
        List<T> list = queryForListP(pSql, paramMap, type);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> T queryForObjectP(String pSql, BeanSource beanSource, Class<T> type) {
        List<T> list = queryForListP(pSql, beanSource, type);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }


    public <T> List<T> queryForList(String sql, Object[] args, Class<T> type) {
        return jdbcTemplate.query(sql, args, new BeanRowMapper<>(type));
    }

    public <T> List<T> queryForListP(String pSql, Map<String, Object> paramMap, Class<T> type) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertMap(pSql, paramMap);
        return queryForList(sqlObject.getSql(), sqlObject.getObjects(), type);
    }

    public <T> List<T> queryForListP(String pSql, BeanSource beanSource, Class<T> type) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertBeanSource(pSql, beanSource);
        return queryForList(sqlObject.getSql(), sqlObject.getObjects(), type);
    }


    public DataSet queryForDataSet(String sql, Object... args) {
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, args);
        SqlRowSetMetaData rowSetMetaData = set.getMetaData();
        String[] labels = rowSetMetaData.getColumnNames();

        List<DataRowSet> itemList = new ArrayList<>();
        while (set.next()) {
            Map<String, Object> map = new HashMap<>();
            for (String label : labels) {
                map.put(label, set.getObject(label));
            }
            itemList.add(new DataRowSetImpl(labels, map));
        }
        return new DataSetImpl(itemList);
    }

    public DataSet queryForDataSetP(String pSql, Map<String, Object> paramMap) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertMap(pSql, paramMap);
        return queryForDataSet(sqlObject.getSql(), sqlObject.getObjects());
    }

    public DataSet queryForDataSetP(String pSql, BeanSource beanSource) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertBeanSource(pSql, beanSource);
        return queryForDataSet(sqlObject.getSql(), sqlObject.getObjects());
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
        if (!deleteStatement.checkValid(type)) {
            return false;
        }
        String sql = deleteStatement.getSql(type);
        Object[] objects = deleteStatement.getObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    public <T> int batchDelete(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Class type = TypeUtils.getType(list);
        if (!deleteStatement.checkValid(type)) {
            return 0;
        }
        String sql = deleteStatement.getSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T t : list) {
            Object[] objects = deleteStatement.getObjects(t);
            argList.add(objects);
        }
        return JdbcHelper.getBatchResult(jdbcTemplate.batchUpdate(sql, argList));
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
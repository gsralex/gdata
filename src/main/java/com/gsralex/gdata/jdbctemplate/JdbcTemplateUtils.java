package com.gsralex.gdata.jdbctemplate;

import com.gsralex.gdata.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 *         2018/3/10
 */
public class JdbcTemplateUtils {


    private static final String GENERATED_KEY = "GENERATED_KEY";

    private JdbcTemplate jdbcTemplate;
    private SqlCuHelper cuHelper;
    private SqlRHelper rHelper;


    public JdbcTemplateUtils(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.cuHelper=new SqlCuHelper();
        this.rHelper=new SqlRHelper();
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
        if (t == null) {
            return false;
        }
        String sql = cuHelper.getInsertSql(t.getClass());
        Object[] objects = cuHelper.getInsertObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    private <T> boolean insertGeneratedKey(T t) {
        String sql = cuHelper.getInsertSql(t.getClass());
        Object[] objects = cuHelper.getInsertObjects(t);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int r = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0, length = objects.length; i < length; i++) {
                ps.setObject(i + 1, objects[i]);
            }
            return ps;
        }, keyHolder);

        List<FieldColumn> columnList = cuHelper.getColumns(t.getClass(), FieldEnum.Id);
        if (columnList.size() != 0) {
            Map<String, Object> keyMap = keyHolder.getKeyList().get(0);
            ModelMap modelMap = ModelMapper.getMapperCache(t.getClass());
            FieldValue fieldValue = new FieldValue(t, modelMap);
            for (FieldColumn column : columnList) {
                Object key = keyMap.get(GENERATED_KEY);
                fieldValue.setValue(column.getName(), key);
            }
        }
        return r != 0 ? true : false;
    }

    public <T> int batchInsert(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        String sql = cuHelper.getInsertSql(TypeUtils.getType(list));
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(cuHelper.getInsertObjects(item));
        }
        return getOkResult(jdbcTemplate.batchUpdate(sql, argList));
    }

    private static int getOkResult(int[] r) {
        int cnt = 0;
        for (int item : r) {
            if (item != Statement.EXECUTE_FAILED) {
                cnt++;
            }
        }
        return cnt;
    }

    public <T> boolean update(T t) {
        if (t == null) {
            return false;
        }
        SqlCuHelper modelUtils = new SqlCuHelper();
        String sql = modelUtils.getUpdateSql(t.getClass());
        Object[] objects = modelUtils.getUpdateObjects(t);
        return jdbcTemplate.update(sql, objects) != 0 ? true : false;
    }

    public <T> int batchUpdate(List<T> list) {
        if (list == null || list.size() == 0)
            return 0;
        SqlCuHelper modelUtils = new SqlCuHelper();
        Class<T> type = (Class<T>) list.get(0).getClass();
        String sql = modelUtils.getUpdateSql(type);
        List<Object[]> argList = new ArrayList<>();
        for (T item : list) {
            argList.add(modelUtils.getUpdateObjects(item));
        }
        return getOkResult(jdbcTemplate.batchUpdate(sql, argList));
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


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}

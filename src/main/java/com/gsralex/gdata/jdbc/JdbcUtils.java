package com.gsralex.gdata.jdbc;


import com.gsralex.gdata.constant.JdbcConstants;
import com.gsralex.gdata.mapper.FieldColumn;
import com.gsralex.gdata.mapper.FieldValue;
import com.gsralex.gdata.mapper.MapperHelper;
import com.gsralex.gdata.sqlhelper.*;
import com.gsralex.gdata.result.MemRow;
import com.gsralex.gdata.result.MemSet;
import com.gsralex.gdata.result.MemRowImpl;
import com.gsralex.gdata.result.MemSetImpl;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class JdbcUtils {

    private static Logger LOGGER = Logger.getLogger(JdbcUtils.class);

    private DataSource dataSource;
    private SqlInsertHelper insertHelper;
    private SqlUpdateHelper updateHelper;
    private MapperHelper mapperHelper;
    private SqlDeleteHelper deleteHelper;


    public JdbcUtils(DataSource dataSource) {
        this.dataSource = dataSource;
        this.insertHelper = new SqlInsertHelper();
        this.updateHelper = new SqlUpdateHelper();
        this.mapperHelper = new MapperHelper();
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
        if (t == null) {
            return false;
        }
        Object[] objects = insertHelper.getObjects(t);
        String sql = insertHelper.getSql(t.getClass());
        return executeUpdate(sql, objects) != 0 ? true : false;
    }

    private <T> boolean insertGeneratedKey(T t) {
        Class type = t.getClass();
        String sql = insertHelper.getSql(type);
        Object[] objects = insertHelper.getObjects(t);
        JdbcGeneratedKey generatedKey = executeUpdateGenerateKey(sql, objects);
        if (generatedKey != null) {
            List<FieldColumn> columnList = insertHelper.getIdColumns(type);
            if (columnList.size() != 0) {
                FieldValue fieldValue = new FieldValue(t);
                Object key = generatedKey.getKeyList().get(0).get(JdbcConstants.GENERATED_KEY);
                for (FieldColumn column : columnList) {
                    fieldValue.setValue(column.getType(), column.getName(), key);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public <T> int batchInsert(List<T> list) {
        return batchInsert(list, false);
    }

    public <T> int batchInsert(List<T> list, boolean generatedKey) {
        if (list == null || list.size() == 0)
            return 0;
        if (generatedKey) {
            Class type = TypeUtils.getType(list);
            if (insertHelper.existsGenerateKey(type)) {
                return batchInsertGeneratedKey(list);
            } else {
                return batchInsertBean(list);
            }
        } else {
            return batchInsertBean(list);
        }
    }

    private <T> int batchInsertBean(List<T> list) {
        String sql = insertHelper.getSql(TypeUtils.getType(list));
        List<Object[]> objectList = new ArrayList<>();
        for (T t : list) {
            objectList.add(insertHelper.getObjects(t));
        }
        return executeBatch(sql, objectList);
    }

    private <T> int batchInsertGeneratedKey(List<T> list) {
        Class type = TypeUtils.getType(list);
        String sql = insertHelper.getSql(type);
        List<Object[]> objectList = new ArrayList<>();
        for (T t : list) {
            objectList.add(insertHelper.getObjects(t));
        }
        JdbcGeneratedKey generatedKey = executeBatchGeneratedKey(sql, objectList);
        if (generatedKey != null) {
            int i = 0;
            for (T t : list) {
                FieldValue fieldValue = new FieldValue(t);
                List<FieldColumn> columnList = insertHelper.getIdColumns(type);
                for (FieldColumn column : columnList) {
                    Object value = generatedKey.get(i++).get(JdbcConstants.GENERATED_KEY);
                    fieldValue.setValue(column.getType(), column.getName(), value);
                }
            }
            return generatedKey.getR();
        }
        return 0;
    }


    public JdbcGeneratedKey executeUpdateGenerateKey(String sql, Object[] objects) {
        PreparedStatement ps = null;
        try {
            ps = pre(sql, objects, true);
            int r = ps.executeUpdate();
            if (r != 0) {
                return getGeneratedKey(ps.getGeneratedKeys(), r);
            }
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.executeUpdate", e);
        } finally {
            close(ps);
        }
        return null;
    }

    public JdbcGeneratedKey getGeneratedKey(ResultSet rs, int r) throws SQLException {
        if (rs != null) {
            try {
                JdbcGeneratedKey generatedKey = new JdbcGeneratedKey(r);
                int columnCount = rs.getMetaData().getColumnCount();
                if (columnCount != 0) {
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();
                        generatedKey.getKeyList().add(map);
                        for (int i = 0; i < columnCount; i++) {
                            String label = rs.getMetaData().getColumnLabel(i + 1);
                            map.put(label, rs.getObject(label));
                        }
                    }
                }
                return generatedKey;
            } finally {
                closeRs(rs);
            }
        }
        return null;
    }


    public JdbcGeneratedKey executeBatchGeneratedKey(String sql, List<Object[]> objects) {
        PreparedStatement ps = null;
        try {
            ps = preBatch(sql, objects, true);
            int[] r = ps.executeBatch();
            ps.getConnection().commit();
            if (r.length != 0) {
                return getGeneratedKey(ps.getGeneratedKeys(), JdbcHelper.getBatchResult(r));
            }
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.executeUpdate", e);
        } finally {
            close(ps);
        }
        return null;
    }


    public <T> boolean update(T t) {
        if (t == null)
            return false;
        Class type = t.getClass();
        if (!updateHelper.checkValid(type)) {
            return false;
        }
        String sql = updateHelper.getSql(type);
        Object[] objects = updateHelper.getObjects(t);
        return executeUpdate(sql, objects) != 0 ? true : false;
    }

    public <T> int batchUpdate(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Class<T> type = (Class<T>) list.get(0).getClass();
        if (!updateHelper.checkValid(type)) {
            return 0;
        }
        String sql = updateHelper.getSql(type);
        List<Object[]> objectList = new ArrayList<>();
        for (T t : list) {
            objectList.add(updateHelper.getObjects(t));
        }
        return executeBatch(sql, objectList);
    }


    public int executeUpdate(String sql, Object[] objects) {
        PreparedStatement ps = null;
        try {
            ps = pre(sql, objects);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.executeUpdate", e);
            return 0;
        } finally {
            close(ps);
        }
    }

    public <T> T get(String sql, Object[] objects, Class<T> type) {
        List<T> list = getList(sql, objects, type);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> List<T> getList(String sql, Object[] objects, Class<T> type) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = pre(sql, objects);
            rs = ps.executeQuery();
            return mapperList(rs, type);
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.getList", e);
            return null;
        } finally {
            closeRs(rs);
            close(ps);
        }
    }

    public MemSet queryForMemSet(String sql, Object... objects) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MemRow> itemList = new ArrayList<>();
        try {
            ps = pre(sql, objects);
            rs = ps.executeQuery();
            String[] labels = JdbcHelper.getColumnLabels(rs.getMetaData());
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (String label : labels) {
                    map.put(label, rs.getObject(label));
                }
                MemRow memRow = new MemRowImpl(labels, map);
                itemList.add(memRow);
            }
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.queryForList", e);
        } finally {
            closeRs(rs);
            close(ps);
        }
        return new MemSetImpl(itemList);
    }


    public List<Map<String, Object>> queryForList(String sql, Object... objects) {
        MemSet memSet = queryForMemSet(sql, objects);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (MemRow memRow : memSet.getRows()) {
            mapList.add(memRow.getMap());
        }
        return mapList;
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
        return executeUpdate(sql, objects) != 0 ? true : false;
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
        return executeBatch(sql, argList);
    }

    public int executeBatch(String sql, List<Object[]> objectList) {
        PreparedStatement ps = null;
        try {
            ps = preBatch(sql, objectList, false);
            int[] r = ps.executeBatch();
            ps.getConnection().commit();
            return JdbcHelper.getBatchResult(r);
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.executeBatch", e);
            return 0;
        } finally {
            close(ps);
        }
    }

    private <T> List<T> mapperList(ResultSet rs, Class<T> type) {
        List<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(mapperHelper.mapperEntity(rs, type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    private PreparedStatement pre(String sql, Object[] objects) {
        return pre(sql, objects, false);
    }

    private PreparedStatement pre(String sql, Object[] objects, boolean autoGeneratedKeys) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps;
            if (autoGeneratedKeys) {
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(sql);
            }
            if (objects != null && objects.length != 0) {
                for (int i = 0; i < objects.length; i++) {
                    ps.setObject(i + 1, objects[i]);
                }
            }
            return ps;
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.pre", e);
            return null;
        }
    }

    private PreparedStatement preBatch(String sql, List<Object[]> objectsList, boolean autoGeneratedKeys) {
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = null;
            if (autoGeneratedKeys) {
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            for (Object[] objects : objectsList) {
                for (int i = 0, size = objects.length; i < size; i++) {
                    ps.setObject(i + 1, objects[i]);
                }
                ps.addBatch();
            }
            return ps;
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.preBatch", e);
            return null;
        }
    }

    private Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.getConnection", e);
            return null;
        }
    }

    private void close(PreparedStatement ps) {

        try {
            Connection conn = ps.getConnection();
            closePs(ps);
            closeConn(conn);
        } catch (SQLException e) {
            LOGGER.error("JdbcUtils.close", e);
        }
    }

    private void closePs(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                LOGGER.error("JdbcUtils.closePs", e);
            }
        }
    }

    private void closeRs(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error("JdbcUtils.closeRs", e);
            }
        }
    }

    private void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("JdbcUtils.closeConn", e);
            }
        }
    }
}

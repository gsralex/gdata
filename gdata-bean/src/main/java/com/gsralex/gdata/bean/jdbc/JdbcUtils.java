package com.gsralex.gdata.bean.jdbc;


import com.gsralex.gdata.bean.exception.DataException;
import com.gsralex.gdata.bean.placeholder.BeanSource;
import com.gsralex.gdata.bean.placeholder.SqlObject;
import com.gsralex.gdata.bean.placeholder.ValueConverterImpl;
import com.gsralex.gdata.bean.result.DataRowSet;
import com.gsralex.gdata.bean.result.DataSet;
import com.gsralex.gdata.bean.result.DataSetUtils;
import com.gsralex.gdata.bean.sqlstatement.JdbcHelper;
import com.gsralex.gdata.bean.mapper.MapperHelper;
import com.gsralex.gdata.bean.sqlstatement.SqlDeleteStatement;
import com.gsralex.gdata.bean.sqlstatement.SqlInsertStatement;
import com.gsralex.gdata.bean.sqlstatement.SqlUpdateStatement;
import com.gsralex.gdata.bean.mapper.TypeUtils;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gsralex
 * @version 2018/3/10
 */
public class JdbcUtils {

    private static Logger LOGGER = Logger.getLogger(JdbcUtils.class);

    private DataSource dataSource;
    private SqlInsertStatement insertStatement;
    private SqlUpdateStatement updateStatement;
    private MapperHelper mapperHelper;
    private SqlDeleteStatement deleteStatement;


    public JdbcUtils(DataSource dataSource) {
        this.dataSource = dataSource;
        this.insertStatement = new SqlInsertStatement(dataSource);
        this.updateStatement = new SqlUpdateStatement(dataSource);
        this.mapperHelper = new MapperHelper();
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

    public JdbcGeneratedKey insert(String sql, Object[] objects) {
        return executeUpdateGenerateKey(sql, objects);
    }


    private <T> boolean insertBean(T t) {
        if (t == null) {
            return false;
        }
        Object[] objects = insertStatement.getObjects(t);
        String sql = insertStatement.getSql(t.getClass());
        return executeUpdate(sql, objects) != 0 ? true : false;
    }

    private <T> boolean insertGeneratedKey(T t) {
        Class type = t.getClass();
        String sql = insertStatement.getSql(type);
        Object[] objects = insertStatement.getObjects(t);
        JdbcGeneratedKey generatedKey = executeUpdateGenerateKey(sql, objects);
        insertStatement.setIdValue(generatedKey, t);
        return generatedKey.getResult() != 0 ? true : false;
    }


    public <T> int batchInsert(List<T> list) {
        return batchInsert(list, false);
    }

    public <T> int batchInsert(List<T> list, boolean generatedKey) {
        if (list == null || list.size() == 0)
            return 0;
        if (generatedKey) {
            Class type = TypeUtils.getType(list);
            if (insertStatement.existsGenerateKey(type)) {
                return batchInsertGeneratedKey(list);
            } else {
                return batchInsertBean(list);
            }
        } else {
            return batchInsertBean(list);
        }
    }

    private <T> int batchInsertBean(List<T> list) {
        String sql = insertStatement.getSql(TypeUtils.getType(list));
        List<Object[]> objectList = new ArrayList<>();
        for (T t : list) {
            objectList.add(insertStatement.getObjects(t));
        }
        return executeBatch(sql, objectList);
    }

    private <T> int batchInsertGeneratedKey(List<T> list) {
        Class type = TypeUtils.getType(list);
        String sql = insertStatement.getSql(type);
        List<Object[]> objectList = new ArrayList<>();
        for (T t : list) {
            objectList.add(insertStatement.getObjects(t));
        }
        JdbcGeneratedKey generatedKey = executeBatchGeneratedKey(sql, objectList);
        insertStatement.setIdValue(generatedKey, list);
        return generatedKey.getResult();
    }


    public JdbcGeneratedKey executeUpdateGenerateKey(String sql, Object[] objects) {
        return executeUpdate(sql, objects, true);
    }


    public JdbcGeneratedKey executeBatchGeneratedKey(String sql, List<Object[]> objects) {
        return executeBatch(sql, objects, true);
    }


    public <T> boolean update(T t) {
        if (t == null)
            return false;
        Class type = t.getClass();
        if (!updateStatement.checkValid(type)) {
            return false;
        }
        String sql = updateStatement.getSql(type);
        Object[] objects = updateStatement.getObjects(t);
        return executeUpdate(sql, objects) != 0 ? true : false;
    }

    public <T> int batchUpdate(List<T> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Class<T> type = (Class<T>) list.get(0).getClass();
        if (!updateStatement.checkValid(type)) {
            return 0;
        }
        String sql = updateStatement.getSql(type);
        List<Object[]> objectList = new ArrayList<>();
        for (T t : list) {
            objectList.add(updateStatement.getObjects(t));
        }
        return executeBatch(sql, objectList);
    }

    public int executeBatch(String sql, List<Object[]> objects) {
        return executeBatch(sql, objects, false).getResult();
    }

    public int executeUpdate(String sql, Object[] objects) {
        return executeUpdate(sql, objects, false).getResult();
    }

    public int executeUpdatePh(String pSql, Map<String, Object> paramMap) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertMap(pSql, paramMap);
        return executeUpdate(sqlObject.getSql(), sqlObject.getObjects());
    }

    public int executeUpdatePh(String pSql, BeanSource beanSource) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertBeanSource(pSql, beanSource);
        return executeUpdate(sqlObject.getSql(), sqlObject.getObjects());
    }

    public <T> T queryForObject(String sql, Object[] objects, Class<T> type) {
        List<T> list = queryForList(sql, objects, type);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> T queryForObjectPh(String pSql, BeanSource beanSource, Class<T> type) {
        List<T> list = queryForListPh(pSql, beanSource, type);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> T queryForObjectPh(String pSql, Map<String, Object> paramMap, Class<T> type) {
        List<T> list = queryForListPh(pSql, paramMap, type);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> List<T> queryForList(String sql, Object[] objects, Class<T> type) {
        List<T> list = new ArrayList<>();
        executeQuery(sql, objects, new ResultCallback() {
            @Override
            public void mapper(ResultSet rs) {
                try {
                    Set<String> columnSet = JdbcHelper.getColumnLabelSet(rs.getMetaData());
                    while (rs.next()) {
                        list.add(mapperHelper.mapperEntity(rs, columnSet, type));
                    }
                } catch (Exception e) {
                    throw new DataException("queryForList", e);
                }
            }
        });
        return list;
    }

    public <T> List<T> queryForListPh(String pSql, Map<String, Object> paramMap, Class<T> type) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertMap(pSql, paramMap);
        return queryForList(sqlObject.getSql(), sqlObject.getObjects(), type);
    }

    public <T> List<T> queryForListPh(String pSql, BeanSource beanSource, Class<T> type) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertBeanSource(pSql, beanSource);
        return queryForList(sqlObject.getSql(), sqlObject.getObjects(), type);
    }

    public DataSet queryForDataSet(String sql, Object... objects) {
        final DataSet[] dataSet = {null};
        executeQuery(sql, objects, new ResultCallback() {
            @Override
            public void mapper(ResultSet rs) {
                try {
                    dataSet[0] = DataSetUtils.getDataSet(rs, false);
                } catch (SQLException e) {
                }
            }
        });
        return dataSet[0];
    }

    public DataSet queryForDataSetPh(String pSql, Map<String, Object> paramMap) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertMap(pSql, paramMap);
        return queryForDataSet(sqlObject.getSql(), sqlObject.getObjects());
    }

    public DataSet queryForDatasetPh(String pSql, BeanSource beanSource) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertBeanSource(pSql, beanSource);
        return queryForDataSet(sqlObject.getSql(), sqlObject.getObjects());
    }

    public List<Map<String, Object>> queryForList(String sql, Object... objects) {
        DataSet dataSet = queryForDataSet(sql, objects);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (DataRowSet dataRowSet : dataSet.getRows()) {
            mapList.add(dataRowSet.getMap());
        }
        return mapList;
    }

    public List<Map<String, Object>> queryForListPh(String pSql, Map<String, Object> paramMap) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertMap(pSql, paramMap);
        return queryForList(sqlObject.getSql(), sqlObject.getObjects());
    }

    public List<Map<String, Object>> queryForListPh(String pSql, BeanSource beanSource) {
        SqlObject sqlObject = ValueConverterImpl.getInstance().convertBeanSource(pSql, beanSource);
        return queryForList(sqlObject.getSql(), sqlObject.getObjects());
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
        return executeUpdate(sql, objects) != 0 ? true : false;
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
        return executeBatch(sql, argList);
    }


    //事务支持

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        Connection connection = JdbcConnHolder.getConnection(this.dataSource);
        connection.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        Connection connection = JdbcConnHolder.getConnection(this.dataSource);
        return connection.getAutoCommit();
    }

    public void commit() throws SQLException {
        Connection connection = JdbcConnHolder.getConnection(this.dataSource);
        try {
            connection.commit();
        } finally {
            JdbcConnHolder.closeConnection();
        }
    }

    public void rollback() throws SQLException {
        Connection connection = JdbcConnHolder.getConnection(this.dataSource);
        connection.rollback();
    }


    private void executeQuery(String sql, Object[] objects, ResultCallback resultCallback) {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                Connection conn = JdbcConnHolder.getConnection(this.dataSource);
                ps = conn.prepareStatement(sql);
                if (objects != null) {
                    for (int i = 0; i < objects.length; i++) {
                        ps.setObject(i + 1, objects[i]);
                    }
                }
                rs = ps.executeQuery();
                if (resultCallback != null) {
                    resultCallback.mapper(rs);
                }
            } finally {
                ResultUtils.closeResultSet(rs);
                PreparedStatementUtils.clearStatement(ps);
                if (getAutoCommit()) {
                    JdbcConnHolder.closeConnection();
                }
            }
        } catch (SQLException e) {
            throw new DataException("executeQuery", e);
        }
    }

    private JdbcGeneratedKey executeUpdate(String sql, Object[] objects, boolean autoGeneratedKey) {
        try {
            PreparedStatement ps = null;
            try {
                Connection conn = JdbcConnHolder.getConnection(this.dataSource);
                if (autoGeneratedKey) {
                    ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                } else {
                    ps = conn.prepareStatement(sql);
                }
                if (objects != null) {
                    for (int i = 0; i < objects.length; i++) {
                        ps.setObject(i + 1, objects[i]);
                    }
                }
                int r = ps.executeUpdate();
                DataSet dataSet = null;
                if (r != 0) {
                    if (autoGeneratedKey) {
                        dataSet = DataSetUtils.getDataSet(ps.getGeneratedKeys(), true);
                    }
                }
                return new JdbcGeneratedKey(r, dataSet);
            } finally {
                PreparedStatementUtils.clearStatement(ps);
                if (getAutoCommit()) {
                    JdbcConnHolder.closeConnection();
                }
            }
        } catch (SQLException e) {
            throw new DataException("executeUpdate", e);
        }
    }

    private JdbcGeneratedKey executeBatch(String sql, List<Object[]> objectsList, boolean autoGeneratedKeys) {
        try {
            PreparedStatement ps = null;
            try {
                Connection conn = JdbcConnHolder.getConnection(this.dataSource);
                conn.setAutoCommit(false);
                if (autoGeneratedKeys) {
                    ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                } else {
                    ps = conn.prepareStatement(sql);
                }
                if (objectsList != null) {
                    for (Object[] objects : objectsList) {
                        if (objects != null) {
                            for (int i = 0, size = objects.length; i < size; i++) {
                                ps.setObject(i + 1, objects[i]);
                            }
                            ps.addBatch();
                        }
                    }
                }
                int[] r = ps.executeBatch();
                ps.getConnection().commit();
                int result = JdbcHelper.getBatchResult(r);
                DataSet dataSet = null;
                if (autoGeneratedKeys) {
                    dataSet = DataSetUtils.getDataSet(ps.getGeneratedKeys(), true);
                }
                return new JdbcGeneratedKey(result, dataSet);
            } finally {
                PreparedStatementUtils.clearBatchStatemt(ps);
                if (getAutoCommit()) {
                    JdbcConnHolder.closeConnection();
                }
            }
        } catch (SQLException e) {
            throw new DataException("executeBatch", e);
        }
    }
}

package com.gsralex.gdata.bean.jdbc;

import com.gsralex.gdata.bean.exception.DataException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author gsralex
 * @version 2018/3/30
 */
public class JdbcConnHolder {

    private static final ThreadLocal<Connection> connPool = new ThreadLocal<>();

    public static Connection getConnection(DataSource dataSource) throws SQLException {
        Connection conn = connPool.get();
        if (conn == null) {
            conn = dataSource.getConnection();
            connPool.set(conn);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        Connection conn = connPool.get();
        if (conn != null && !conn.isClosed()) {
            conn.close();
            connPool.set(null);
        }
    }
}

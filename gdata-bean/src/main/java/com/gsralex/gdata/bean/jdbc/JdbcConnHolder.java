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

    public static Connection getConnection(DataSource dataSource) {
        Connection conn = connPool.get();
        if (conn == null) {
            try {
                conn = dataSource.getConnection();
                connPool.set(conn);
            } catch (SQLException e) {
                throw new DataException("getConnection", e);
            }
        }
        return conn;
    }

    public static void closeConnection() {
        Connection conn = connPool.get();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                connPool.set(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

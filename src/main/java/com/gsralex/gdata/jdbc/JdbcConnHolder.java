package com.gsralex.gdata.jdbc;

import com.gsralex.gdata.exception.DataException;
import org.springframework.core.NamedThreadLocal;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author gsralex
 * @version 2018/3/30
 */
public class JdbcConnHolder {

    private static final ThreadLocal<Connection> connPool = new NamedThreadLocal<>("CONN");

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

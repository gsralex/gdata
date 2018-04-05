package com.gsralex.gdata.bean.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author gsralex
 * @version 2018/4/1
 */
public class PreparedStatementUtils {

    public static void clearStatement(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.clearParameters();
                ps.close();
            }
        } catch (SQLException e) {

        }
    }

    public static void clearBatchStatemt(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.clearParameters();
                ps.clearBatch();
                ps.close();
            }
        } catch (SQLException e) {

        }
    }
}

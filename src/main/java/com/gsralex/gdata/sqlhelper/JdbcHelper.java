package com.gsralex.gdata.sqlhelper;

import com.gsralex.gdata.jdbc.JdbcGeneratedKey;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class JdbcHelper {

    public static void closePsConn(PreparedStatement ps) {
        try {
            Connection conn = ps.getConnection();
            closePs(ps);
            closeConn(conn);
        } catch (SQLException e) {
        }
    }

    public static void closePs(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void closeRs(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public static String[] getColumnLabels(ResultSetMetaData metaData) throws SQLException {
        int columnCount = metaData.getColumnCount();

        List<String> labelList = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            labelList.add(metaData.getColumnLabel(i + 1));
        }
        String[] labels = new String[labelList.size()];
        labelList.toArray(labels);
        return labels;
    }


    public static int getBatchResult(int[] r) {
        int cnt = 0;
        for (int item : r) {
            if (item != Statement.EXECUTE_FAILED) {
                cnt++;
            }
        }
        return cnt;
    }

}

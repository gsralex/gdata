package com.gsralex.gdata.bean.sqlstatement;

import com.gsralex.gdata.bean.exception.DataException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class JdbcHelper {

    public static Set<String> getColumnLabelSet(ResultSetMetaData metaData) throws SQLException {
        Set<String> labelSet = new HashSet<>();
        String[] labels = getColumnLabels(metaData);
        for (String label : labels) {
            labelSet.add(label);
        }
        return labelSet;
    }

    public static String[] getColumnLabels(ResultSetMetaData metaData) throws SQLException {
        int columnCount = metaData.getColumnCount();

        List<String> labelList = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            String label = metaData.getColumnLabel(i + 1).toLowerCase();
            labelList.add(label);
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


    public static String getProductName(DataSource dataSource) {
        try {
            return dataSource.getConnection().getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new DataException("getProductName", e);
        }
    }

}

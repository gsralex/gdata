package com.gsralex.gdata.sqlstatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gsralex
 * @version 2018/3/15
 */
public class JdbcHelper {

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

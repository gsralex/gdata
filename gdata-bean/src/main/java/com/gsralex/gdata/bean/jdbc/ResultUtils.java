package com.gsralex.gdata.bean.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author gsralex
 * @version 2018/4/1
 */
public class ResultUtils {

    public static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                if (!resultSet.isClosed()) {
                    resultSet.close();
                }
            }
        } catch (SQLException e) {
        }
    }
}

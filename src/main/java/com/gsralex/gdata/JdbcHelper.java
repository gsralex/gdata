package com.gsralex.gdata;

import java.sql.Statement;

/**
 * @author gsralex
 *         date: 2018/3/15
 */
public class JdbcHelper {

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

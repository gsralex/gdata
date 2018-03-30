package com.gsralex.gdata.result;

import com.gsralex.gdata.sqlhelper.JdbcHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/29
 */
public class DataSetUtils {

    public static DataSet getDataSet(ResultSet rs, boolean autoCloseResultSet) throws SQLException {
        try {
            String[] labels = JdbcHelper.getColumnLabels(rs.getMetaData());
            List<DataRow> dataRows = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (String label : labels) {
                    map.put(label, rs.getObject(label));
                }
                DataRow dataRow = new DataRowImpl(labels, map);
                dataRows.add(dataRow);
            }
            return new DataSetImpl(dataRows);
        } finally {
            if (autoCloseResultSet) {
                JdbcHelper.closeRs(rs);
            }
        }
    }
}

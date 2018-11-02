package com.gsralex.gdata.bean.result;

import com.gsralex.gdata.bean.sqlstatement.JdbcHelper;

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
        if (rs == null) {
            return new DataSetImpl();
        }
        try {
            String[] labels = JdbcHelper.getColumnLabels(rs.getMetaData());
            List<DataRowSet> dataRowSets = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (String label : labels) {
                    map.put(label, rs.getObject(label));
                }
                DataRowSet dataRowSet = new DataRowSetImpl(labels, map);
                dataRowSets.add(dataRowSet);
            }
            return new DataSetImpl(dataRowSets);
        } finally {
            if (autoCloseResultSet) {
                if (!rs.isClosed())
                    rs.close();
            }
        }
    }
}

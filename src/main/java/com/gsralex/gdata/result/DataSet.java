package com.gsralex.gdata.result;

import java.util.List;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public interface DataSet {

    DataRowSet get(int row);

    List<DataRowSet> getRows();
}

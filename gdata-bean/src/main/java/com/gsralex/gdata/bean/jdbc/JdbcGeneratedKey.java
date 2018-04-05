package com.gsralex.gdata.bean.jdbc;

import com.gsralex.gdata.bean.result.DataSet;

/**
 * @author gsralex
 * @version 2018/3/29
 */
public class JdbcGeneratedKey {

    private int result;
    private DataSet dataSet;

    public JdbcGeneratedKey(int result) {
        this.result = result;
    }

    public JdbcGeneratedKey(int result, DataSet dataSet) {
        this.result = result;
        this.dataSet = dataSet;
    }


    public int getResult() {
        return result;
    }

    public DataSet getDataSet() {
        return dataSet;
    }
}

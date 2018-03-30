package com.gsralex.gdata.result;


import java.util.ArrayList;
import java.util.List;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public class DataSetImpl implements DataSet {


    private List<DataRow> dataRows = new ArrayList<>();

    public DataSetImpl(List<DataRow> dataRows) {
        this.dataRows = dataRows;
    }

    @Override
    public DataRow get(int row) {
        return dataRows.get(row);
    }

    @Override
    public List<DataRow> getRows() {
        return dataRows;
    }
}

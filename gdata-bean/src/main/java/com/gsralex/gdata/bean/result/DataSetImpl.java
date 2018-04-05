package com.gsralex.gdata.bean.result;


import java.util.ArrayList;
import java.util.List;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public class DataSetImpl implements DataSet {


    private List<DataRowSet> dataRowSets;

    public DataSetImpl() {
        this.dataRowSets = new ArrayList<>();
    }

    public DataSetImpl(List<DataRowSet> dataRowSets) {
        this.dataRowSets = dataRowSets;
    }

    @Override
    public DataRowSet get(int row) {
        return dataRowSets.get(row);
    }

    @Override
    public List<DataRowSet> getRows() {
        return dataRowSets;
    }
}

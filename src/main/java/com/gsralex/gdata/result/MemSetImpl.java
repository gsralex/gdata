package com.gsralex.gdata.result;


import java.util.ArrayList;
import java.util.List;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public class MemSetImpl implements MemSet {


    private List<MemRow> memRows = new ArrayList<>();

    public MemSetImpl(List<MemRow> memRows) {
        this.memRows = memRows;
    }

    @Override
    public MemRow get(int row) {
        return memRows.get(row);
    }

    @Override
    public List<MemRow> getRows() {
        return memRows;
    }
}

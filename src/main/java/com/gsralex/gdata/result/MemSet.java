package com.gsralex.gdata.result;

import java.util.List;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public interface MemSet {

    MemRow get(int row);

    List<MemRow> getRows();
}

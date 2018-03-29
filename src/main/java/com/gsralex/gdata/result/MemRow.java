package com.gsralex.gdata.result;


import com.gsralex.gdata.exception.DataException;

import java.util.Date;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public interface MemRow {

    Map<String, Object> getMap();

    Object getObject(int column) throws DataException;

    Object getObject(String label) throws DataException;

    Integer getInt(int column) throws DataException;

    Integer getInt(String label) throws DataException;

    Double getDouble(int column) throws DataException;

    Double getDouble(String label) throws DataException;

    Long getLong(int column) throws DataException;

    Long getLong(String label) throws DataException;

    String getString(int column) throws DataException;

    String getString(String label) throws DataException;

    Short getShort(int column) throws DataException;

    Short getShort(String label) throws DataException;

    Boolean getBoolean(int column) throws DataException;

    Boolean getBoolean(String label) throws DataException;

    Date getDate(int column) throws DataException;

    Date getDate(String label) throws DataException;

    Byte getByte(int column) throws DataException;

    Byte getByte(String label) throws DataException;
}

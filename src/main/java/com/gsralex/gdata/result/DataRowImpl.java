package com.gsralex.gdata.result;

import com.gsralex.gdata.exception.DataException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public class DataRowImpl implements DataRow {
    private Map<String, Object> map = new HashMap<>();
    private String[] labels;

    public DataRowImpl(String[] labels, Map<String, Object> map) {
        this.labels = labels;
        this.map = map;
    }

    @Override
    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public Object getObject(int column) {
        try {
            return map.get(labels[column - 1]);
        } catch (Throwable e) {
            throw new DataException("getObject", e);
        }
    }

    @Override
    public Object getObject(String label) {
        try {
            return map.get(label);
        } catch (Throwable e) {
            throw new DataException("getObject", e);
        }
    }

    @Override
    public Integer getInt(int column) throws DataException {
        return toInt(getObject(column));
    }

    @Override
    public Integer getInt(String label) throws DataException {
        return toInt(getObject(label));
    }

    private Integer toInt(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj; //no use
        }
        if (obj != null) {
            return Integer.parseInt(obj.toString());
        }
        return null;
    }

    @Override
    public Double getDouble(int column) throws DataException {
        return toDouble(getObject(column));
    }

    @Override
    public Double getDouble(String label) throws DataException {
        return toDouble(getObject(label));
    }

    private Double toDouble(Object obj) {
        if (obj instanceof Double) {
            return (Double) obj; //no use
        }
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return null;
    }

    @Override
    public Long getLong(int column) throws DataException {
        return toLong(getObject(column));
    }

    @Override
    public Long getLong(String label) throws DataException {
        return toLong(getObject(label));
    }

    private Long toLong(Object obj) {
        if (obj instanceof Long) {
            return (Long) obj; //no use
        }
        if (obj != null) {
            return Long.parseLong(obj.toString());
        }
        return null;
    }

    @Override
    public String getString(int column) throws DataException {
        return toString(getObject(column));
    }

    @Override
    public String getString(String label) throws DataException {
        return toString(getObject(label));
    }

    private String toString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    @Override
    public Short getShort(int column) throws DataException {
        return toShort(getObject(column));
    }

    @Override
    public Short getShort(String label) throws DataException {
        return toShort(getObject(label));
    }

    private Short toShort(Object obj) {
        if (obj instanceof Short) {
            return (Short) obj; //no use
        }
        if (obj != null) {
            return Short.parseShort(obj.toString());
        }
        return null;
    }

    @Override
    public Boolean getBoolean(int column) throws DataException {
        return toBoolean(getObject(column));
    }


    @Override
    public Boolean getBoolean(String label) throws DataException {
        return toBoolean(getObject(label));
    }


    private Boolean toBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj != null) {
            return Boolean.parseBoolean(obj.toString());
        }
        return null;
    }

    @Override
    public Date getDate(int column) throws DataException {
        return toDate(getObject(column));
    }

    @Override
    public Date getDate(String label) throws DataException {
        return toDate(getObject(label));
    }

    private Date toDate(Object obj) {
        if (obj instanceof Date) {
            return (Date) obj;
        }
        return null;
    }

    @Override
    public Byte getByte(int column) throws DataException {
        return toByte(getByte(column));
    }

    @Override
    public Byte getByte(String label) throws DataException {
        return toByte(getByte(label));
    }

    private Byte toByte(Object obj) {
        if (obj instanceof Byte) {
            return (Byte) obj; //no use
        }
        if (obj != null) {
            return Byte.parseByte(obj.toString());
        }
        return null;
    }
}

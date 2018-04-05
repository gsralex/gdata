package com.gsralex.gdata.bean.exception;

/**
 * @author gsralex
 * @version 2018/3/28
 */
public class DataException extends RuntimeException {

    public DataException() {
        super();
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }


}

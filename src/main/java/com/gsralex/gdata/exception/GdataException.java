package com.gsralex.gdata.exception;

/**
 * @author gsralex
 * @version 2018/3/22
 */
public class GdataException extends RuntimeException {

    public  GdataException(String message){
        super(message);
    }

    public GdataException(Throwable cause) {
        super(cause);
    }
}

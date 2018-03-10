package com.gsralex.gdata.core;

import java.lang.reflect.Method;

/**
 * @author gsralex
 * @date 2018/2/22
 */
public class FieldValue {

    private Object instance;
    private Class type;

    public FieldValue(Object instance, Class type) {
        this.instance = instance;
        this.type = type;
    }

    public Object getInstance() {
        return instance;
    }


    public <T> T getValue(Class<T> fieldType, String fieldName)  {
        try {
            String typeName = fieldType.getTypeName();
            Method method = null;
            switch (typeName) {
                case "boolean": {
                    try {
                        method = type.getMethod(getIsMethodName(fieldName));
                    } catch (NoSuchMethodException e) {
                        try {
                            method = type.getMethod(getGetMethodName(fieldName));
                        } catch (NoSuchMethodException e1) {
                        }
                    }
                    break;
                }
                default: {
                    method = type.getMethod(getGetMethodName(fieldName));
                }
            }
            return (T) method.invoke(instance);
        }catch (Throwable e){
            return null;
        }
    }


    public <T> void setValue(Class<T> fieldType, String fieldName, T value)  {
        try {
            String setMethodName = getSetMethodName(fieldName);
            Method method = type.getMethod(setMethodName, fieldType);
            method.invoke(instance, value);
        }catch(Throwable e){
            return;
        }
    }

    private static String getGetMethodName(String fieldName) {
        return "get" + getPostMethodName(fieldName);
    }

    private static String getIsMethodName(String fieldName) {
        return "is" + getPostMethodName(fieldName);
    }

    private static String getSetMethodName(String fieldName) {
        return "set" + getPostMethodName(fieldName);
    }

    private static String getPostMethodName(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}

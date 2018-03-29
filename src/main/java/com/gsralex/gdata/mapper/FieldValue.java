package com.gsralex.gdata.mapper;


import com.gsralex.gdata.exception.DataException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/2/22
 */
public class FieldValue {

    private Object instance;
    private Class type;
    private Map<String, List<Method>> methodMap;

    public FieldValue(Object instance) {
        this.instance = instance;
        this.type = instance.getClass();
        this.init(this.type);
    }

    private void init(Class type) {
        Method[] methods = type.getMethods();
        this.methodMap = new HashMap<>();
        for (Method method : methods) {
            String key = method.getName().toLowerCase();
            if (this.methodMap.containsKey(key)) {
                this.methodMap.get(key).add(method);
            } else {
                List<Method> methodList = new ArrayList<>();
                methodList.add(method);
                this.methodMap.put(key, methodList);
            }
        }
    }

    public Object getInstance() {
        return instance;
    }


    public <T> T getValue(Class<T> fieldType, String fieldName) {
        try {
            String typeName = fieldType.getTypeName();
            Method method;
            switch (typeName) {
                case "boolean": {
                    method = this.getMethod(getIsMethodName(fieldName), null);
                    if (method == null) {
                        method = this.getMethod(getGetMethodName(fieldName), null);
                        if (method == null) {
                            throw new NoSuchMethodException("getMethod:" + this.type.getName() + "." + fieldName);
                        }
                    }
                    break;
                }
                default: {
                    method = this.getMethod(getGetMethodName(fieldName), null);
                    if (method == null) {
                        throw new NoSuchMethodException("getMethod:" + this.type.getName() + "." + fieldName);
                    }
                }
            }
            return (T) method.invoke(instance);
        } catch (Throwable e) {
            throw new DataException("getValue", e);
        }
    }


    public void setValue(Class fieldType, String fieldName, Object value) {
        try {
            String setMethodName = getSetMethodName(fieldName);
            Method method = this.getMethod(setMethodName, fieldType);
            if (method == null) {
                throw new NoSuchMethodException("setMethod:" + this.type.getName() + "." + fieldName);
            }
            method.invoke(instance, value);
        } catch (Throwable e) {
            throw new DataException("setValue", e);
        }
    }

    private Method getMethod(String methodName, Class fieldType) {
        String name = methodName.toLowerCase();
        List<Method> methodList = this.methodMap.get(name);
        if (methodList != null && methodList.size() != 0) {
            for (Method method : methodList) {
                Class[] types = method.getParameterTypes();
                if (fieldType != null) {
                    if (types != null && types.length == 1 && types[0] == fieldType) {
                        return method;
                    }
                } else {
                    if (types == null || types.length == 0) {
                        return method;
                    }
                }
            }
        }
        return null;
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

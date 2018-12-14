package com.gsralex.gdata.bean.mapper;


import com.gsralex.gdata.bean.exception.DataException;

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

    private static final String TYPENAME_BOOLEAN = "boolean";
    private static final String TYPENAME_JAVALANGBOOLEAN = "java.lang.Boolean";

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
                case TYPENAME_BOOLEAN: {
                    method = this.getMethod(getIsMethodName(fieldName), null);//1
                    if (method == null) {
                        method = this.getMethod(getGetMethodName(fieldName), null);//2
                    }
                    if (method == null) {
                        method = this.getMethod(getGetNoIsMethodName(fieldName), null);//3
                    }
                    break;
                }
                case TYPENAME_JAVALANGBOOLEAN: {
                    method = this.getMethod(getGetNoIsMethodName(fieldName), null);//3
                    if (method == null) {
                        method = this.getMethod(getGetMethodName(fieldName), null);//2
                    }
                    if (method == null) {
                        method = this.getMethod(getIsMethodName(fieldName), null);//1
                    }
                    break;
                }
                default: {
                    method = this.getMethod(getGetMethodName(fieldName), null);
                }
            }
            if (method == null) {
                throw new NoSuchMethodException("getMethod:" + this.type.getName() + "." + fieldName);
            }
            return (T) method.invoke(instance);
        } catch (Exception e) {
            throw new DataException("getValue", e);
        }
    }


    public void setValue(Class fieldType, String fieldName, Object value) {
        try {
            String setMethodName = getSetMethodName(fieldName);
            String typeName = fieldType.getTypeName();
            Method method;
            switch (typeName) {
                case TYPENAME_BOOLEAN:
                case TYPENAME_JAVALANGBOOLEAN: {
                    method = this.getMethod(getSetNoIsMethodName(fieldName), fieldType);
                    if (method == null) {
                        method = this.getMethod(setMethodName, fieldType);
                    }
                    break;
                }
                default: {
                    method = this.getMethod(setMethodName, fieldType);
                    break;
                }
            }
            if (method == null) {
                throw new NoSuchMethodException("setMethod:" + this.type.getName() + "." + fieldName);
            }
            method.invoke(instance, value);
        } catch (Exception e) {
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
        return "get" + getInitCapsMethodName(fieldName);
    }

    private static String getIsMethodName(String fieldName) {
        return "is" + getInitCapsMethodName(fieldName);
    }

    private static String getGetNoIsMethodName(String fieldName) {
        String noIsMethodName = fieldName;
        if (fieldName.startsWith("is")) {
            noIsMethodName = fieldName.substring(2);
        }
        return "get" + getInitCapsMethodName(noIsMethodName);
    }


    private static String getSetMethodName(String fieldName) {
        return "set" + getInitCapsMethodName(fieldName);
    }

    private static String getSetNoIsMethodName(String fieldName) {
        String noIsMethodName = fieldName;
        if (fieldName.startsWith("is")) {
            noIsMethodName = fieldName.substring(2);
        }
        return "set" + getInitCapsMethodName(noIsMethodName);
    }

    private static String getInitCapsMethodName(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}

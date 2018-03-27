package com.gsralex.gdata;

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
            Method method = null;
            switch (typeName) {
                case "boolean": {
                    try {
                        method = this.getMethod(getIsMethodName(fieldName), null);
                    } catch (NoSuchMethodException e) {
                        try {
                            method = this.getMethod(getGetMethodName(fieldName), null);
                        } catch (NoSuchMethodException e1) {
                        }
                    }
                    break;
                }
                default: {
                    method = this.getMethod(getGetMethodName(fieldName), null);
                }
            }
            return (T) method.invoke(instance);
        } catch (Throwable e) {
            return null;
        }
    }


    public void setValue(Class fieldType, String fieldName, Object value) {
        try {
            String setMethodName = getSetMethodName(fieldName);
            Method method = this.getMethod(setMethodName, fieldType);
            method.invoke(instance, value);
        } catch (Throwable e) {
            return;
        }
    }

    private Method getMethod(String methodName, Class fieldType) throws NoSuchMethodException {
        String name = methodName.toLowerCase();
        List<Method> methodList = this.methodMap.get(name);
        if (methodList.size() != 0) {
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
        throw new NoSuchMethodException();
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

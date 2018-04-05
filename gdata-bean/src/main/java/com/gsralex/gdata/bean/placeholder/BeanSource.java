package com.gsralex.gdata.bean.placeholder;

/**
 * @author gsralex
 * @version 2018/4/5
 */
public class BeanSource {

    private Object value;

    public BeanSource() {
    }

    public BeanSource(Object object) {
        this.value = object;
    }

    public void setValue(Object object) {
        this.value = object;
    }

    public Object getValue() {
        return value;
    }
}

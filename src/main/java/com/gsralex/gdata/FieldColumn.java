package com.gsralex.gdata;

/**
 * @author gsralex
 * 2018/2/18
 */
public class FieldColumn {
    private String name;
    private boolean id;
    private String label;
    private Class type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}

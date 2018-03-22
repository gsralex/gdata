package com.gsralex.gdata;

/**
 * @author gsralex
 * @version 2018/2/18
 */
public class FieldColumn {
    private String name;
    private boolean id;
    private boolean generatedKey;
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

    public boolean isGeneratedKey() {
        return generatedKey;
    }

    public void setGeneratedKey(boolean generatedKey) {
        this.generatedKey = generatedKey;
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

package com.gsralex.gdata;

/**
 * @author gsralex
 * @date 2018/2/18
 */
public class FieldColumn {
    private String name;
    private boolean id;
    private boolean alias;
    private String aliasName;
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

    public boolean isAlias() {
        return alias;
    }

    public void setAlias(boolean alias) {
        this.alias = alias;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}

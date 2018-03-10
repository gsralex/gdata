package com.gsralex.gdata.core.domain;

import com.gsralex.gdata.core.annotation.AliasField;
import com.gsralex.gdata.core.annotation.IdField;
import com.gsralex.gdata.core.annotation.TbName;

import java.util.Date;

/**
 * @author gsralex
 * @date 2018/3/10
 */
@TbName(name = "t_foo")
public class Foo {

    @IdField
    private long id;

    @AliasField(name = "foo_1")
    private String foo1;

    @AliasField(name = "foo_2")
    private double foo2;

    @AliasField(name = "foo_3")
    private Date foo3;

    @AliasField(name = "foo_4")
    private int foo4;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFoo1() {
        return foo1;
    }

    public void setFoo1(String foo1) {
        this.foo1 = foo1;
    }

    public double getFoo2() {
        return foo2;
    }

    public void setFoo2(double foo2) {
        this.foo2 = foo2;
    }

    public Date getFoo3() {
        return foo3;
    }

    public void setFoo3(Date foo3) {
        this.foo3 = foo3;
    }

    public int getFoo4() {
        return foo4;
    }

    public void setFoo4(int foo4) {
        this.foo4 = foo4;
    }
}

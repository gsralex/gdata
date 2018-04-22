package com.gsralex.gdata.bean.domain;


import com.gsralex.gdata.bean.annotation.Id;
import com.gsralex.gdata.bean.annotation.Column;
import com.gsralex.gdata.bean.annotation.Table;

import java.util.Date;

/**
 * @author gsralex
 *         2018/3/10
 */
@Table(name = "t_foo")
public class Foo {

    @Id
    private int id;

    @Column(name = "foo_1")
    private String foo1;

    @Column(name = "foo_2")
    private double foo2;

    @Column(name = "foo_3")
    private Date foo3;

    @Column(name = "foo_4")
    private int foo4;

    @Column(name = "foo_img")
    private String fooimg;

    @Column(name = "foo_5")
    private boolean foo5;

    @Column(name = "foo_date")
    private Date fooDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getFooImg() {
        return fooimg;
    }

    public void setFooImg(String fooimg) {
        this.fooimg = fooimg;
    }

    public boolean isFoo5() {
        return foo5;
    }

    public void setFoo5(boolean foo5) {
        this.foo5 = foo5;
    }

    public Date getFooDate() {
        return fooDate;
    }

    public void setFooDate(Date fooDate) {
        this.fooDate = fooDate;
    }
}

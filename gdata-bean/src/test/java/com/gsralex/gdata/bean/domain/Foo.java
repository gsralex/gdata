package com.gsralex.gdata.bean.domain;


import com.gsralex.gdata.bean.annotation.Id;
import com.gsralex.gdata.bean.annotation.Column;
import com.gsralex.gdata.bean.annotation.Table;

import java.util.Date;

/**
 * @author gsralex
 *         2018/3/10
 */
@Table("t_foo")
public class Foo {

    @Id
    private Long id;

    @Column("foo_1")
    private String foo1;

    @Column("foo_2")
    private double foo2;

    @Column("foo_3")
    private Date foo3;

    @Column("foo_4")
    private int foo4;

    @Column("foo_img")
    private String fooimg;

    @Column("foo_5")
    private boolean foo5;

    @Column("foo_date")
    private Date fooDate;


    @Column("foo_6")
    private Boolean isFoo6;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getFooimg() {
        return fooimg;
    }

    public void setFooimg(String fooimg) {
        this.fooimg = fooimg;
    }

    public Boolean getFoo6() {
        return isFoo6;
    }

    public void setFoo6(Boolean foo6) {
        isFoo6 = foo6;
    }
}

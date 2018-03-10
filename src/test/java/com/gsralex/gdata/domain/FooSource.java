package com.gsralex.gdata.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gsralex
 * @date 2018/3/10
 */
public class FooSource {

    public static Foo getEntity() {
        Foo foo1 = new Foo();
        foo1.setFoo1("123");
        foo1.setFoo2(123.13);
        foo1.setFoo3(new Date());
        foo1.setFoo4(1);
        return foo1;
    }

    public static List<Foo> getEntityList() {
        List<Foo> list = new ArrayList<>();
        Foo foo1 = new Foo();
        foo1.setFoo1("123");
        foo1.setFoo2(123.13);
        foo1.setFoo3(new Date());
        foo1.setFoo4(1);
        list.add(foo1);

        Foo foo2 = new Foo();
        foo2.setFoo1("1234");
        foo2.setFoo2(1234.13);
        foo2.setFoo3(new Date());
        foo2.setFoo4(2);
        list.add(foo2);
        return list;
    }
}

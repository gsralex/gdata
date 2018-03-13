package com.gsralex.gdata.jdbctemplate;

import com.gsralex.gdata.DataSourceConfg;
import com.gsralex.gdata.domain.Foo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * @author gsralex
 * @date 2018/3/10
 */
public class JdbcTemplateUtilsTest {

    private JdbcTemplateUtils templateUtils;


    @Before
    public void setUpBeforeClass() throws Exception {
        templateUtils = new JdbcTemplateUtils(DataSourceConfg.getDataSource());
    }

    @Test
    public void insert() throws Exception {
        Foo foo1 = new Foo();
        foo1.setFoo1("1234");
        foo1.setFoo2(123.13);
        foo1.setFoo3(new Date());
        foo1.setFoo4(1);
        Assert.assertEquals(true, templateUtils.insert(foo1));
        Assert.assertEquals(0, foo1.getId());

        Foo foo2 = new Foo();
        foo2.setFoo1("1234");
        foo2.setFoo2(123.13);
        foo2.setFoo3(new Date());
        foo2.setFoo4(1);
        Assert.assertEquals(true, templateUtils.insert(foo2, true));
        Assert.assertNotEquals(0, foo2.getId());
    }

    @Test
    public void batchInsert() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void batchUpdate() throws Exception {

    }

    @Test
    public void get() throws Exception {
        Foo foo1 = new Foo();
        foo1.setFoo1("1234");
        foo1.setFoo2(123.13);
        foo1.setFoo3(new Date());
        foo1.setFoo4(1);
        Assert.assertEquals(true, templateUtils.insert(foo1, true));
        Foo foo1Data = templateUtils.get("select * from t_foo where id=?", new Object[]{foo1.getId()}, Foo.class);
        Assert.assertNotEquals(foo1Data, null);
    }

    @Test
    public void getList() throws Exception {

    }

    @Test
    public void getJdbcTemplate() throws Exception {

    }

}
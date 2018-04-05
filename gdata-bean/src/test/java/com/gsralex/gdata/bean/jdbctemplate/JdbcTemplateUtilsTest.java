package com.gsralex.gdata.bean.jdbctemplate;

import com.gsralex.gdata.bean.DataSourceConfg;
import com.gsralex.gdata.bean.domain.Foo;
import com.gsralex.gdata.bean.domain.FooSource;
import com.gsralex.gdata.bean.result.DataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        List<Foo> fooList = FooSource.getEntityList();
        Assert.assertEquals(templateUtils.batchInsert(fooList), 2);
    }

    @Test
    public void update() throws Exception {
        Foo foo = FooSource.getEntity();
        templateUtils.insert(foo, true);
        foo.setFoo1("111");
        foo.setFoo2(111);
        Date now = new Date();
        foo.setFoo3(now);
        foo.setFoo4(111);

        templateUtils.update(foo);
        Foo data = templateUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo.getId()}, Foo.class);
        Assert.assertEquals(data.getFoo1(), foo.getFoo1());
        Assert.assertEquals(data.getFoo4(), foo.getFoo4());

    }

    @Test
    public void batchUpdate() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Foo foo2 = FooSource.getEntity();
        List<Foo> list = new ArrayList<>();
        list.add(foo1);
        list.add(foo2);
        Assert.assertEquals(templateUtils.batchUpdate(list), 2);
    }

    @Test
    public void get() throws Exception {
        Foo foo1 = new Foo();
        foo1.setFoo1("1234");
        foo1.setFoo2(123.13);
        foo1.setFoo3(new Date());
        foo1.setFoo4(1);
        Assert.assertEquals(true, templateUtils.insert(foo1, true));
        Foo foo1Data = templateUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo1.getId()}, Foo.class);
        Assert.assertNotEquals(foo1Data, null);

        String cnt = templateUtils.queryForObject("select count(1) from t_foo ", null, String.class);
        Assert.assertNotEquals(cnt, null);
    }

    @Test
    public void getList() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Assert.assertEquals(true, templateUtils.insert(foo1, true));
        List<Foo> fooList = templateUtils.queryForList("select * from t_foo where id=?", new Object[]{foo1.getId()}, Foo.class);
        Assert.assertEquals(fooList.size(), 1);
    }

    @Test
    public void getJdbcTemplate() throws Exception {

    }


    @Test
    public void delete() throws Exception {
        Foo foo = FooSource.getEntity();
        templateUtils.insert(foo, true);
        templateUtils.delete(foo);
        Foo data = templateUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo.getId()}, Foo.class);
        Assert.assertEquals(data, null);
    }

    @Test
    public void batchDelete() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Foo foo2 = FooSource.getEntity();
        List<Foo> list = new ArrayList<>();
        list.add(foo1);
        list.add(foo2);
        templateUtils.insert(foo1, true);
        templateUtils.insert(foo2, true);
        templateUtils.batchDelete(list);
        int size = templateUtils.queryForList("select * from t_foo where id in (" + foo1.getId() + "," + foo2.getId() + ")", null, Foo.class).size();
        Assert.assertEquals(size, 0);
    }

    @Test
    public void queryForDataSet() throws Exception {
        Foo foo = FooSource.getEntity();
        templateUtils.insert(foo, true);
        DataSet meset = templateUtils.queryForDataSet("select * from t_foo where id=? ", new Object[]{foo.getId()});
        Assert.assertNotEquals(meset.getRows().size(), 0);
        Assert.assertNotEquals(meset.get(0).getInt("id"), null);
        Assert.assertNotEquals(meset.get(0).getString("foo_1"), null);
        Assert.assertNotEquals(meset.get(0).getDate("foo_3"), null);
        Assert.assertNotEquals(meset.get(0).getBoolean("foo_5"), null);
    }

}
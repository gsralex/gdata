package com.gsralex.gdata.jdbc;

import com.gsralex.gdata.DataSourceConfg;
import com.gsralex.gdata.domain.Foo;
import com.gsralex.gdata.domain.FooSource;
import com.gsralex.gdata.result.DataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gsralex
 *         2018/3/10
 */
public class JdbcUtilsTest {


    private JdbcUtils jdbcUtils;

    @Before
    public void setUpBeforeClass() throws Exception {
        jdbcUtils = new JdbcUtils(DataSourceConfg.getDataSource());
    }


    @Test
    public void save() throws Exception {
        Assert.assertEquals(true, jdbcUtils.insert(FooSource.getEntity()));

        Foo foo = FooSource.getEntity();
        jdbcUtils.insert(foo, true);
        Assert.assertNotEquals(0, foo.getId());

    }

    @Test
    public void batchSave() throws Exception {
        List<Foo> list = FooSource.getEntityList();
        int r = jdbcUtils.batchInsert(list, true);
        Assert.assertEquals(r, 2);
        Assert.assertNotEquals(list.get(0).getId(), 0);
        Assert.assertEquals(list.get(0).getId() < list.get(1).getId(), true);

    }

    @Test
    public void update() throws Exception {
        Foo foo = FooSource.getEntity();
        jdbcUtils.insert(foo, true);
        foo.setFoo1("112");
        foo.setFoo2(112);
        Date now = new Date();
        foo.setFoo3(now);
        foo.setFoo4(112);

        jdbcUtils.update(foo);
        Foo data = jdbcUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo.getId()}, Foo.class);
        Assert.assertEquals(data.getFoo1(), foo.getFoo1());
        Assert.assertEquals(data.getFoo4(), foo.getFoo4());
        Assert.assertNotEquals(data.getFooImg(), null);
    }

    @Test
    public void batchUpdate() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Foo foo2 = FooSource.getEntity();
        List<Foo> list = new ArrayList<>();
        list.add(foo1);
        list.add(foo2);
        Assert.assertEquals(jdbcUtils.batchUpdate(list), 2);
    }

    @Test
    public void executeUpdate() throws Exception {
        Foo foo1 = FooSource.getEntity();
        jdbcUtils.insert(foo1, true);

        String sql = "update t_foo set foo_1=? where id=?";
        String foo_1 = "123123123";
        jdbcUtils.executeUpdate(sql, new Object[]{foo_1, foo1.getId()});
        Foo data = jdbcUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo1.getId()}, Foo.class);
        Assert.assertEquals(foo_1, data.getFoo1());
    }

    @Test
    public void get() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Assert.assertEquals(true, jdbcUtils.insert(foo1, true));

        Foo fooData = jdbcUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo1.getId()}, Foo.class);
        Integer cnt = jdbcUtils.queryForObject("select count(1) from t_foo", null, Integer.class);
        Assert.assertNotEquals(cnt, null);
    }


    @Test
    public void query() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Assert.assertEquals(true, jdbcUtils.insert(foo1, true));
        List<Foo> fooList = jdbcUtils.queryForList("select * from t_foo where id=?", new Object[]{foo1.getId()}, Foo.class);


        Assert.assertEquals(fooList.size(), 1);
    }

    @Test
    public void executeBatch() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Foo foo2 = FooSource.getEntity();
        List<Foo> list = new ArrayList<>();
        list.add(foo1);
        list.add(foo2);
        jdbcUtils.batchInsert(list, true);

        String sql = "update t_foo set foo_1=? where id=?";
        String foo_1 = "123123123";
        Object[] obj1 = new Object[]{foo_1, foo1.getId()};
        Object[] obj2 = new Object[]{foo_1, foo2.getId()};
        List<Object[]> objList = new ArrayList<>();
        objList.add(obj1);
        objList.add(obj2);
        Assert.assertEquals(jdbcUtils.executeBatch(sql, objList), 2);

        String querySql = "select * from t_foo where id in (" + foo1.getId() + "," + foo2.getId() + ")";
        List<Foo> dataList = jdbcUtils.queryForList(querySql, null, Foo.class);
        Assert.assertEquals(dataList.get(0).getFoo1(), foo_1);
        Assert.assertEquals(dataList.get(1).getFoo1(), foo_1);
    }

    @Test
    public void delete() throws Exception {
        Foo foo = FooSource.getEntity();
        jdbcUtils.insert(foo, true);
        jdbcUtils.delete(foo);
        Foo data = jdbcUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo.getId()}, Foo.class);
        Assert.assertEquals(data, null);
    }

    @Test
    public void batchDelete() throws Exception {
        Foo foo1 = FooSource.getEntity();
        Foo foo2 = FooSource.getEntity();
        List<Foo> list = new ArrayList<>();
        list.add(foo1);
        list.add(foo2);
        jdbcUtils.batchInsert(list, true);
        jdbcUtils.batchDelete(list);
        int size = jdbcUtils.queryForList("select * from t_foo where id in (" + foo1.getId() + "," + foo2.getId() + ")", null, Foo.class).size();
        Assert.assertEquals(size, 0);
    }

    @Test
    public void queryForDataSet() throws Exception {
        Foo foo = FooSource.getEntity();
        jdbcUtils.insert(foo, true);
        DataSet meset = jdbcUtils.queryForDataSet("select * from t_foo where id=? ", new Object[]{foo.getId()});
        Assert.assertNotEquals(meset.getRows().size(), 0);
        Assert.assertNotEquals(meset.get(0).getInt("id"), null);
        Assert.assertNotEquals(meset.get(0).getString("foo_1"), null);
        Assert.assertNotEquals(meset.get(0).getDate("foo_3"), null);
        Assert.assertNotEquals(meset.get(0).getBoolean("foo_5"), null);
    }

    @Test
    public void transaction() {
        Foo foo = FooSource.getEntity();
        jdbcUtils.setAutoCommit(false);
        jdbcUtils.insert(foo, true);
        Foo data = jdbcUtils.queryForObject("select * from t_foo where id=? ", new Object[]{foo.getId()}, Foo.class);
        Foo foo1 = FooSource.getEntity();
        jdbcUtils.insert(foo1, true);
        Assert.assertNotEquals(foo1.getId(), 0);
        jdbcUtils.rollback();
        jdbcUtils.setAutoCommit(true);
        Foo foo2 = FooSource.getEntity();
        jdbcUtils.insert(foo2, true);
        Assert.assertEquals(foo2.getId(), foo1.getId() + 1);
        Assert.assertNotEquals(getData(foo2.getId()), null);
    }

    public Foo getData(long id) {
        return jdbcUtils.queryForObject("select * from t_foo where id=? ", new Object[]{id}, Foo.class);
    }

}
package com.gsralex.gdata.bean.jdbc;

import com.gsralex.gdata.bean.DataSourceConfg;
import com.gsralex.gdata.bean.domain.FooSource;
import com.gsralex.gdata.bean.domain.Foo;
import com.gsralex.gdata.bean.domain.FooVo;
import com.gsralex.gdata.bean.exception.DataException;
import com.gsralex.gdata.bean.placeholder.BeanSource;
import com.gsralex.gdata.bean.result.DataSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

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
        foo.setFoo6(null);
        jdbcUtils.insert(foo, true);
        Assert.assertNotEquals(0, foo.getId());
        String sql = "select * from t_foo where id=?";
        Foo fooData = jdbcUtils.queryForObject(sql, new Object[]{foo.getId()}, Foo.class);
        Assert.assertEquals(fooData.getFoo6(), null);

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
        Date now = new Date();
        foo1.setFooDate(now);
        foo1.setFoo5(true);
        foo1.setFoo6(true);
        Assert.assertEquals(true, jdbcUtils.insert(foo1, true));


        Foo fooData = jdbcUtils.queryForObject("select * from t_foo where id=?", new Object[]{foo1.getId()}, Foo.class);
        Integer cnt = jdbcUtils.queryForObject("select count(1) from t_foo", null, Integer.class);
        Assert.assertNotEquals(cnt, null);
        Assert.assertEquals(fooData.isFoo5(), true);
        Assert.assertEquals(fooData.getFoo6(), true);
        //date test
        String nullTime = "00:00:00";
        String hhmmss = "HH:mm:ss";
        String nowH = DateFormatUtils.format(now, hhmmss);
        String dataH = DateFormatUtils.format(fooData.getFooDate(), hhmmss);
        if (!StringUtils.equals(nowH, nullTime)) {
            Assert.assertNotEquals(dataH, nullTime);
        }
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

        List<Foo> list = new ArrayList<>();
        Foo foo3 = FooSource.getEntity();
        Foo foo4 = FooSource.getEntity();
        list.add(foo3);
        list.add(foo4);
        jdbcUtils.batchInsert(list, true);
        Assert.assertNotEquals(foo3.getId(), 0);
        Assert.assertNotEquals(foo4.getId(), 0);


        jdbcUtils.rollback();
        jdbcUtils.commit();
        jdbcUtils.close();
        Foo foo2 = FooSource.getEntity();
        jdbcUtils.insert(foo2, true);
        Assert.assertEquals(foo2.getId(), foo1.getId() + 3);
        Assert.assertNotEquals(getData(foo2.getId()), null);
    }

    public Foo getData(long id) {
        return jdbcUtils.queryForObject("select * from t_foo where id=? ", new Object[]{id}, Foo.class);
    }

    @Test
    public void updateP() {
        Foo foo = FooSource.getEntity();
        jdbcUtils.insert(foo, true);
        Map<String, Object> map = new HashMap<>();
        map.put("Foo1", "t_123");
        map.put("Id", foo.getId());
        jdbcUtils.executeUpdatePh("update t_foo set foo_1=:foo1 where id=:id", map);
        Foo data = getData(foo.getId());
        Assert.assertEquals(data.getFoo1(), "t_123");
        foo.setFoo1("t_1234");
        jdbcUtils.executeUpdatePh("update t_foo set foo_1=:foo1 where id=:id", new BeanSource(foo));
        Foo data1 = getData(foo.getId());
        Assert.assertEquals(data1.getFoo1(), "t_1234");
    }

    @Test
    public void queryVo() {
        String sql = "select * from t_foo";
        Foo foo = FooSource.getEntity();
        jdbcUtils.insert(foo, true);
        List<FooVo> list = jdbcUtils.queryForList(sql, null, FooVo.class);
        Assert.assertNotEquals(list.size(), 0);
        Assert.assertEquals(list.get(0).getFoo_7(), null);
        Assert.assertNotEquals(list.get(0).getId(), null);
        Assert.assertNotEquals(list.get(0).getFoo_1(), null);
    }

}
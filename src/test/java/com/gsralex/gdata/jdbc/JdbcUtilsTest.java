package com.gsralex.gdata.jdbc;

import com.gsralex.gdata.DataSourceConfg;
import com.gsralex.gdata.domain.Foo;
import com.gsralex.gdata.domain.FooSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    }

    @Test
    public void batchUpdate() throws Exception {

    }

    @Test
    public void executeUpdate() throws Exception {

    }

    @Test
    public void query() throws Exception {

    }

    @Test
    public void executeBatch() throws Exception {

    }

}
package com.gsralex.gdata.jdbc;

import com.gsralex.gdata.DataSourceConfg;
import com.gsralex.gdata.domain.Foo;
import com.gsralex.gdata.domain.FooSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Assert.assertEquals(jdbcUtils.batchInsert(FooSource.getEntityList()), 2);
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
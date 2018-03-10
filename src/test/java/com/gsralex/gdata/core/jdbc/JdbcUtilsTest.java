package com.gsralex.gdata.core.jdbc;

import com.gsralex.gdata.core.DataSourceConfg;
import com.gsralex.gdata.core.domain.Foo;
import com.gsralex.gdata.core.domain.FooSource;
import com.gsralex.gdata.core.jdbc.JdbcUtils;
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
public class JdbcUtilsTest {


    private JdbcUtils jdbcUtils;

    @Before
    public void setUpBeforeClass() throws Exception {
        jdbcUtils = new JdbcUtils(DataSourceConfg.getDataSource());
    }


    @Test
    public void save() throws Exception {
        Assert.assertEquals(true, jdbcUtils.insert(FooSource.getEntity()));
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
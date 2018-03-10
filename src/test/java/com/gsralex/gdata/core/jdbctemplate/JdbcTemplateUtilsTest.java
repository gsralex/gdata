package com.gsralex.gdata.core.jdbctemplate;

import com.gsralex.gdata.core.DataSourceConfg;
import com.gsralex.gdata.core.domain.Foo;
import com.gsralex.gdata.core.jdbc.JdbcUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

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

    }

    @Test
    public void getList() throws Exception {

    }

    @Test
    public void getJdbcTemplate() throws Exception {

    }

}
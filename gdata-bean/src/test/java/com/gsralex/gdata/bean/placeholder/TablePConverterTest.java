package com.gsralex.gdata.bean.placeholder;

import com.gsralex.gdata.bean.domain.Foo;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class TablePConverterTest {


    @Test
    public void convert() throws Exception {

        String namedSql = "insert into #table value(1,1,1)";

        TablePConverter converter = new TablePConverter();
        String sql = converter.convert(namedSql, Foo.class);
        Assert.assertEquals(sql, "insert into t_foo value(1,1,1)");
    }

}
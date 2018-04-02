package com.gsralex.gdata.named;

import com.gsralex.gdata.domain.Foo;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class TableNamedConverterTest {


    @Test
    public void convert() throws Exception {

        String namedSql = "insert into #table value(1,1,1)";

        TableNamedConverter converter = new TableNamedConverter();
        String sql = converter.convert(namedSql, Foo.class);
        Assert.assertEquals(sql, "insert into t_foo value(1,1,1)");
    }

}
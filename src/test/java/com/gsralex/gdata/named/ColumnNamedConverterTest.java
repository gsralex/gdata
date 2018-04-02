package com.gsralex.gdata.named;

import com.gsralex.gdata.domain.Foo;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class ColumnNamedConverterTest {
    @Test
    public void convert() throws Exception {
        String namedSql = "insert into #table(#id,#foo1,#foo2) value(1,1,1)";

        ColumnNamedConverter converter = new ColumnNamedConverter();
        String sql = converter.convert(namedSql, Foo.class);
        Assert.assertEquals(sql, "insert into #table(`id`,`foo_1`,`foo_2`) value(1,1,1)");
    }

}
package com.gsralex.gdata.bean.placeholder;

import com.gsralex.gdata.bean.domain.Foo;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class ColumnPConverterTest {
    @Test
    public void convert() throws Exception {
        String namedSql = "insert into #table(#id,#foo1,#foo2) value(1,1,1)";

        ColumnPConverter converter = new ColumnPConverter();
        String sql = converter.convert(namedSql, Foo.class);
        Assert.assertEquals(sql, "insert into #table(`id`,`foo_1`,`foo_2`) value(1,1,1)");
    }

}
package com.gsralex.gdata.bean.placeholder;

import com.gsralex.gdata.bean.domain.Foo;
import com.gsralex.gdata.bean.domain.FooSource;
import org.junit.Test;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class ValueConverterImplTest {
    @Test
    public void convert() throws Exception {
        ValueConverter valueConverter = new ValueConverterImpl();
        Foo foo = FooSource.getEntity();
        String namedSql = "select * from #table where #id=? and #foo=:foo1 and #foo1  in （select #foo1 from where id=?）";
        valueConverter.convert(namedSql, Foo.class, foo, new Object[]{1, 1});
    }

}
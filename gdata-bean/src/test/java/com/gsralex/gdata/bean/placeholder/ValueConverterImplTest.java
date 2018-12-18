package com.gsralex.gdata.bean.placeholder;

import com.gsralex.gdata.bean.domain.Foo;
import com.gsralex.gdata.bean.domain.FooSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class ValueConverterImplTest {
    @Test
    public void convertObject() throws Exception {
        ValueConverter valueConverter = new ValueConverterImpl();
        Foo foo = FooSource.getEntity();
        String pSql = "select * from #table where id=:id and foo=:foo1 and id=:id";
        SqlObject sqlObject = valueConverter.convertBeanSource(pSql, new BeanSource(foo));

        Assert.assertEquals(sqlObject.getSql(), "select * from #table where id=? and foo=? and id=?");
        Assert.assertEquals(sqlObject.getObjects().length, 3);
    }


    @Test
    public void convertMap() throws Exception {
        ValueConverter valueConverter = new ValueConverterImpl();
        Foo foo = FooSource.getEntity();
        foo.setId(1L);
        String pSql = "select * from #table where id=:id and foo=:foo1 and id=:id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Id", foo.getId());
        paramMap.put("foo1", foo.getFoo1());
        SqlObject sqlObject = valueConverter.convertMap(pSql, paramMap);

        Assert.assertEquals(sqlObject.getSql(), "select * from #table where id=? and foo=? and id=?");
        Assert.assertEquals(sqlObject.getObjects().length, 3);
    }

}
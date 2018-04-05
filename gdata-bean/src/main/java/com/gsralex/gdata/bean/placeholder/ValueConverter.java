package com.gsralex.gdata.bean.placeholder;

import java.util.Map;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public interface ValueConverter {

    SqlObject convertBeanSource(String pSql, BeanSource beanSource);

    SqlObject convertMap(String pSql, Map<String, Object> paramMap);

}

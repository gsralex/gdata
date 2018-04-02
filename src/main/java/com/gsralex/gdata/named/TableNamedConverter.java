package com.gsralex.gdata.named;

import com.gsralex.gdata.mapper.Mapper;
import com.gsralex.gdata.mapper.MapperHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class TableNamedConverter implements NamedConverter {

    private static final Pattern PATTERN = Pattern.compile("(?<table>\\#(table))", Pattern.CASE_INSENSITIVE);

    @Override
    public <T> String convert(String namedSql, Class<T> type) {
        Matcher matcher = PATTERN.matcher(namedSql);
        Mapper mapper = MapperHolder.getMapperCache(type);
        if (matcher.find()) {
            return matcher.replaceAll(mapper.getTableName());
        }
        return namedSql;
    }
}

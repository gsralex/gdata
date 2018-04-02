package com.gsralex.gdata.named;

import com.gsralex.gdata.mapper.FieldColumn;
import com.gsralex.gdata.mapper.Mapper;
import com.gsralex.gdata.mapper.MapperHolder;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class ColumnNamedConverter implements NamedConverter {
    @Override
    public <T> String convert(String namedSql, Class<T> type) {
        String sql = namedSql;
        Mapper mapper = MapperHolder.getMapperCache(type);
        for (Map.Entry<String, FieldColumn> item : mapper.getMapper().entrySet()) {
            String regex = String.format("(?<column>\\#(%s))", item.getKey().toLowerCase());
            Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll("`" + item.getValue().getLabel() + "`");
            }
        }
        return sql;
    }
}

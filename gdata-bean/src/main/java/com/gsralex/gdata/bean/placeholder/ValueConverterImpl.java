package com.gsralex.gdata.bean.placeholder;

import com.gsralex.gdata.bean.mapper.Mapper;
import com.gsralex.gdata.bean.mapper.FieldColumn;
import com.gsralex.gdata.bean.mapper.FieldValue;
import com.gsralex.gdata.bean.mapper.MapperHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gsralex
 * @version 2018/4/2
 */
public class ValueConverterImpl implements ValueConverter {
    @Override
    public <T> SqlObject convert(String namedSql, Class<T> type, T t, Object[] objects) {
        List<Object> objectsList = new ArrayList<>();
        for (Object object : objects) {
            objectsList.add(object);
        }
        String sql = namedSql;
        FieldValue fieldValue = new FieldValue(t);
        Mapper mapper = MapperHolder.getMapperCache(type);
        for (Map.Entry<String, FieldColumn> item : mapper.getMapper().entrySet()) {
            String regex = String.format("(?<column>\\:%s)", item.getKey().toLowerCase());
            Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(sql);
            if (matcher.find()) {
                int cnt = countStr(sql, "?", matcher.start());
                sql = matcher.replaceFirst("?");
                Class fieldType = item.getValue().getType();
                String fieldName = item.getKey();
                objectsList.add(cnt, fieldValue.getValue(fieldType, fieldName));
            }
        }
        SqlObject sqlObject = new SqlObject();
        sqlObject.setSql(sql);
        Object[] result = new Object[objectsList.size()];
        objectsList.toArray(result);
        sqlObject.setObjects(result);
        return sqlObject;
    }


    private static int countStr(String str, String matchStr, int endIndex) {
        String subStr = str.substring(0, endIndex);
        StringTokenizer tokenizer = new StringTokenizer(subStr, matchStr);
        return tokenizer.countTokens();
    }
}

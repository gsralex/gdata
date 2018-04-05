package com.gsralex.gdata.bean.placeholder;


/**
 * @author gsralex
 * @version 2018/4/5
 */
public class PlaceHolderHandler {


    private static final PConverter tableConverter = new TablePConverter();
    private static final PConverter columnConverter = new ColumnPConverter();
    private static final ValueConverter valueConverter = new ValueConverterImpl();


    public static SqlObject convert(String plhSql, Object obj, Object[] objects) {
        if (obj == null) {
            SqlObject sqlObject = new SqlObject();
            sqlObject.setObjects(objects);
            sqlObject.setSql(plhSql);
        }
        Class type = obj.getClass();
        plhSql = tableConverter.convert(plhSql, type);
        plhSql = columnConverter.convert(plhSql, type);
        return valueConverter.convert(plhSql, type, obj, objects);
    }

}

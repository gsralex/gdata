package com.gsralex.gdata.bean.sqlstatement;


/**
 * @author gsralex
 * @version 2018/4/2
 */
public class SqlAlias {

    private static final String DB_MYSQL = "mysql";
    private static final String DB_SQLSERVER = "sql server";
    private static final String DB_ORACLE = "oracle";

    private static final String ALIAS_MYSQL = "`%s`";
    private static final String ALIAS_SQLSERVER = "[%s]";
    private static final String ALIAS_ORACLE = "\"%s\"";
    private static final String ALIAS_DEFAULT = "%s";

    public static String getAliasFormat(String databaseProductName) {
        if (databaseProductName == null) {
            databaseProductName = "";
        }
        databaseProductName = databaseProductName.toLowerCase().trim();
        switch (databaseProductName) {
            case DB_MYSQL: {
                return ALIAS_MYSQL;
            }
            case DB_SQLSERVER: {
                return ALIAS_SQLSERVER;
            }
            case DB_ORACLE: {
                return ALIAS_ORACLE;
            }
            default: {
                return ALIAS_DEFAULT;
            }
        }
    }
}

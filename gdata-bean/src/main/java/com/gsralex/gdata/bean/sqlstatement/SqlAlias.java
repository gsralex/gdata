package com.gsralex.gdata.bean.sqlstatement;


/**
 * @author gsralex
 * @version 2018/4/2
 */
public class SqlAlias {

    private static final String ALIAS_MYSQL = "`%s`";
    private static final String ALIAS_SQLSERVER = "[%s]";
    private static final String ALIAS_ORACLE = "\"%s\"";
    private static final String ALIAS_DEFAULT = "%s";

    public static String getAliasFormat(String databaseProductName) {
        switch (databaseProductName) {
            case "MySQL": {
                return ALIAS_MYSQL;
            }
            case "SQL Server": {
                return ALIAS_SQLSERVER;
            }
            case "Oracle": {
                return ALIAS_ORACLE;
            }
            default: {
                return ALIAS_DEFAULT;
            }
        }
    }
}

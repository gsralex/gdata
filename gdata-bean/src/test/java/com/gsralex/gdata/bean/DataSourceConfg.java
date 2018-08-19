package com.gsralex.gdata.bean;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

/**
 * @author gsralex
 * 2018/3/10
 */
public class DataSourceConfg {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gflow?useSSL=false&useServerPrepStmts=false&rewriteBatchedStatements=true";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "123456";

    public static DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }


}

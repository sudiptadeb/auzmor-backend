package com.auzmor.util;

import com.auzmor.constants.ConfigurationConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
public class DBUtil {
    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());

    private static String jdbcUrl = ConfigurationConstants.getDatabaseUrl();
    private static String username = ConfigurationConstants.getDatabaseUser();
    private static String password = ConfigurationConstants.getDatabasePass();

    public static Connection getConnection() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}

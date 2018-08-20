package com.auzmor.constants;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
public class ConfigurationConstants {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationConstants.class.getName());

    private static Properties configProperties = new Properties();

    public static void load(ServletContext servletContext) throws IOException
    {
        InputStream inputStream = servletContext.getResourceAsStream(StringConstants.WEB_INF_CONFIG_PROPERTIES);
        configProperties.load(inputStream);
    }

    public static Properties getConfigProperties(){
        return configProperties;
    }

    public static String getDatabaseUrl(){
        return configProperties.getProperty("database.url");
    }

    public static String getDatabaseUser(){
        return configProperties.getProperty("database.user");
    }

    public static String getDatabasePass(){
        return configProperties.getProperty("database.pass");
    }

    public static String getRedisHost()
    {
        return configProperties.getProperty("redis.host");
    }

    public static int getRedisPort()
    {
        return Integer.parseInt(configProperties.getProperty("redis.port"));
    }

    public static int getTimlimitSeconds()
    {
        return Integer.parseInt(configProperties.getProperty("time.limit"));
    }

    public static int getMaxRequests()
    {
        return Integer.parseInt(configProperties.getProperty("max.requests"));
    }

    public static int getBlockTimeLimit(){
        return Integer.parseInt(configProperties.getProperty("block.time.limit"));
    }
}

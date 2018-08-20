package com.auzmor.util;

import com.auzmor.constants.ConfigurationConstants;
import com.auzmor.constants.StringConstants;
import redis.clients.jedis.Jedis;

import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
public class RedisUtil {
    private static final Logger LOGGER = Logger.getLogger(RedisUtil.class.getName());

    public static void storeWithBlockTimeLimit(String fromString, String toString)
    {
        try (Jedis jedis = getJedis())
        {
            String fromToKey = StringConstants.STOP_KEYS + fromString + ":" + toString;
            jedis.set(fromToKey, String.valueOf(System.currentTimeMillis()));
            jedis.expire(fromToKey, ConfigurationConstants.getBlockTimeLimit());
        }
    }

    public static boolean isKeyPresent(String toString, String fromString)
    {
        try (Jedis jedis = getJedis())
        {
            String toFromKey = StringConstants.STOP_KEYS + toString + ":" + fromString;
            return jedis.exists(toFromKey);
        }
    }

    public static Jedis getJedis()
    {
        return new Jedis(ConfigurationConstants.getRedisHost(), ConfigurationConstants.getRedisPort());
    }

    public static boolean isFromLimitReached(String fromString)
    {
        try (Jedis jedis = getJedis())
        {
            String fromKey = StringConstants.LIMIT_KEYS + fromString;
            if (!jedis.exists(fromKey))
            {
                jedis.set(fromKey, String.valueOf(1));
                jedis.expire(fromKey, ConfigurationConstants.getTimlimitSeconds());
                return false;
            } else if (Integer.valueOf(jedis.get(fromKey)) < ConfigurationConstants.getMaxRequests())
            {
                jedis.incr(fromKey);
                return false;
            } else
            {
                return true;
            }
        }
    }
}

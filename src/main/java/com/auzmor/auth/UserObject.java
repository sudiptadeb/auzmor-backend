package com.auzmor.auth;

import java.io.InputStream;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
public class UserObject {
    private static final Logger LOGGER = Logger.getLogger(UserObject.class.getName()); //No I18N

    private String username;
    private Integer id;

    public UserObject(String username, Integer id)
    {
        this.username = username;
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}

package com.auzmor.auth;

import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
public class AuthContext {
    private static final Logger LOGGER = Logger.getLogger(AuthContext.class.getName());

    private static ThreadLocal<UserObject> currentAccount = new ThreadLocal<UserObject>();

    public static void setCurrentAccount(UserObject userObject){
        currentAccount.set(userObject);
    }

    public static UserObject getCurrentAccount()
    {
        return currentAccount.get();
    }
}

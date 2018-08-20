package com.auzmor.util;

import com.auzmor.auth.AuthContext;
import com.auzmor.auth.UserObject;
import com.auzmor.constants.ConfigurationConstants;
import com.auzmor.constants.StringConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
class ValidationUtilTest {
    @BeforeEach
    void setUp() throws IOException
    {
        ConfigurationConstants.getConfigProperties().load(new FileInputStream(new File("src/main/webapp/WEB-INF/config.properties")));
    }
    @Test
    void checkUsernameAuthID() throws SQLException, ClassNotFoundException
    {
        assertFalse(ValidationUtil.checkUsernameAuthID("userpass", "userpass"), "false for wrong username and auth id");
        assertTrue(ValidationUtil.checkUsernameAuthID("azr1", "20S0KPNOIM"), "true for correct username and auth id");
    }

    @org.junit.jupiter.api.Test
    void checkNumberInAccount() throws JSONException, IOException, SQLException, ClassNotFoundException
    {
        AuthContext.setCurrentAccount(new UserObject("azr1",1));

        assertEquals(" parameter not found", ValidationUtil.checkNumberInAccount("asdadsasd"), "Number must be in account");
        assertEquals("", ValidationUtil.checkNumberInAccount("4924195509198"), "when number is in account");

        AuthContext.setCurrentAccount(null);
    }

    @org.junit.jupiter.api.Test
    void lengthCheck() throws JSONException, IOException
    {
        JSONObject requestParams = new JSONObject();
        requestParams.put(StringConstants.TO, "");
        requestParams.put(StringConstants.FROM, "123123123123");
        requestParams.put(StringConstants.TEXT, "Hello this is test");

        assertEquals("to is invalid", ValidationUtil.lengthCheck(requestParams), "To should of minimum 1 length");


        requestParams.put(StringConstants.TO, "12345678901234567890");
        assertEquals("to is invalid", ValidationUtil.lengthCheck(requestParams), "To should of maximum 16 length");

    }

    @org.junit.jupiter.api.Test
    void presenceCheck() throws JSONException, IOException
    {
        JSONObject requestParams = new JSONObject();
        requestParams.put(StringConstants.FROM, "123123123123");
        requestParams.put(StringConstants.TEXT, "Hello this is test");

        assertEquals("to is missing", ValidationUtil.presenceCheck(requestParams), "To should present");

    }

}
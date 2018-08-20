package com.auzmor.util;

import com.auzmor.auth.AuthContext;
import com.auzmor.auth.UserObject;
import com.auzmor.constants.StringConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 18/08/18.
 */
public class ValidationUtil {
    private static final Logger LOGGER = Logger.getLogger(ValidationUtil.class.getName()); //No I18N

    /**
     * checks if the provided username and the auth_id matched as per the data in the database
     *
     * @param username of the account
     * @param auth_id  of the account
     * @return true if both match else false
     */
    public static boolean checkUsernameAuthID(String username, String auth_id) throws SQLException, ClassNotFoundException
    {
        try (Connection conn = DBUtil.getConnection())
        {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from account WHERE username = ? AND auth_id = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, auth_id);
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next())
            {
                return false;
            } else
            {
                AuthContext.setCurrentAccount(new UserObject(rs.getString("username"), rs.getInt("id")));
                return true;
            }
        }
    }

    public static String checkNumberInAccount(String number) throws IOException, JSONException, SQLException, ClassNotFoundException
    {
        Integer accId = AuthContext.getCurrentAccount().getId();

        try (Connection conn = DBUtil.getConnection())
        {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from phone_number WHERE account_id = ? AND number=?");
            preparedStatement.setInt(1, accId);
            preparedStatement.setString(2, number);
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next())
            {
                return " parameter not found";
            } else
            {
                return "";
            }
        }
    }

    public static String lengthCheck(JSONObject requestParams) throws JSONException, IOException
    {
        List<String> paramList = new ArrayList<>(Arrays.asList(StringConstants.FROM, StringConstants.TO));

        for (String param : paramList)
        {
            int paramLength = requestParams.getString(param).length();
            if ((paramLength < 6) || (paramLength > 16))
            {
                return param + " is invalid";
            }
        }

        if ((requestParams.getString(StringConstants.TEXT).length() < 1) || (requestParams.getString(StringConstants.TEXT).length() > 120))
        {
            return StringConstants.TEXT + " is invalid";
        }

        return "";
    }

    public static String presenceCheck(JSONObject requestParams) throws IOException, JSONException
    {
        List<String> paramList = new ArrayList<>(Arrays.asList(StringConstants.FROM, StringConstants.TO, StringConstants.TEXT));

        for (String param : paramList)
        {
            if (!requestParams.has(param))
            {
                return param + " is missing";
            }
        }

        return "";
    }
}

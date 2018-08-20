package com.auzmor.util;

import com.auzmor.constants.StringConstants;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 18/08/18.
 */
public class ResponseUtil {
    private static final Logger LOGGER = Logger.getLogger(ResponseUtil.class.getName()); //No I18N

    public static void respond(HttpServletResponse response, String message, int code) throws IOException
    {
        response.setStatus(code);
        response.getWriter().write(message);
    }

    public static void unauthorized(HttpServletResponse response, String message) throws IOException
    {
        respond(response,message,403);
    }

    public static void unauthorized(HttpServletResponse response) throws IOException
    {
        unauthorized(response, "Unexpected Error occurred!");
    }

    public static void error(HttpServletResponse response,String error) throws JSONException, IOException
    {
        JSONObject responseJson = new JSONObject();
        responseJson.put(StringConstants.MESSAGE,"");
        responseJson.put(StringConstants.ERROR,error);

        respond(response,responseJson.toString(),200);

    }

    public static void success(HttpServletResponse response,String message) throws JSONException, IOException
    {
        JSONObject responseJson = new JSONObject();
        responseJson.put(StringConstants.MESSAGE,message);
        responseJson.put(StringConstants.ERROR,"");

        respond(response,responseJson.toString(),200);

    }
}

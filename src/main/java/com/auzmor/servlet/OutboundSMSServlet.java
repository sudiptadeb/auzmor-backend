package com.auzmor.servlet;

import com.auzmor.constants.StringConstants;
import com.auzmor.util.MiscUtil;
import com.auzmor.util.RedisUtil;
import com.auzmor.util.ResponseUtil;
import com.auzmor.util.ValidationUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 18/08/18.
 */
public class OutboundSMSServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StringConstants.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        JSONObject requestParams = null;

        try
        {
            String jsonString = MiscUtil.streamToString(request.getInputStream());
            jsonString = jsonString.isEmpty()?"{}":jsonString;
            requestParams = new JSONObject(jsonString);

            String presenceCheckError = ValidationUtil.presenceCheck(requestParams);
            if (!presenceCheckError.isEmpty())
            {
                ResponseUtil.error(response,presenceCheckError);
                return;
            }

            String lengthCheckError = ValidationUtil.lengthCheck(requestParams);
            if (!lengthCheckError.isEmpty())
            {
                ResponseUtil.error(response,lengthCheckError);
                return;
            }

            String fromString = requestParams.getString(StringConstants.FROM);
            String toString = requestParams.getString(StringConstants.TO);

            String accountCheckError = ValidationUtil.checkNumberInAccount(fromString);
            if (!accountCheckError.isEmpty())
            {
                ResponseUtil.error(response,StringConstants.FROM+accountCheckError);
                return;
            }

            if(RedisUtil.isKeyPresent(toString, fromString)){
                ResponseUtil.error(response,"sms from "+fromString+" to "+toString+" blocked by STOP request");
                return;
            }

            if(RedisUtil.isFromLimitReached(fromString)){
                ResponseUtil.error(response,"limit reached for from "+fromString);
                return;
            }

            ResponseUtil.success(response,"outbound sms ok");

        }
        catch (JSONException | SQLException | ClassNotFoundException je)
        {
            try
            {
                ResponseUtil.error(response, "unknown failure");
            }
            catch (
                    JSONException e)
            {
                throw new ServletException("unknown failure", e);
            }
        }

    }


}
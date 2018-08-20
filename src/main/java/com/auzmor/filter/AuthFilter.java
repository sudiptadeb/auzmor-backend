package com.auzmor.filter;

import com.auzmor.constants.StringConstants;
import com.auzmor.util.MiscUtil;
import com.auzmor.util.ResponseUtil;
import com.auzmor.util.ValidationUtil;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 18/08/18.
 */
public class AuthFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(StringConstants.class.getName());

    public void destroy()
    {
        //Nothing is required here as of now. Because we are not storing or opening resources here in filter.
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //URI Check
        String requestURI = request.getRequestURI().substring(request.getContextPath().length());
        if(!requestURI.equals("/inbound/sms") && !requestURI.equals("/outbound/sms")){
            ResponseUtil.respond(response,"Not Found",404);
            return;
        }

        //Method Check
        if(!MiscUtil.caseInsensitiveMatch(request.getMethod(),StringConstants.POST)){
            ResponseUtil.respond(response,"Method not allowed",405);
            return;
        }


        //Auth Check
        String authHeader = request.getHeader(StringConstants.AUTHORIZATION);

        if (authHeader != null)
        {
            StringTokenizer tokens = new StringTokenizer(authHeader); //Separating the Basic and the base64 parts
            if (tokens.hasMoreTokens())
            {
                String basic = tokens.nextToken();

                if (basic.equalsIgnoreCase(StringConstants.BASIC))
                {
                    try
                    {
                        String credentialsBase64 = tokens.nextToken();
                        String credentials = new String(Base64.decodeBase64(credentialsBase64), StandardCharsets.UTF_8);
                        int colonIndex = credentials.indexOf(StringConstants.COLON);
                        if (colonIndex != -1)
                        {
                            String username = credentials.substring(0, colonIndex).trim();
                            String auth_id = credentials.substring(colonIndex + 1).trim();

                            if (!ValidationUtil.checkUsernameAuthID(username, auth_id))
                            {
                                ResponseUtil.unauthorized(response, "Wrong Credentials");
                            } else
                            {
                                chain.doFilter(req, resp);
                            }
                        } else
                        {
                            ResponseUtil.unauthorized(response, "Invalid authentication token");
                        }
                    }
                    catch (UnsupportedEncodingException | SQLException e)
                    {
                        ResponseUtil.unauthorized(response, "Invalid authentication token");
                    }
                    catch (ClassNotFoundException e)
                    {
                        ResponseUtil.unauthorized(response, "unknown failure");
                    }
                }
            }
        } else
        {
            ResponseUtil.unauthorized(response,"Authorization header missing");
        }
    }

    public void init(javax.servlet.FilterConfig config) throws javax.servlet.ServletException
    {
        //No init code as of now
    }

}

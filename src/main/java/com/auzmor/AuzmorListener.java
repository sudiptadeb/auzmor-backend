package com.auzmor; /**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */

import com.auzmor.constants.ConfigurationConstants;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.IOException;

public class AuzmorListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public AuzmorListener()
    {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce)
    {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        try
        {
            ConfigurationConstants.load(sce.getServletContext());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Cannot load config.properties");
        }

    }

    public void contextDestroyed(ServletContextEvent sce)
    {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se)
    {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se)
    {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe)
    {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe)
    {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe)
    {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
}

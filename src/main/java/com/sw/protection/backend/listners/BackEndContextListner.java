package com.sw.protection.backend.listners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.sw.protection.backend.config.SharedInMemoryData;

/**
 * Context listener which is going to initiate the database configuration
 * 
 * @author dinuka
 */
public class BackEndContextListner implements ServletContextListener {
    public static boolean isLocalDeployment = true;
    Logger log = Logger.getLogger(BackEndContextListner.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
	ServletContext ctx = sce.getServletContext();
	if (log.isDebugEnabled()) {
	    log.debug("Initalizing the basic configurations");
	}
	isLocalDeployment = Boolean.valueOf(ctx.getInitParameter("local-deployment"));
	SharedInMemoryData.getInstance();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}

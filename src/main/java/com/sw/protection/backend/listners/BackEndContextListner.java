/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.listners;

import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.dao.impl.AdminDAOImpl;
import com.sw.protection.backend.entity.Admin;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context listener which is going to initiate the database configuration
 * @author dinuka
 */
public class BackEndContextListner implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        HibernateUtil.setHost(ctx.getInitParameter("db-host"));
        HibernateUtil.setPort(ctx.getInitParameter("db-port"));
        HibernateUtil.setUsername(ctx.getInitParameter("db-user"));
        HibernateUtil.setPassword(ctx.getInitParameter("db-pw"));
        HibernateUtil.setDbname(ctx.getInitParameter("db-name"));
        HibernateUtil.init();
        
        System.out.println("saveAdmin");
        Admin admin = new Admin();
        admin.setUser_name("dinuka");
        admin.setPass_word("test");
        AdminDAO instance = new AdminDAOImpl();
        instance.saveAdmin(admin);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sw.protection.backend.config;

import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.User;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author SIT
 */
public class HibernateUtil {
     private static final SessionFactory sessionFactory;
    
    static {
        Logger log = Logger.getLogger(HibernateUtil.class.getName());
        try {
            Properties prop = new Properties();
            
            //Access the property file form root
            prop.load(new FileInputStream("db.properties"));
          //  System.out.println(prop.getProperty("version"));

            AnnotationConfiguration cnf = new AnnotationConfiguration();
            cnf.addAnnotatedClass(Admin.class);
            cnf.addAnnotatedClass(Company.class);
            cnf.addAnnotatedClass(CompanyClient.class);
            cnf.addAnnotatedClass(CompanySW.class);
            cnf.addAnnotatedClass(CompanySWCopy.class);
            cnf.addAnnotatedClass(User.class);            
            cnf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            cnf.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            cnf.setProperty("hibernate.connection.url", "jdbc:mysql://" + prop.getProperty("host") + ":" + prop.getProperty("port") + "/" + prop.getProperty("dbname"));
            cnf.setProperty("hibernate.connection.username", prop.getProperty("username"));
            cnf.setProperty("hibernate.connection.password", prop.getProperty("pw"));
            cnf.setProperty("hibernate.connection.pool_size", "1");
            cnf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            cnf.setProperty("hibernate.current_session_context_class", "thread");
            cnf.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
            cnf.setProperty("hibernate.show_sql", "true");
            cnf.setProperty("hibernate.hbm2ddl.auto", "update");

            // sessionFactory = new AnnotationConfiguration().configure("/hibernate.cfg.xml").buildSessionFactory();
            sessionFactory = cnf.buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception.
            log.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

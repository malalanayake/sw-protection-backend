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
import java.io.InputStream;
import java.util.Properties;
import javax.resource.spi.AuthenticationMechanism;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author SIT
 */
public class HibernateUtil {

    private static String host;
    private static String port;
    private static String username;
    private static String password;  
    private static String dbname;
    
    private static SessionFactory sessionFactory = null;
    
    public static void init() {
        Logger log = Logger.getLogger(HibernateUtil.class.getName());
        try {
            //Properties prop = new Properties();
            //System.out.println(""+HibernateUtil.class.getClassLoader().getResource("").getPath());
            //Access the property file form root
            //prop.load(new FileInputStream("/WEB-INF/db.properties"));
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
            cnf.setProperty("hibernate.connection.url", "jdbc:mysql://" + host +":" + port + "/" +dbname );
            cnf.setProperty("hibernate.connection.username", username);
            cnf.setProperty("hibernate.connection.password", password);
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

    public static String getDbname() {
        return dbname;
    }

    public static void setDbname(String dbname) {
        HibernateUtil.dbname = dbname;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        HibernateUtil.host = host;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        HibernateUtil.port = port;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        HibernateUtil.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        HibernateUtil.password = password;
    }
    
    
}

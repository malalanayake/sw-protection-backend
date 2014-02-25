package com.sw.protection.backend.config;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;
import com.sw.protection.backend.entity.SuperAdmin;
import com.sw.protection.backend.entity.Trace;
import com.sw.protection.backend.entity.Usage;
import com.sw.protection.backend.listners.BackEndContextListner;

/**
 * This is contain hibernate configurations.
 * 
 * @author dinuka
 */
public class HibernateUtil {

    private static String host;
    private static String port;
    private static String username;
    private static String password;
    private static String dbname;

    private static SessionFactory sessionFactory = null;

    /**
     * Initializing database configuration.
     */
    public static void init() {
	Logger log = Logger.getLogger(HibernateUtil.class.getName());
	try {

	    Configuration cnf = new Configuration();
	    cnf.addAnnotatedClass(Admin.class);
	    cnf.addAnnotatedClass(AdminScope.class);
	    cnf.addAnnotatedClass(Company.class);
	    cnf.addAnnotatedClass(CompanyClient.class);
	    cnf.addAnnotatedClass(CompanySW.class);
	    cnf.addAnnotatedClass(CompanySWCopy.class);
	    cnf.addAnnotatedClass(CompanyUser.class);
	    cnf.addAnnotatedClass(SuperAdmin.class);
	    cnf.addAnnotatedClass(CompanyUserScope.class);
	    cnf.addAnnotatedClass(Usage.class);
	    cnf.addAnnotatedClass(Trace.class);
	    cnf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	    cnf.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");

	    if (BackEndContextListner.isLocalDeployment) {
		cnf.setProperty("hibernate.connection.url", "jdbc:mysql://" + host + ":" + port + "/" + dbname);
		cnf.setProperty("hibernate.connection.username", username);
		cnf.setProperty("hibernate.connection.password", password);
	    } else {
		// Connect to RH Cloud DB
		cnf.setProperty("hibernate.connection.url", "jdbc:mysql://" + System.getenv("OPENSHIFT_MYSQL_DB_HOST")
			+ ":" + System.getenv("OPENSHIFT_MYSQL_DB_PORT") + "/sw");
		cnf.setProperty("hibernate.connection.username", System.getenv("OPENSHIFT_MYSQL_DB_USERNAME"));
		cnf.setProperty("hibernate.connection.password", System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD"));

	    }

	    // Connect to jelastic
	    // cnf.setProperty("hibernate.connection.url","jdbc:mysql://mysql-dinuka.jelastic.servint.net/sw");
	    // cnf.setProperty("hibernate.connection.username", "root");
	    // cnf.setProperty("hibernate.connection.password", "w6zEzxKm1F");

	    cnf.setProperty("hibernate.connection.pool_size", "1");
	    cnf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	    cnf.setProperty("hibernate.current_session_context_class", "thread");
	    cnf.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
	    cnf.setProperty("hibernate.show_sql", "true");
	    cnf.setProperty("hibernate.hbm2ddl.auto", "update");

	    // sessionFactory = new
	    // AnnotationConfiguration().configure("/hibernate.cfg.xml").buildSessionFactory();
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

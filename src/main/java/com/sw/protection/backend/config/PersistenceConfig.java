package com.sw.protection.backend.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring configuration class
 * 
 * @author dinuka
 * 
 */
@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:database.properties" })
@ComponentScan({ "com.sw.protection.backend" })
public class PersistenceConfig {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	sessionFactory.setDataSource(restDataSource());
	sessionFactory.setPackagesToScan(new String[] { "com.sw.protection.backend.entity" });
	sessionFactory.setHibernateProperties(hibernateProperties());

	return sessionFactory;
    }

    @Bean
    public DataSource restDataSource() {
	BasicDataSource dataSource = new BasicDataSource();
	dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	dataSource.setUrl(env.getProperty("jdbc.url"));
	dataSource.setUsername(env.getProperty("jdbc.user"));
	dataSource.setPassword(env.getProperty("jdbc.pass"));

	return dataSource;
    }

    Properties hibernateProperties() {
	return new Properties() {
	    {
		setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		setProperty("hibernate.globally_quoted_identifiers", "true");
		setProperty("hibernate.current_session_context_class", "thread");
		setProperty("hibernate.connection.pool_size", "100");
		setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		setProperty("hibernate.connection.release_mode", "after_transaction");
	    }
	};
    }
}

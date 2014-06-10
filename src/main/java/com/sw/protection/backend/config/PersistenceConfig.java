package com.sw.protection.backend.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

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
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {

			dataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
			dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
			dataSource.setUser(env.getProperty("jdbc.user"));
			dataSource.setPassword(env.getProperty("jdbc.pass"));

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
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

				// Specific parameters for avoid broken pipe exception when
				// mysql refresh the connection after 8 hours
				setProperty("hibernate.c3p0.acquire_increment", "3");
				setProperty("hibernate.c3p0.idle_test_period", "14400");
				setProperty("hibernate.c3p0.timeout", "25200");
				setProperty("hibernate.c3p0.max_siz", "100");
				setProperty("hibernate.c3p0.min_size", "30");
				setProperty("hibernate.c3p0.max_statements", "0");
				setProperty("hibernate.c3p0.preferredTestQuery", "select 1;");
			}
		};
	}
}

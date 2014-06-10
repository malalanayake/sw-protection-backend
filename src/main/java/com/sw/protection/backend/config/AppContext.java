package com.sw.protection.backend.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * This class is use to get the spring application context to get the services
 * 
 * @author dinuka
 * 
 */
public class AppContext {
	public static ApplicationContext ctx = null;

	private AppContext() {

	}

	public static ApplicationContext getInstance() {
		if (ctx == null) {
			synchronized (AppContext.class) {
				if (ctx == null) {
					ctx = new AnnotationConfigApplicationContext(PersistenceConfig.class);
				}
			}
		}
		return ctx;
	}
}

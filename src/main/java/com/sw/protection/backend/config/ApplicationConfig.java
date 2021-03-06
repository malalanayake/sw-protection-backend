package com.sw.protection.backend.config;

import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Declare the REST resources
 * 
 * @author dinuka
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {
    static {
	System.out.println("LOADING ApplicationConfig");
    }

    @Override
    public Set<Class<?>> getClasses() {
	Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
	// following code can be used to customize Jersey 1.x JSON provider:
	try {
	    Class jacksonProvider = Class.forName("com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider");
	    resources.add(jacksonProvider);
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	addRestResourceClasses(resources);
	return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * re-generated by NetBeans REST support to populate given list with all
     * resources defined in the project.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
	resources.add(com.sw.protection.backend.rest.GenericResource.class);
	resources.add(com.sw.protection.backend.rest.TestResource.class);
    }

}

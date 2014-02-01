package com.sw.protection.backend.config;

import org.apache.log4j.Logger;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.sw.protection.backend.dao.impl.AdminDAOImpl;

/**
 * Provides the shared memory space for clustered environment by using HaselCast
 * 
 * @author dinuka
 * 
 */
public class SharedInMemoryData {

    public static volatile HazelcastInstance SHARED_INSTANCE;
    public static String INSTANCE_NAME = "SWBackend";

    /**
     * Interface to provide the DB_LOCKS for Map name
     * 
     * @author dinuka
     * 
     */
    public static interface DB_LOCKS {
	public static final String ADMIN_DAO = "ADMIN";
    }

    public static final Logger log = Logger.getLogger(SharedInMemoryData.class.getName());

    private void SharedInMemoryData() {

    }

    // static method to get instance
    public static HazelcastInstance getInstance() {
	if (SHARED_INSTANCE == null) { // first time lock
	    synchronized (SharedInMemoryData.class) {
		if (SHARED_INSTANCE == null) { // second time lock
		    Config config = new Config();
		    config.setInstanceName(INSTANCE_NAME);
		    SHARED_INSTANCE = Hazelcast.newHazelcastInstance(config);
		    if (log.isDebugEnabled()) {
			log.debug("Shared Memory Hazelcast instance initiated with INSTANCE_NAME" + INSTANCE_NAME);
		    }
		}
	    }
	}

	return SHARED_INSTANCE;
    }
}

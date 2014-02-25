package com.sw.protection.backend.config;

import org.apache.log4j.Logger;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.sw.protection.backend.listners.BackEndContextListner;

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
	public static final String ADMIN_SCOPE_DAO = "ADMIN_SCOPE";
	public static final String COMPANY_USER_DAO = "COMPANY_USER";
	public static final String SUPER_ADMIN_DAO = "SUPER_ADMIN";
	public static final String COMPANY_DAO = "COMPANY";
    }

    public static final Logger log = Logger.getLogger(SharedInMemoryData.class.getName());

    private void SharedInMemoryData() {

    }

    // static method to get instance
    public static HazelcastInstance getInstance() {
	if (SHARED_INSTANCE == null) {
	    synchronized (SharedInMemoryData.class) {
		if (SHARED_INSTANCE == null) {
		    Config config = new Config();
		    if (BackEndContextListner.isLocalDeployment) {
			// Local Configuration
			config.setInstanceName(INSTANCE_NAME);
			config.getGroupConfig().setName("app1");
		    } else {
			// Openshift specific configuration
			// config.setInstanceName(INSTANCE_NAME);
			config.getGroupConfig().setName("app1");
			// config.setProperty("hazelcast.initial.min.cluster.size","2");
			config.setProperty("hazelcast.socket.bind.any", "false");
			config.setProperty("hazelcast.local.localAddress", System.getenv("OPENSHIFT_JBOSSEWS_IP"));
			NetworkConfig networkConfig = new NetworkConfig();
			networkConfig.setPort(15000);
			networkConfig.setPortAutoIncrement(false);

			JoinConfig join = networkConfig.getJoin();
			join.getAwsConfig().setEnabled(false);
			join.getMulticastConfig().setEnabled(false);
			TcpIpConfig tcpIpConfig = new TcpIpConfig();
			tcpIpConfig.setEnabled(true);

			tcpIpConfig.setConnectionTimeoutSeconds(120);

			join.setTcpIpConfig(tcpIpConfig);
			networkConfig.setJoin(join);
			networkConfig.getInterfaces().setEnabled(false).addInterface("OPENSHIFT_JBOSSEWS_IP");
			// .addInterface("127.13.21.129");
			config.setNetworkConfig(networkConfig);
		    }

		    SHARED_INSTANCE = Hazelcast.newHazelcastInstance(config);

		    if (log.isDebugEnabled()) {
			log.debug("Shared Memory Hazelcast instance initiated with INSTANCE_NAME " + INSTANCE_NAME);
		    }
		}
	    }
	}

	return SHARED_INSTANCE;
    }
}

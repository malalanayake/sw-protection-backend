package com.sw.protection.backend.config;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
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
		     //config.getGroupConfig().setName("app1");
		     //config.setProperty("hazelcast.initial.min.cluster.size","2");
		     config.setProperty("hazelcast.socket.bind.any", "false");
		     config.setProperty("hazelcast.local.localAddress", "127.4.158.1");
		     NetworkConfig networkConfig = new NetworkConfig();
		     networkConfig.setPort(15001);
		     networkConfig.setPortAutoIncrement(false);
		     JoinConfig join = networkConfig.getJoin();
		    // MulticastConfig multy = new MulticastConfig();
		    // multy.setEnabled(true);
		    // multy.setMulticastGroup("224.2.2.3");
		    // multy.setMulticastPort(54327);
		     TcpIpConfig tcpIpConfig = new TcpIpConfig();
		     tcpIpConfig.setEnabled(true);
		     
		     join.setTcpIpConfig(tcpIpConfig);
		     networkConfig.setJoin(join);
		     config.setNetworkConfig(networkConfig);

//		    Config config = new Config();
//		    config.setInstanceName(INSTANCE_NAME);
//		    config.getGroupConfig().setName("app1");
//		    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
//		    try {
//			config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true).addMember(
//				Inet4Address.getLocalHost().getHostAddress());
//		    } catch (UnknownHostException e) {
//			log.debug("Problem===========");
//			e.printStackTrace();
//		    }
		    
		    
		    // config.getNetworkConfig().getInterfaces().setEnabled(true).addInterface("192.168.3.*");
		    // config.setProperty("hazelcast.initial.min.cluster.size","2");
		    //		   

		    // ManagementCenterConfig managementCenterConfig = new
		    // ManagementCenterConfig();
		    // managementCenterConfig.setEnabled(true);
		    // managementCenterConfig.setUrl("http://dinuka.jelastic.servint.net/man");
		    // config.setManagementCenterConfig(managementCenterConfig);
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

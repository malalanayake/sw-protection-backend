package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.Types;
import com.sw.protection.backend.dao.UsageDAO;
import com.sw.protection.backend.entity.Trace;
import com.sw.protection.backend.entity.UsageData;

public class UsageDAOImpl implements UsageDAO {

    private Session session;
    public static final Logger log = Logger.getLogger(UsageDAOImpl.class.getName());

    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.USAGE_DAO);

    @Override
    public List<UsageData> getAllUsagesByTypeAndID(Types type, Long id) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<UsageData> usageAll = session.getNamedQuery(UsageData.Constants.NAME_QUERY_FIND_ALL_BY_TYPE_AND_ID)
		    .setParameter(UsageData.Constants.PARAM_USAGE_TYPE_NAME, type)
		    .setParameter(UsageData.Constants.PARAM_USAGE_TYPE_ID, id).list();
	    tr.commit();

	    if (usageAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Usages are not exist for Type: " + type + " and ID : " + id);
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + usageAll.size() + " Usage for Type: " + type + " and ID: " + id);
		}
		return usageAll;
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    // TODO: Throw exception
	    return null;
	}
    }

    @Override
    public List<UsageData> getAllUsagesByAPIName(String apiName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<UsageData> usageAll = session.getNamedQuery(UsageData.Constants.NAME_QUERY_FIND_ALL_BY_API_NAME)
		    .setParameter(UsageData.Constants.PARAM_USAGE_API_NAME, apiName).list();
	    tr.commit();

	    if (usageAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Usages are not exist for Api name: " + apiName);
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + usageAll.size() + " Usages for Api name: " + apiName);
		}
		return usageAll;
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    // TODO: Throw exception
	    return null;
	}
    }

    @Override
    public UsageData getUsage(Long ID) {
	session = HibernateUtil.getSessionFactory().openSession();
	try {
	    UsageData usage = new UsageData();
	    usage = (UsageData) session.get(UsageData.class, ID);
	    if (log.isDebugEnabled()) {
		log.debug("Loaded Usage " + usage.toString());
	    }
	    return usage;
	} catch (RuntimeException ex) {
	    log.error(ex);
	    // TODO: Throw exception
	    return null;
	}
    }

    @Override
    public void updateUsage(UsageData usage) {
	Transaction tr = null;
	try {
	    // Lock by usage ID
	    LOCK_MAP.lock(usage.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Usage ID " + usage.getId());
	    }
	    
	    // check last modification
	    if (getUsage(usage.getId()) != null) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		usage.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(usage);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Usage" + usage.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Usage data is not exist " + usage.toString());
		}
		// TODO:Create Exception
	    }

	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    // TODO: capture the exception
	} finally {
	    if (log.isDebugEnabled()) {
		log.debug("Releasing LOCK by Usage ID " + usage.getId());
	    }
	    // Unlock the lock by usage ID
	    LOCK_MAP.unlock(usage.getId());
	    // TODO: throw the captured exception
	}

    }

    @Override
    public void deleteUsage(UsageData usage) {
	Transaction tr = null;
	try {
	    // Lock by trace ID
	    LOCK_MAP.lock(usage.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Usage ID " + usage.getId());
	    }

	    // check whether the recode is exist
	    if (getUsage(usage.getId()) != null) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(usage);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Usage" + usage.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Usage is not exist " + usage.toString());
		}
		// TODO:Create Exception
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    // TODO: Throw exception
	} finally {
	    if (log.isDebugEnabled()) {
		log.debug("Releasing LOCK by Usage ID " + usage.getId());
	    }
	    // Unlock the lock by usage ID
	    LOCK_MAP.unlock(usage.getId());
	    // TODO: throw the captured exception
	}

    }

    @Override
    public void saveUsage(UsageData usage) {
	Transaction tr = null;
	try {
	    session = HibernateUtil.getSessionFactory().getCurrentSession();
	    tr = session.beginTransaction();
	    session.save(usage);
	    tr.commit();
	    if (log.isDebugEnabled()) {
		log.debug("Save Usage " + usage.toString());
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    // TODO: Throw exception
	}

    }

}

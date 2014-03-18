package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.UsageDAO;
import com.sw.protection.backend.entity.UsageData;

/**
 * Usage tacking operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class UsageDAOImpl implements UsageDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public static final Logger log = Logger.getLogger(UsageDAOImpl.class.getName());

    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.USAGE_DAO);

    @Override
    public List<UsageData> getAllUsagesByTypeAndID(ObjectType type, Long id) {
	Session session = sessionFactory.getCurrentSession();
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
	    return null;
	}
    }

    @Override
    public List<UsageData> getAllUsagesByAPIName(String apiName) {
	Session session = sessionFactory.getCurrentSession();
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
	    return null;
	}
    }

    @Override
    public UsageData getUsage(Long ID) {
	Session session = sessionFactory.openSession();
	try {
	    UsageData usage = new UsageData();
	    usage = (UsageData) session.get(UsageData.class, ID);
	    if (log.isDebugEnabled()) {
		log.debug("Loaded Usage " + usage.toString());
	    }
	    return usage;
	} catch (RuntimeException ex) {
	    log.error(ex);
	    return null;
	}
    }

    @Override
    public void updateUsage(UsageData usage) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by usage ID
	    LOCK_MAP.lock(usage.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Usage ID " + usage.getId());
	    }

	    // check last modification
	    if (getUsage(usage.getId()).getLast_modified().equals(usage.getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		usage.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(usage);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Usage" + usage.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Usage data is already modified " + usage.toString());
		}
		// create custom RecordAlreadyModifiedException
		recordAlreadyModifiedException = new RecordAlreadyModifiedException();
	    }

	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
		// create custom OperationRollBackException
		operationRollBackException = new OperationRollBackException(ex);
	    }
	} finally {
	    if (log.isDebugEnabled()) {
		log.debug("Releasing LOCK by Usage ID " + usage.getId());
	    }
	    // Unlock the lock by usage ID
	    LOCK_MAP.unlock(usage.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}

    }

    @Override
    public void deleteUsage(UsageData usage) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by trace ID
	    LOCK_MAP.lock(usage.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Usage ID " + usage.getId());
	    }

	    // check whether the recode is exist
	    if (getUsage(usage.getId()).getLast_modified().equals(usage.getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		session.delete(usage);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Usage" + usage.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Usage data is already modified" + usage.toString());
		}
		// create custom RecordAlreadyModifiedException
		recordAlreadyModifiedException = new RecordAlreadyModifiedException();
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
		// create custom OperationRollBackException
		operationRollBackException = new OperationRollBackException(ex);
	    }
	} finally {
	    if (log.isDebugEnabled()) {
		log.debug("Releasing LOCK by Usage ID " + usage.getId());
	    }
	    // Unlock the lock by usage ID
	    LOCK_MAP.unlock(usage.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}

    }

    @Override
    public UsageData saveUsage(UsageData usage) throws DuplicateRecordException, OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	UsageData usageDataReturn = null;
	try {
	    // check whether the usage data is there exist
	    if (!this.isUsageDataExist(usage)) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		session.save(usage);
		usageDataReturn = usage;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Usage " + usage.toString());
		}
	    } else {
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
		// create custom OperationRollBackException
		operationRollBackException = new OperationRollBackException(ex);
	    }
	} finally {
	    // throw the captured exceptions
	    if (duplicateRecordException != null) {
		throw duplicateRecordException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}

	return usageDataReturn;

    }

    @Override
    public boolean isUsageDataExist(UsageData usage) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;

	try {
	    tr = session.beginTransaction();
	    List<UsageData> usageAll = session.getNamedQuery(UsageData.Constants.NAME_QUERY_IS_RECORD_ALREADY_EXIST)
		    .setParameter(UsageData.Constants.PARAM_USAGE_API_NAME, usage.getApi_name())
		    .setParameter(UsageData.Constants.PARAM_USAGE_API_OPERATION, usage.getOperation())
		    .setParameter(UsageData.Constants.PARAM_USAGE_TYPE_NAME, usage.getType())
		    .setParameter(UsageData.Constants.PARAM_USAGE_TYPE_ID, usage.getType_id()).list();

	    tr.commit();
	    if (usageAll != null) {
		if (usageAll.size() == 0) {
		    if (log.isDebugEnabled()) {
			log.debug("Usage data not found :" + usage.toString());
		    }
		    return false;
		} else {
		    if (log.isDebugEnabled()) {
			log.debug("Usage data found :" + usage.toString());
		    }
		    return true;
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Usage data not found :" + usage.toString());
		}
		return false;
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    return false;
	}

    }

}

package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.CompanyClientDAO;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;

/**
 * Company Client operation implementation
 * 
 * @author dinuka
 * 
 */
public class CompanyClientDAOImpl implements CompanyClientDAO {
    private Session session;
    public static final Logger log = Logger.getLogger(CompanyClientDAOImpl.class.getName());

    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_CLIENT_DAO);

    @Override
    public List<CompanyClient> getAllCompanyClients() {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanyClient> companyClientAll = session.getNamedQuery(
		    CompanyClient.Constants.NAME_QUERY_FIND_COMPANY_CLIENT_ALL).list();
	    tr.commit();

	    if (companyClientAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company clients are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companyClientAll.size() + " Company clients");
		}
		return companyClientAll;
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
    public CompanyClient getCompanyClient(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanyClient> companyClientAll = session
		    .getNamedQuery(CompanyClient.Constants.NAME_QUERY_FIND_BY_COMPANY_CLIENT_USER_NAME)
		    .setParameter(CompanyClient.Constants.PARAM_COMPANY_CLIENT_USER_NAME, userName).list();
	    tr.commit();

	    if (companyClientAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company client's username: " + userName + " is not found");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company client's username: " + userName + " found");
		}
		return companyClientAll.get(0);
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
    public void updateCompanyClient(CompanyClient companyClient) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by company client ID
	    LOCK_MAP.lock(companyClient.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Client ID " + companyClient.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (companyClient.getLast_modified().equals(
		    this.getCompanyClient(companyClient.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		companyClient.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(companyClient);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Company client " + companyClient.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company client " + companyClient.toString()
			    + " so cannot update");
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
		log.debug("Releasing LOCK by Client ID " + companyClient.getId());
	    }
	    // Unlock the lock by company client ID
	    LOCK_MAP.unlock(companyClient.getId());

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
    public void deleteCompanyClient(CompanyClient companyClient) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by company client ID
	    LOCK_MAP.lock(companyClient.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Company client ID " + companyClient.getId());
	    }

	    // check last modification
	    if (companyClient.getLast_modified().equals(
		    this.getCompanyClient(companyClient.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(companyClient);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Company client " + companyClient.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company client " + companyClient.toString()
			    + " so cannot delete");
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
		log.debug("Releasing LOCK by Company client ID " + companyClient.getId());
	    }
	    // Unlock the lock by company user ID
	    LOCK_MAP.unlock(companyClient.getId());

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
    public CompanyClient saveCompanyClient(CompanyClient companyClient) throws DuplicateRecordException,
	    OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	CompanyClient companyClientReturn = null;
	try {
	    // check whether the company user name already exist
	    if (this.isCompanyClientUserNameExist(companyClient.getUser_name())) {
		if (log.isDebugEnabled()) {
		    log.debug("Company client username :" + companyClient.toString() + " already exist");
		}
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
	    } else {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		String dateTime = Formatters.formatDate(new Date());
		companyClient.setDate_time(dateTime);
		companyClient.setLast_modified(dateTime);
		// set last modified data if scope is not null
		if (companyClient.getCompanySWCopySet() != null) {
		    for (CompanySWCopy companySWCopy : companyClient.getCompanySWCopySet()) {
			companySWCopy.setLast_modified(dateTime);
		    }
		}
		session.save(companyClient);
		companyClientReturn = companyClient;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Company client " + companyClient.toString());
		}
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

	return companyClientReturn;
    }

    @Override
    public boolean isCompanyClientUserNameExist(String userName) {
	if (this.getCompanyClient(userName) == null) {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company client " + userName + " Exist: False");
	    }
	    return false;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company client " + userName + " Exist: True");
	    }
	    return true;
	}
    }

    @Override
    public CompanyClient loadAllPropertiesOfCompanyClient(Long id) {
	session = HibernateUtil.getSessionFactory().openSession();
	try {
	    CompanyClient companyClient = new CompanyClient();
	    companyClient = (CompanyClient) session.get(CompanyUser.class, id);
	    if (log.isDebugEnabled()) {
		log.debug("Loaded Company client " + companyClient.toString());
	    }
	    return companyClient;
	} catch (RuntimeException ex) {
	    log.error(ex);
	    return null;
	}
    }

}

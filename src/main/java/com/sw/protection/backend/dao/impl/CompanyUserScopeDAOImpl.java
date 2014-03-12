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
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.CompanyUserScopeDAO;
import com.sw.protection.backend.entity.CompanyUserScope;

/**
 * Company User Scope operation implementation
 * 
 * @author dinuka
 * 
 */
public class CompanyUserScopeDAOImpl implements CompanyUserScopeDAO {

    private Session session;
    public static final Logger log = Logger.getLogger(CompanyUserScopeDAOImpl.class.getName());

    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_USER_SCOPE_DAO);

    @Override
    public List<CompanyUserScope> getAllCompanyUserScopes(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanyUserScope> companyUserScopes = session
		    .getNamedQuery(CompanyUserScope.Constants.NAME_QUERY_FIND_USER_SCOPE_BY_USER_NAME)
		    .setParameter(CompanyUserScope.Constants.PARAM_USER_SCOPE_USER_NAME, userName).list();
	    tr.commit();

	    if (companyUserScopes.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company user scopes not found under company user: " + userName);
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company user " + userName + " found with company user scopes");
		}
		return companyUserScopes;
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
    public CompanyUserScope saveNewCompanyUserScope(CompanyUserScope companyUserScope) throws DuplicateRecordException,
	    OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	CompanyUserScope companyUserScopeReturn = null;
	try {
	    if (this.getCompanyUserScope(companyUserScope.getCompanyUser().getUser_name(),
		    companyUserScope.getApi_name()) == null) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		// set latest time on modification
		companyUserScope.setLast_modified(Formatters.formatDate(new Date()));
		session.save(companyUserScope);
		companyUserScopeReturn = companyUserScope;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Company user scope" + companyUserScope.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company user scope" + companyUserScope.toString() + " already exist");
		}
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

	return companyUserScopeReturn;
    }

    @Override
    public void deleteCompanyUserScope(CompanyUserScope companyUserScope) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by company user scope ID
	    LOCK_MAP.lock(companyUserScope.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Company user ID " + companyUserScope.getId());
	    }

	    // check last modification
	    if (companyUserScope.getLast_modified().equals(
		    this.getCompanyUserScope(companyUserScope.getCompanyUser().getUser_name(),
			    companyUserScope.getApi_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(companyUserScope);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Company user scope " + companyUserScope.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company user scope "
			    + companyUserScope.toString() + " so cannot delete");
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
		log.debug("Releasing LOCK by Admin ID " + companyUserScope.getId());
	    }
	    // Unlock the lock by company user scope ID
	    LOCK_MAP.unlock(companyUserScope.getId());

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
    public void updateCompanyUserScope(CompanyUserScope companyUserScope) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by company user ID
	    LOCK_MAP.lock(companyUserScope.getCompanyUser().getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by company user ID " + companyUserScope.getCompanyUser().getId());
	    }

	    // check the modification time
	    if (companyUserScope.getLast_modified().equals(
		    getCompanyUserScope(companyUserScope.getCompanyUser().getUser_name(),
			    companyUserScope.getApi_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();

		// set the latest date and time
		companyUserScope.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(companyUserScope);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update company user scope to " + companyUserScope.toString());
		}

	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest company user scope:" + companyUserScope.toString()
			    + " so this is not going to upate");
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
		log.debug("Releasing LOCK by company user ID " + companyUserScope.getCompanyUser().getId());
	    }
	    // Unlock the lock by company user ID
	    LOCK_MAP.unlock(companyUserScope.getCompanyUser().getId());

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
    public CompanyUserScope getCompanyUserScope(String userName, String apiName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanyUserScope> companyUserScopes = session
		    .getNamedQuery(CompanyUserScope.Constants.NAME_QUERY_FIND_USER_SCOPE_BY_USER_NAME_AND_API_NAME)
		    .setParameter(CompanyUserScope.Constants.PARAM_USER_SCOPE_USER_NAME, userName)
		    .setParameter(CompanyUserScope.Constants.PARAM_USER_SCOPE_API_NAME, apiName).list();
	    tr.commit();

	    if (companyUserScopes.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company user scopes not found under company user: " + userName);
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company user " + userName + " found with company user scope according to API name "
			    + apiName);
		}
		return companyUserScopes.get(0);
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
    public boolean isAccessGrantedFor(String userName, String apiName, APIOperations operation) {
	CompanyUserScope companyUserScope = null;
	companyUserScope = getCompanyUserScope(userName, apiName);
	boolean result = false;
	if (companyUserScope == null) {// User doesn't have scope recode for
				       // given API
	    if (log.isDebugEnabled()) {
		log.debug("Cmmpany user " + userName + " doesn't have scope recode for API " + apiName);
	    }
	    return result;
	}

	switch (operation) {
	case GET:
	    result = companyUserScope.isGet();
	    break;
	case POST:
	    result = companyUserScope.isPost();
	    break;
	case PUT:
	    result = companyUserScope.isPut();
	    break;
	case DELETE:
	    result = companyUserScope.isDel();
	    break;
	}

	if (log.isDebugEnabled()) {
	    log.debug("Company user " + userName + " has scope recode for API " + apiName + " " + operation + "="
		    + result);
	}
	return result;
    }

}

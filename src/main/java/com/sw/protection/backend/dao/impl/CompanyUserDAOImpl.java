package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.CompanyUserDAO;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;

/**
 * Company User operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class CompanyUserDAOImpl implements CompanyUserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public static final Logger log = Logger.getLogger(CompanyUserDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_USER_DAO);

    @Override
    public List<CompanyUser> getAllUsers() {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanyUser> companyUserAll = session.getNamedQuery(
		    CompanyUser.Constants.NAME_QUERY_FIND_COMPANY_USER_ALL).list();
	    tr.commit();

	    if (companyUserAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company users are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companyUserAll.size() + " Company users");
		}
		return companyUserAll;
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
    public CompanyUser getUser(String userName) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanyUser> companyUserAll = session
		    .getNamedQuery(CompanyUser.Constants.NAME_QUERY_FIND_BY_USER_NAME)
		    .setParameter(CompanyUser.Constants.PARAM_USER_NAME, userName).list();
	    tr.commit();

	    if (companyUserAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company user's username: " + userName + " is not found");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company user's username: " + userName + " found");
		}
		return companyUserAll.get(0);
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
    public CompanyUser updateUser(CompanyUser user) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	CompanyUser companyUserReturn = null;
	try {
	    // Lock by company user ID
	    LOCK_MAP.lock(user.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Admin ID " + user.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (user.getLast_modified().equals(this.getUser(user.getUser_name()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		user.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(user);
		tr.commit();
		companyUserReturn = user;

		if (log.isDebugEnabled()) {
		    log.debug("Update Company user " + user.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company user " + user.toString()
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
		log.debug("Releasing LOCK by Admin ID " + user.getId());
	    }
	    // Unlock the lock by company user ID
	    LOCK_MAP.unlock(user.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }

	}

	return companyUserReturn;
    }

    @Override
    public CompanyUser deleteUser(CompanyUser user) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	CompanyUser companyUserReturn = null;
	try {
	    // Lock by company user ID
	    LOCK_MAP.lock(user.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Companu user ID " + user.getId());
	    }

	    // check last modification
	    if (user.getLast_modified().equals(this.getUser(user.getUser_name()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		user = (CompanyUser) session.get(CompanyUser.class, user.getId());
		session.delete(user);
		tr.commit();
		companyUserReturn = user;
		if (log.isDebugEnabled()) {
		    log.debug("Delete Company user " + user.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company user " + user.toString()
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
		log.debug("Releasing LOCK by Company user ID " + user.getId());
	    }
	    // Unlock the lock by company user ID
	    LOCK_MAP.unlock(user.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }

	}

	return companyUserReturn;
    }

    @Override
    public CompanyUser saveUser(CompanyUser user) throws DuplicateRecordException, OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	CompanyUser companyUserReturn = null;
	try {
	    // check whether the company user name already exist
	    if (this.isCompanyUserNameExist(user.getUser_name())) {
		if (log.isDebugEnabled()) {
		    log.debug("Company user username :" + user.toString() + " already exist");
		}
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
	    } else {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		String dateTime = Formatters.formatDate(new Date());
		user.setDate_time(dateTime);
		user.setLast_modified(dateTime);
		// set last modified data if scope is not null
		if (user.getUserScopeSet() != null) {
		    for (CompanyUserScope companyUserScp : user.getUserScopeSet()) {
			companyUserScp.setLast_modified(dateTime);
		    }
		}
		session.save(user);
		tr.commit();
		companyUserReturn = user;
		if (log.isDebugEnabled()) {
		    log.debug("Save Company user " + user.toString());
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

	return companyUserReturn;
    }

    @Override
    public boolean isCompanyUserNameExist(String userName) {
	if (this.getUser(userName) == null) {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company user " + userName + " Exist: False");
	    }
	    return false;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company user " + userName + " Exist: True");
	    }
	    return true;
	}
    }

    @Override
    public CompanyUser loadAllPropertiesOfCompanyUser(Long id) {
	Session session = sessionFactory.openSession();
	try {
	    CompanyUser companyUser = new CompanyUser();
	    companyUser = (CompanyUser) session.get(CompanyUser.class, id);
	    if (log.isDebugEnabled()) {
		log.debug("Loaded Company user " + companyUser.toString());
	    }
	    return companyUser;
	} catch (RuntimeException ex) {
	    log.error(ex);
	    return null;
	}
    }

    @Override
    public boolean validateCompanyUserforSave(CompanyUser companyUser) {
	boolean status = false;
	if (companyUser.getUser_name() != null && companyUser.getUser_name() != "" && companyUser.getEmail() != null
		&& companyUser.getEmail() != "" && companyUser.getCompany() != null) {
	    status = true;
	} else {
	    status = false;
	}
	return status;
    }

    @Override
    public boolean validateCompanyUserforUpdateandDelete(CompanyUser companyUser) {
	boolean status = false;
	if (companyUser.getId() != null && companyUser.getUser_name() != null && companyUser.getUser_name() != ""
		&& companyUser.getEmail() != null && companyUser.getEmail() != ""
		&& companyUser.getLast_modified() != null && companyUser.getLast_modified() != ""
		&& companyUser.getCompany() != null) {
	    // check whether the data is null or not
	    if (this.isCompanyUserNameExist(companyUser.getUser_name())) {
		// check whether the given user name is not changed
		CompanyUser saveCompanyUser = this.getUser(companyUser.getUser_name());
		if (saveCompanyUser.getId().equals(companyUser.getId())) {
		    status = true;
		    if (log.isDebugEnabled()) {
			log.debug("Validation Pass");
		    }
		} else {
		    status = false;
		    if (log.isDebugEnabled()) {
			log.debug("Validation fail due to User Name changed in given object- This is the saved ID:"
				+ saveCompanyUser.getId() + " This is the given ID:" + companyUser.getId());
		    }
		}
	    } else {
		status = false;
		if (log.isDebugEnabled()) {
		    log.debug("Validation fail due to CompanyUser userName:" + companyUser.getUser_name()
			    + " doesn't exist");
		}
	    }
	} else {
	    status = false;
	    if (log.isDebugEnabled()) {
		log.debug("Validation fail due to empty or null values");
	    }
	}
	return status;
    }

    @Override
    public List<CompanyUser> getAllCompanyUsersWithPagination(int page, int recordePerPage) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    Criteria cr = session.createCriteria(CompanyUser.class);
	    cr.setFirstResult((page - 1) * recordePerPage);
	    cr.setMaxResults(recordePerPage);
	    List<CompanyUser> companyUserAll = cr.list();
	    tr.commit();

	    if (companyUserAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company Users are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companyUserAll.size() + " Company Users");
		}
		return companyUserAll;
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    return null;
	}
    }

}

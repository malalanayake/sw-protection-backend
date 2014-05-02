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
import com.sw.protection.backend.dao.CompanySWDAO;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;

/**
 * Company Software operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class CompanySWDAOImpl implements CompanySWDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public static final Logger log = Logger.getLogger(CompanySWDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_SW_DAO);

    @Override
    public List<CompanySW> getAllCompanySWs() {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanySW> companyUserAll = session.getNamedQuery(CompanySW.Constants.NAME_QUERY_FIND_COMPANY_SW_ALL)
		    .list();
	    tr.commit();

	    if (companyUserAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company Softwares are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companyUserAll.size() + " Company softwares");
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
    public CompanySW updateCompanySW(CompanySW companySW) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	CompanySW companySWReturn = null;
	try {
	    // Lock by company software ID
	    LOCK_MAP.lock(companySW.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by company software ID " + companySW.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (companySW.getLast_modified().equals(
		    this.getCompanySW(companySW.getCompany().getUser_name(), companySW.getName()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		companySW.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(companySW);
		tr.commit();
		companySWReturn = companySW;
		if (log.isDebugEnabled()) {
		    log.debug("Update Company software " + companySW.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company software " + companySW.toString()
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
		log.debug("Releasing LOCK by company software ID " + companySW.getId());
	    }
	    // Unlock the lock by company software ID
	    LOCK_MAP.unlock(companySW.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}

	return companySWReturn;
    }

    @Override
    public CompanySW deleteCompanySW(CompanySW companySW) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	CompanySW companySWReturn = null;
	try {
	    // Lock by company software ID
	    LOCK_MAP.lock(companySW.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Company software ID " + companySW.getId());
	    }

	    // check last modification
	    if (companySW.getLast_modified().equals(
		    this.getCompanySW(companySW.getCompany().getUser_name(), companySW.getName()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		companySW = (CompanySW) session.get(CompanySW.class, companySW.getId());
		session.delete(companySW);
		tr.commit();
		companySWReturn = companySW;
		if (log.isDebugEnabled()) {
		    log.debug("Delete Company software " + companySW.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company software " + companySW.toString()
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
		log.debug("Releasing LOCK by Company software ID " + companySW.getId());
	    }
	    // Unlock the lock by company software ID
	    LOCK_MAP.unlock(companySW.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}

	return companySWReturn;
    }

    @Override
    public CompanySW saveCompanySW(CompanySW companySW) throws DuplicateRecordException, OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	CompanySW companySWReturn = null;
	try {
	    // check whether the company software name already exist
	    if (this.isCompanySWExist(companySW.getCompany().getUser_name(), companySW.getName())) {
		if (log.isDebugEnabled()) {
		    log.debug("Company software :" + companySW.toString() + " already exist");
		}
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
	    } else {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		String dateTime = Formatters.formatDate(new Date());
		companySW.setDate_time(dateTime);
		companySW.setLast_modified(dateTime);
		// set last modified data if scope is not null
		if (companySW.getCompanySWCopySet() != null) {
		    for (CompanySWCopy companySwCopy : companySW.getCompanySWCopySet()) {
			companySwCopy.setLast_modified(dateTime);
		    }
		}
		session.save(companySW);
		companySWReturn = companySW;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Company software " + companySW.toString());
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

	return companySWReturn;
    }

    @Override
    public CompanySW getCompanySW(String companyUserName, String softwareName) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanySW> companyUserAll = session
		    .getNamedQuery(CompanySW.Constants.NAME_QUERY_FIND_BY_COMPANY_SW_NAME_AND_COMPANY_USER_NAME)
		    .setParameter(CompanySW.Constants.PARAM_COMPANY_USER_NAME, companyUserName)
		    .setParameter(CompanySW.Constants.PARAM_COMPANY_SW_NAME, softwareName).list();
	    tr.commit();

	    if (companyUserAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company software name : " + softwareName + " is not found");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company software name : " + softwareName + " found");
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
    public boolean isCompanySWExist(String companyUserName, String softwareName) {
	if (this.getCompanySW(companyUserName, softwareName) == null) {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company software " + softwareName + " Exist: False");
	    }
	    return false;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company software " + softwareName + " Exist: True");
	    }
	    return true;
	}
    }

    @Override
    public boolean validateCompanySWforSave(CompanySW companySW) {
	boolean status = false;
	if (companySW.getCompany() != null && companySW.getCompany().getUser_name() != "" && companySW.getName() != "") {
	    status = true;
	} else {
	    status = false;
	}
	return status;
    }

    @Override
    public boolean validateCompanySWforUpdateandDelete(CompanySW companySW) {
	boolean status = false;
	if (companySW.getId() != null && companySW.getCompany().getUser_name() != null && companySW.getName() != ""
		&& companySW.getLast_modified() != null && companySW.getLast_modified() != ""
		&& companySW.getCompany() != null) {
	    // check whether the data is null or not
	    if (this.isCompanySWExist(companySW.getCompany().getUser_name(), companySW.getName())) {
		// check whether the given user name is not changed
		CompanySW saveCompanySw = this.getCompanySW(companySW.getCompany().getUser_name(), companySW.getName());
		if (saveCompanySw.getId().equals(companySW.getId())) {
		    status = true;
		    if (log.isDebugEnabled()) {
			log.debug("Validation Pass");
		    }
		} else {
		    status = false;
		    if (log.isDebugEnabled()) {
			log.debug("Validation fail due to User Name changed in given object- This is the saved ID:"
				+ saveCompanySw.getId() + " This is the given ID:" + companySW.getId());
		    }
		}
	    } else {
		status = false;
		if (log.isDebugEnabled()) {
		    log.debug("Validation fail due to CompanySW Name:" + companySW.getName() + " doesn't exist");
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
    public List<CompanySW> getAllCompanySWWithPagination(int page, int recordePerPage) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    Criteria cr = session.createCriteria(CompanySW.class);
	    cr.setFirstResult((page - 1) * recordePerPage);
	    cr.setMaxResults(recordePerPage);
	    List<CompanySW> companySWAll = cr.list();
	    tr.commit();

	    if (companySWAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company SW are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companySWAll.size() + " Company SW");
		}
		return companySWAll;
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

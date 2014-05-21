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
import com.sw.protection.backend.dao.CompanySWCopyDAO;
import com.sw.protection.backend.entity.CompanySWCopy;

/**
 * Company Software copy operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class CompanySWCopyDAOImpl implements CompanySWCopyDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public static final Logger log = Logger.getLogger(CompanySWCopyDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_SW_COPY_DAO);

    @Override
    public CompanySWCopy updateCompanySWCopy(CompanySWCopy companySWCopy) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	CompanySWCopy companySWCopyReturn = null;
	try {
	    // Lock by company software copy ID
	    LOCK_MAP.lock(companySWCopy.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Company software copy ID " + companySWCopy.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (companySWCopy.getLast_modified().equals(
		    this.getCompanySWCopy(companySWCopy.getCompany_client().getUser_name(),
			    companySWCopy.getCompany_sw().getName(), companySWCopy.getMother_board(),
			    companySWCopy.getHd(), companySWCopy.getMac()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		companySWCopy.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(companySWCopy);
		tr.commit();
		companySWCopyReturn = companySWCopy;
		if (log.isDebugEnabled()) {
		    log.debug("Update Company software copy " + companySWCopy.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company software copy "
			    + companySWCopy.toString() + " so cannot update");
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
		log.debug("Releasing LOCK by software copy ID " + companySWCopy.getId());
	    }
	    // Unlock the lock by software copy ID
	    LOCK_MAP.unlock(companySWCopy.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}
	return companySWCopyReturn;
    }

    @Override
    public CompanySWCopy deleteCompanySWCopy(CompanySWCopy companySWCopy) throws RecordAlreadyModifiedException,
	    OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	CompanySWCopy companySWCopyReturn = null;
	try {
	    // Lock by company software copy ID
	    LOCK_MAP.lock(companySWCopy.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Company software copy ID " + companySWCopy.getId());
	    }

	    // check last modification
	    if (companySWCopy.getLast_modified().equals(
		    this.getCompanySWCopy(companySWCopy.getCompany_client().getUser_name(),
			    companySWCopy.getCompany_sw().getName(), companySWCopy.getMother_board(),
			    companySWCopy.getHd(), companySWCopy.getMac()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		session.delete(companySWCopy);
		tr.commit();
		companySWCopyReturn = companySWCopy;

		if (log.isDebugEnabled()) {
		    log.debug("Delete Company " + companySWCopy.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company software copy "
			    + companySWCopy.toString() + " so cannot delete");
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
		log.debug("Releasing LOCK by Company software copy ID " + companySWCopy.getId());
	    }
	    // Unlock the lock by software copy ID
	    LOCK_MAP.unlock(companySWCopy.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}
	return companySWCopyReturn;
    }

    @Override
    public List<CompanySWCopy> getAllCompanySWCopies() {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanySWCopy> companySWCopyAll = session.getNamedQuery(
		    CompanySWCopy.Constants.NAME_QUERY_FIND_COMPANY_SW_COPY_ALL).list();
	    tr.commit();

	    if (companySWCopyAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company software copies are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companySWCopyAll.size() + " Company software copies");
		}
		return companySWCopyAll;
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
    public CompanySWCopy saveCompanySWCopy(CompanySWCopy companySWCopy) throws DuplicateRecordException,
	    OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	CompanySWCopy companySWCopyReturn = null;
	try {
	    // check whether the software copy already exist
	    if (this.isCompanySWCopyExist(companySWCopy.getCompany_client().getUser_name(), companySWCopy
		    .getCompany_sw().getName(), companySWCopy.getMother_board(), companySWCopy.getHd(), companySWCopy
		    .getMac())) {
		if (log.isDebugEnabled()) {
		    log.debug("Company software copy :" + companySWCopy.toString() + " already exist");
		}
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
	    } else {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		String dateTime = Formatters.formatDate(new Date());
		companySWCopy.setDate_time(dateTime);
		companySWCopy.setLast_modified(dateTime);

		session.save(companySWCopy);
		companySWCopyReturn = companySWCopy;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save software copy " + companySWCopy.toString());
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

	return companySWCopyReturn;
    }

    @Override
    public CompanySWCopy getCompanySWCopy(String clientUserName, String softwareName, String motherBoard, String hd,
	    String mac) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<CompanySWCopy> softwareCopyAll = session
		    .getNamedQuery(
			    CompanySWCopy.Constants.NAME_QUERY_FIND_BY_COMPANY_SW_NAME_AND_CLIENT_USER_NAME_WITH_PROPERTIES)
		    .setParameter(CompanySWCopy.Constants.PARAM_CLIENT_USER_NAME, clientUserName)
		    .setParameter(CompanySWCopy.Constants.PARAM_COMPANY_SW_NAME, softwareName)
		    .setParameter(CompanySWCopy.Constants.PARAM_MOTHER_BOARD, motherBoard)
		    .setParameter(CompanySWCopy.Constants.PARAM_HARD_DRIVE, hd)
		    .setParameter(CompanySWCopy.Constants.PARAM_MAC_ADDRESS, mac).list();
	    tr.commit();

	    if (softwareCopyAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company software copy : " + softwareName + " is not found");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company software copy : " + softwareName + " found");
		}
		return softwareCopyAll.get(0);
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
    public boolean isCompanySWCopyExist(String clientUserName, String softwareName, String motherBoard, String hd,
	    String mac) {
	if (this.getCompanySWCopy(clientUserName, softwareName, motherBoard, hd, mac) == null) {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company software copy " + softwareName + " Exist: False");
	    }
	    return false;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company software copy " + softwareName + " Exist: True");
	    }
	    return true;
	}
    }

    @Override
    public boolean validateCompanySWCopyforSave(CompanySWCopy companySWCopy) {
	boolean status = false;
	if (companySWCopy.getCompany_client() != null && companySWCopy.getCompany_sw() != null
		&& companySWCopy.getExpire_date() != "" && companySWCopy.getHd() != "" && companySWCopy.getMac() != ""
		&& companySWCopy.getMother_board() != "") {
	    status = true;
	} else {
	    status = false;
	}
	return status;
    }

    @Override
    public boolean validateCompanySWCopyforUpdateandDelete(CompanySWCopy companySWCopy) {
	boolean status = false;
	if (companySWCopy.getId() != null && companySWCopy.getCompany_sw() != null
		&& companySWCopy.getCompany_client() != null && companySWCopy.getLast_modified() != null
		&& companySWCopy.getLast_modified() != "" && companySWCopy.getExpire_date() != null
		&& companySWCopy.getExpire_date() != "" && !companySWCopy.getHd().equals("")
		&& !companySWCopy.getMac().equals("") && !companySWCopy.getMother_board().equals("")) {
	    // check whether the data is null or not
	    if (this.isCompanySWCopyExist(companySWCopy.getCompany_client().getUser_name(), companySWCopy
		    .getCompany_sw().getName(), companySWCopy.getMother_board(), companySWCopy.getHd(), companySWCopy
		    .getMac())) {
		status = true;
		if (log.isDebugEnabled()) {
		    log.debug("Validation true");
		}
	    } else {
		status = false;
		if (log.isDebugEnabled()) {
		    log.debug("Validation false : software copy doesn't exist");
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
    public List<CompanySWCopy> getAllCompanySWCopyWithPagination(int page, int recordePerPage) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    Criteria cr = session.createCriteria(CompanySWCopy.class);
	    cr.setFirstResult((page - 1) * recordePerPage);
	    cr.setMaxResults(recordePerPage);
	    List<CompanySWCopy> companySWCopyAll = cr.list();
	    tr.commit();

	    if (companySWCopyAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("CompanySWCopies are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companySWCopyAll.size() + " CompanySWCopies");
		}
		return companySWCopyAll;
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

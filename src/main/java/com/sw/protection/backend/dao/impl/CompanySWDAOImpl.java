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
import com.sw.protection.backend.dao.CompanySWDAO;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;

/**
 * Company Software operation implementation
 * 
 * @author dinuka
 * 
 */
public class CompanySWDAOImpl implements CompanySWDAO {

    private Session session;
    public static final Logger log = Logger.getLogger(CompanySWDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_SW_DAO);

    @Override
    public List<CompanySW> getAllCompanySWs() {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
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
	    // TODO: Throw exception
	    return null;
	}
    }

    @Override
    public void updateCompanySW(CompanySW companySW) {
	Transaction tr = null;
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
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		companySW.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(companySW);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Company software " + companySW.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company software " + companySW.toString()
			    + " so cannot update");
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
		log.debug("Releasing LOCK by company software ID " + companySW.getId());
	    }
	    // Unlock the lock by company software ID
	    LOCK_MAP.unlock(companySW.getId());
	    // TODO: throw the captured exception
	}
    }

    @Override
    public void deleteCompanySW(CompanySW companySW) {
	Transaction tr = null;
	try {
	    // Lock by company software ID
	    LOCK_MAP.lock(companySW.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Company software ID " + companySW.getId());
	    }

	    // check last modification
	    if (companySW.getLast_modified().equals(
		    this.getCompanySW(companySW.getCompany().getUser_name(), companySW.getName()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(companySW);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Company software " + companySW.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company software " + companySW.toString()
			    + " so cannot delete");
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
		log.debug("Releasing LOCK by Company software ID " + companySW.getId());
	    }
	    // Unlock the lock by company software ID
	    LOCK_MAP.unlock(companySW.getId());
	    // TODO: throw the captured exception
	}
    }

    @Override
    public void saveCompanySW(CompanySW companySW) {
	Transaction tr = null;
	try {
	    // check whether the company software name already exist
	    if (this.isCompanySWExist(companySW.getCompany().getUser_name(), companySW.getName())) {
		if (log.isDebugEnabled()) {
		    log.debug("Company software :" + companySW.toString() + " already exist");
		}
		// TODO: Pass the Exception
	    } else {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
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
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Company software " + companySW.toString());
		}
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    // TODO: Throw exception
	}
    }

    @Override
    public CompanySW getCompanySW(String companyUserName, String softwareName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
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
	    // TODO: Throw exception
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

}

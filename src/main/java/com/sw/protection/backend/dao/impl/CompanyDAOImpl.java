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
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanyUser;

/**
 * Company operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class CompanyDAOImpl implements CompanyDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public static final Logger log = Logger.getLogger(CompanyDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_DAO);

    @Override
    public List<Company> getAllCompanies() {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<Company> companyAll = session.getNamedQuery(Company.Constants.NAME_QUERY_FIND_COMPANY_ALL).list();
	    tr.commit();

	    if (companyAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company users are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + companyAll.size() + " Company users");
		}
		return companyAll;
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
    public void updateCompany(Company company) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by admin ID
	    LOCK_MAP.lock(company.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Company ID " + company.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (company.getLast_modified().equals(this.getCompany(company.getUser_name()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		company.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(company);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Company " + company.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company " + company.toString()
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
		log.debug("Releasing LOCK by Company ID " + company.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(company.getId());

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
    public void deleteCompany(Company company) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by admin ID
	    LOCK_MAP.lock(company.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Company ID " + company.getId());
	    }

	    // check last modification
	    if (company.getLast_modified().equals(this.getCompany(company.getUser_name()).getLast_modified())) {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		session.delete(company);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Company " + company.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of company " + company.toString()
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
		log.debug("Releasing LOCK by Company ID " + company.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(company.getId());

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
    public Company saveCompany(Company company) throws DuplicateRecordException, OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	Company companyReturn = null;
	try {
	    // check whether the admin user name already exist
	    if (this.isCompanyUserNameExist(company.getUser_name())) {
		if (log.isDebugEnabled()) {
		    log.debug("Company username :" + company.toString() + " already exist");
		}
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
	    } else {
		Session session = sessionFactory.getCurrentSession();
		tr = session.beginTransaction();
		String dateTime = Formatters.formatDate(new Date());
		company.setDate_time(dateTime);
		company.setLast_modified(dateTime);
		// set last modified data if user set is not null
		if (company.getCompanyUserSet() != null) {
		    for (CompanyUser companyUserScp : company.getCompanyUserSet()) {
			companyUserScp.setDate_time(dateTime);
			companyUserScp.setLast_modified(dateTime);
		    }
		}
		// set last modified data if client set is not null
		if (company.getCompanyClientSet() != null) {
		    for (CompanyClient companyClientScp : company.getCompanyClientSet()) {
			companyClientScp.setDate_time(dateTime);
			companyClientScp.setLast_modified(dateTime);
		    }
		}
		// set last modified data if software set is not null
		if (company.getCompanySWSet() != null) {
		    for (CompanySW companySW : company.getCompanySWSet()) {
			companySW.setDate_time(dateTime);
			companySW.setLast_modified(dateTime);
		    }
		}

		session.save(company);
		companyReturn = company;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Company " + company.toString());
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

	return companyReturn;
    }

    @Override
    public Company getCompany(String companyUserName) {
	Session session = sessionFactory.getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<Company> adminAll = session.getNamedQuery(Company.Constants.NAME_QUERY_FIND_BY_COMPANY_USER_NAME)
		    .setParameter(Company.Constants.PARAM_COMPANY_USER_NAME, companyUserName).list();
	    tr.commit();

	    if (adminAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Company username: " + companyUserName + " is not found");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company username: " + companyUserName + " found");
		}
		return adminAll.get(0);
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
    public Company loadAllPropertiesOfCompany(Long id) {
	Session session = sessionFactory.openSession();
	try {
	    Company company = new Company();
	    company = (Company) session.get(Company.class, id);
	    if (log.isDebugEnabled()) {
		log.debug("Loaded Admin" + company.toString());
	    }
	    return company;
	} catch (RuntimeException ex) {
	    log.error(ex);
	    return null;
	}

    }

    @Override
    public boolean isCompanyUserNameExist(String userName) {
	if (this.getCompany(userName) == null) {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company " + userName + " Exist: False");
	    }
	    return false;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Is Company " + userName + " Exist: True");
	    }
	    return true;
	}
    }

}

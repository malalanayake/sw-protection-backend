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
public class CompanyDAOImpl implements CompanyDAO {
    private Session session;
    public static final Logger log = Logger.getLogger(CompanyDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.COMPANY_DAO);

    @Override
    public List<Company> getAllCompanies() {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
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
	    // TODO: Throw exception
	    return null;
	}
    }

    @Override
    public void updateCompany(Company company) {
	Transaction tr = null;
	try {
	    // Lock by admin ID
	    LOCK_MAP.lock(company.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Company ID " + company.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (company.getLast_modified().equals(this.getCompany(company.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
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
		log.debug("Releasing LOCK by Company ID " + company.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(company.getId());
	    // TODO: throw the captured exception
	}
    }

    @Override
    public void deleteCompany(Company company) {
	Transaction tr = null;
	try {
	    // Lock by admin ID
	    LOCK_MAP.lock(company.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Company ID " + company.getId());
	    }

	    // check last modification
	    if (company.getLast_modified().equals(this.getCompany(company.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
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
		log.debug("Releasing LOCK by Company ID " + company.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(company.getId());
	    // TODO: throw the captured exception
	}
    }

    @Override
    public void saveCompany(Company company) {
	Transaction tr = null;
	try {
	    // check whether the admin user name already exist
	    if (this.isCompanyUserNameExist(company.getUser_name())) {
		if (log.isDebugEnabled()) {
		    log.debug("Company username :" + company.toString() + " already exist");
		}
		// TODO: Pass the Exception
	    } else {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
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
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Company " + company.toString());
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
    public Company getCompany(String companyUserName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
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
	session = HibernateUtil.getSessionFactory().openSession();
	try {
	    Company company = new Company();
	    company = (Company) session.get(Company.class, id);
	    if (log.isDebugEnabled()) {
		log.debug("Loaded Admin" + company.toString());
	    }
	    return company;
	} catch (RuntimeException ex) {
	    log.error(ex);
	    // TODO: Throw exception
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

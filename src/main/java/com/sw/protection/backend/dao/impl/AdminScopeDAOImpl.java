package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.AdminScopeDAO;
import com.sw.protection.backend.entity.AdminScope;

/**
 * Admin Scope operation implementation
 * 
 * @author dinuka
 * 
 */
public class AdminScopeDAOImpl implements AdminScopeDAO {
    private Session session;
    public static final Logger log = Logger.getLogger(AdminScopeDAOImpl.class.getName());

    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.ADMIN_SCOPE_DAO);

    @Override
    public List<AdminScope> getAllAdminScopes(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<AdminScope> adminScopes = session.getNamedQuery(AdminScope.Constants.NAME_QUERY_FIND_BY_USER_NAME)
		    .setParameter(AdminScope.Constants.PARAM_USER_NAME, userName).list();
	    tr.commit();

	    if (adminScopes.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Admin scopes not found under admin user: " + userName);
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin user " + userName + " found with admin scopes");
		}
		return adminScopes;
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
    public void saveNewAdminScope(AdminScope adminScope) {
	Transaction tr = null;
	try {
	    if (this.getAdminScope(adminScope.getAdmin().getUser_name(), adminScope.getApi_name()) == null) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		// set latest time on modification
		adminScope.setLast_modified(Formatters.formatDate(new Date()));
		session.save(adminScope);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Admin scope" + adminScope.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin scope" + adminScope.toString() + " already exist");
		}
		// TODO: Create Exception
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
    public void deleteAdminScope(AdminScope adminScope) {
	Transaction tr = null;
	try {
	    // Lock by adminScope ID
	    LOCK_MAP.lock(adminScope.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Admin ID " + adminScope.getId());
	    }

	    // check last modification
	    if (adminScope.getLast_modified().equals(
		    this.getAdminScope(adminScope.getAdmin().getUser_name(), adminScope.getApi_name())
			    .getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(adminScope);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete AdminScope" + adminScope.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of admin scope " + adminScope.toString()
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
		log.debug("Releasing LOCK by Admin ID " + adminScope.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(adminScope.getId());
	    // TODO: throw the captured exception
	}

    }

    @Override
    public void updateAdminScope(AdminScope adminScope) {
	Transaction tr = null;
	try {
	    // Lock by admin ID
	    LOCK_MAP.lock(adminScope.getAdmin().getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Admin ID " + adminScope.getAdmin().getId());
	    }

	    // check the modification time
	    if (adminScope.getLast_modified().equals(
		    getAdminScope(adminScope.getAdmin().getUser_name(), adminScope.getApi_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();

		// set the latest date and time
		adminScope.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(adminScope);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update admin scope to " + adminScope.toString());
		}

	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest admin scope:" + adminScope.toString()
			    + " so this is not going to upate");
		}
		// TODO: create the exception
	    }

	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    // TODO: Throw exception
	} finally {
	    if (log.isDebugEnabled()) {
		log.debug("Releasing LOCK by Admin ID " + adminScope.getAdmin().getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(adminScope.getAdmin().getId());
	    // TODO: throw the captured exception
	}

    }

    @Override
    public AdminScope getAdminScope(String userName, String apiName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<AdminScope> adminScopes = session
		    .getNamedQuery(AdminScope.Constants.NAME_QUERY_FIND_BY_USER_NAME_AND_API_NAME)
		    .setParameter(AdminScope.Constants.PARAM_USER_NAME, userName)
		    .setParameter(AdminScope.Constants.PARAM_API_NAME, apiName).list();
	    tr.commit();

	    if (adminScopes.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Admin scopes not found under admin user: " + userName);
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin user " + userName + " found with admin scope according to API name " + apiName);
		}
		return adminScopes.get(0);
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
    public boolean isAccessGrantedFor(String userName, String apiName, APIOperations operation) {
	AdminScope adminScope = null;
	adminScope = getAdminScope(userName, apiName);
	boolean result = false;
	if (adminScope == null) {// User doesn't have scope recode for given API
	    if (log.isDebugEnabled()) {
		log.debug("Admin user " + userName + " doesn't have scope recode for API " + apiName);
	    }
	    return result;
	}

	switch (operation) {
	case GET:
	    result = adminScope.isGet();
	    break;
	case POST:
	    result = adminScope.isPost();
	    break;
	case PUT:
	    result = adminScope.isPut();
	    break;
	case DELETE:
	    result = adminScope.isDel();
	    break;
	}

	if (log.isDebugEnabled()) {
	    log.debug("Admin user " + userName + " has scope recode for API " + apiName + " " + operation + "="
		    + result);
	}
	return result;
    }

}

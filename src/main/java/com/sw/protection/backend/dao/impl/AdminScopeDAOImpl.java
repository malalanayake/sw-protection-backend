package com.sw.protection.backend.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sw.protection.backend.config.HibernateUtil;
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

    @Override
    public List<AdminScope> getAllAdminScopes(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {
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
	    tr.rollback(); // rall back the transaction due to runtime error
	    log.error(ex);
	    // TODO: Throw exception
	    return null;
	}
    }

    @Override
    public void saveNewAdminScope(AdminScope adminScope) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {
	    session.save(adminScope);
	    tr.commit();
	    if (log.isDebugEnabled()) {
		log.debug("Save Admin scope" + adminScope.toString());
	    }
	} catch (RuntimeException ex) {
	    tr.rollback();
	    log.error(ex);
	    // TODO: Throw exception
	}
    }

    @Override
    public void deleteAdminScope(AdminScope adminScope) {
	// TODO Auto-generated method stub

    }

    @Override
    public void updateAdminScope(AdminScope adminScope) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {
	    synchronized (adminScope.getAdmin().getId()) {
		session.merge(adminScope);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Update admin scope to " + adminScope.toString());
		}
	    }
	} catch (RuntimeException ex) {
	    tr.rollback();
	    log.error(ex);
	    // TODO: Throw exception
	}

    }

    @Override
    public AdminScope getAdminScope(String userName, String apiName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {
	    List<AdminScope> adminScopes = session.getNamedQuery(
		    AdminScope.Constants.NAME_QUERY_FIND_BY_USER_NAME_AND_API_NAME).setParameter(
		    AdminScope.Constants.PARAM_USER_NAME, userName).setParameter(AdminScope.Constants.PARAM_API_NAME,
		    apiName).list();
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
	    tr.rollback(); // rall back the transaction due to runtime error
	    log.error(ex);
	    // TODO: Throw exception
	    return null;
	}
    }

}

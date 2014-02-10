/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;

/**
 * Admin operation implementation
 * 
 * @author dinuka
 */
public class AdminDAOImpl implements AdminDAO {
    private Session session;
    public static final Logger log = Logger.getLogger(AdminDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.ADMIN_DAO);

    @Override
    public Admin getAdmin(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<Admin> adminAll = session.getNamedQuery(Admin.Constants.NAME_QUERY_FIND_BY_USER_NAME)
		    .setParameter(Admin.Constants.PARAM_USER_NAME, userName).list();
	    tr.commit();

	    if (adminAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Admin username: " + userName + " is not found");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin username: " + userName + " found");
		}
		return adminAll.get(0);
	    }
	} catch (RuntimeException ex) {
	    tr.rollback(); // rall back the transaction due to runtime error
	    log.error(ex);
	    // TODO: Throw exception
	    return null;
	}

    }

    @Override
    public void updateAdmin(Admin admin) {
	Transaction tr = null;
	try {
	    // Lock by admin ID
	    LOCK_MAP.lock(admin.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Admin ID " + admin.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (admin.getLast_modified().equals(this.getAdmin(admin.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		admin.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(admin);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Admin" + admin.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of admin " + admin.toString() + " so cannot update");
		}
		// TODO:Create Exception
	    }

	} catch (RuntimeException ex) {
	    log.error(ex);
	    tr.rollback();
	    // TODO: capture the exception
	} finally {
	    if (log.isDebugEnabled()) {
		log.debug("Releasing LOCK by Admin ID " + admin.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(admin.getId());
	    // TODO: throw the captured exception
	}
    }

    @Override
    public void deleteAdmin(Admin admin) {
	Transaction tr = null;
	try {
	    // check last modification
	    if (admin.getLast_modified().equals(this.getAdmin(admin.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(admin);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Admin" + admin.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of admin " + admin.toString() + " so cannot delete");
		}
		// TODO:Create Exception
	    }
	} catch (RuntimeException ex) {
	    tr.rollback();
	    log.error(ex);
	    // TODO: Throw exception
	}
    }

    @Override
    public void saveAdmin(Admin admin) {
	Transaction tr = null;
	try {
	    // check whether the admin user name already exist
	    if (this.isAdminUserNameExist(admin.getUser_name())) {
		if (log.isDebugEnabled()) {
		    log.debug("Admin username :" + admin.toString() + " already exist");
		}
		// TODO: Pass the Exception
	    } else {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		String dateTime = Formatters.formatDate(new Date());
		admin.setDate_time(dateTime);
		admin.setLast_modified(dateTime);
		// set last modified data if scope is not null
		if (admin.getAdminScopeSet() != null) {
		    for (AdminScope adminScp : admin.getAdminScopeSet()) {
			adminScp.setLast_modified(dateTime);
		    }
		}
		session.save(admin);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Admin" + admin.toString());
		}
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    tr.rollback();
	    // TODO: Throw exception
	}
    }

    @Override
    public Admin loadAllPropertiesOfAdmin(Long id) {
	session = HibernateUtil.getSessionFactory().openSession();
	try {
	    Admin admin = new Admin();
	    admin = (Admin) session.get(Admin.class, id);
	    if (log.isDebugEnabled()) {
		log.debug("Loaded Admin" + admin.toString());
	    }
	    return admin;
	} catch (RuntimeException ex) {
	    log.error(ex);
	    // TODO: Throw exception
	    return null;
	}

    }

    @Override
    public boolean isAdminUserNameExist(String userName) {
	if (this.getAdmin(userName) == null) {
	    if (log.isDebugEnabled()) {
		log.debug("Is Admin " + userName + " Exist: False");
	    }
	    return false;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Is Admin " + userName + " Exist: True");
	    }
	    return true;
	}
    }

    @Override
    public List<Admin> getAllAdmins() {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<Admin> adminAll = session.getNamedQuery(Admin.Constants.NAME_QUERY_FIND_ALL).list();
	    tr.commit();

	    if (adminAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Admin users are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + adminAll.size() + " Admin users");
		}
		return adminAll;
	    }
	} catch (RuntimeException ex) {
	    tr.rollback(); // rall back the transaction due to runtime error
	    log.error(ex);
	    // TODO: Throw exception
	    return null;
	}
    }

}

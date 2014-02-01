/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;

/**
 * Admin operation implementation
 * 
 * @author dinuka
 */
public class AdminDAOImpl implements AdminDAO {
    private Session session;
    public static final Logger log = Logger.getLogger(AdminDAOImpl.class.getName());

    private static volatile ConcurrentHashMap<Object, ReentrantLock> LOCKS = new ConcurrentHashMap<Object, ReentrantLock>();

    @Override
    public Admin getAdmin(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {
	    List<Admin> adminAll = session.getNamedQuery(Admin.Constants.NAME_QUERY_FIND_BY_USER_NAME).setParameter(
		    Admin.Constants.PARAM_USER_NAME, userName).list();
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
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {
	    LOCKS.putIfAbsent(admin.getId(), new ReentrantLock());
	    LOCKS.get(admin.getId()).lock(); // lock by Admin ID

	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Admin ID " + admin.getId());
	    }
	    session.merge(admin);
	    tr.commit();

	    if (log.isDebugEnabled()) {
		log.debug("Update Admin" + admin.toString());
		log.debug("Estimated waiting number of threads " + LOCKS.get(admin.getId()).getQueueLength());
	    }

	} catch (RuntimeException ex) {
	    tr.rollback();
	    log.error(ex);
	    // TODO: capture the exception
	} finally {
	    LOCKS.get(admin.getId()).unlock(); // Release by Admin ID
	    if (log.isDebugEnabled()) {
		log.debug("Released LOCK by Admin ID " + admin.getId());
	    }
	    // TODO: throw the captured exception
	}
    }

    @Override
    public void deleteAdmin(Admin admin) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {
	    session.delete(admin);
	    tr.commit();
	    if (log.isDebugEnabled()) {
		log.debug("Delete Admin" + admin.toString());
	    }
	} catch (RuntimeException ex) {
	    tr.rollback();
	    log.error(ex);
	    // TODO: Throw exception
	}
    }

    @Override
    public void saveAdmin(Admin admin) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	try {// TODO:synchronize this
	    session.save(admin);
	    tr.commit();
	    if (log.isDebugEnabled()) {
		log.debug("Save Admin" + admin.toString());
	    }
	} catch (RuntimeException ex) {
	    tr.rollback();
	    log.error(ex);
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
	Transaction tr = session.beginTransaction();
	try {
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

    /**
     * Clear all not useful locks from the LOCKS Concurrent HashMap
     */
    public void clearLocks() {
	synchronized (AdminDAOImpl.class) {
	    List<Admin> allAdmins = this.getAllAdmins();
	    for (Admin admin : allAdmins) {
		ReentrantLock lock = null;
		lock = LOCKS.get(admin.getId());
		if (lock != null) { // Check whether the lock initiated
		    if (lock.getQueueLength() == 0) {
			// Check is there any thread waiting for this lock
			LOCKS.remove(admin.getId());
			if (log.isDebugEnabled()) {
			    log.debug("Removed Lock relevent to Admin " + admin.getId());
			}
		    } else {
			if (log.isDebugEnabled()) {
			    log.debug("Some Threads are waiting to Lock relevent to Admin " + admin.getId());
			}
		    }
		} else {
		    if (log.isDebugEnabled()) {
			log.debug("Threads is no lock initiated relevent to Admin " + admin.getId());
		    }
		}
	    }
	}
    }

}

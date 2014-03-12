package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.SuperAdminDAO;
import com.sw.protection.backend.entity.SuperAdmin;

/**
 * Super Admin operation implementation
 * 
 * @author dinuka
 * 
 */
public class SuperAdminDAOImpl implements SuperAdminDAO {
    private Session session;
    public static final Logger log = Logger.getLogger(SuperAdminDAOImpl.class.getName());
    /**
     * Maintain Locks over the cluster
     */
    private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
	    SharedInMemoryData.DB_LOCKS.SUPER_ADMIN_DAO);

    @Override
    public List<SuperAdmin> getAllAdmins() {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<SuperAdmin> superAdminAll = session
		    .getNamedQuery(SuperAdmin.Constants.NAME_QUERY_FIND_SUPER_ADMIN_ALL).list();
	    tr.commit();

	    if (superAdminAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Super admin users are not exist");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Found " + superAdminAll.size() + " Super admin users");
		}
		return superAdminAll;
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
    public SuperAdmin getSuperAdmin(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<SuperAdmin> superAdminAll = session
		    .getNamedQuery(SuperAdmin.Constants.NAME_QUERY_FIND_BY_SUPER_ADMIN_USER_NAME)
		    .setParameter(SuperAdmin.Constants.PARAM_SUPER_ADMIN_USER_NAME, userName).list();
	    tr.commit();

	    if (superAdminAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Super admin username: " + userName + " is not found");
		}
		return null;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Super admin username: " + userName + " found");
		}
		return superAdminAll.get(0);
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
    public void updateSuperAdmin(SuperAdmin admin) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by super admin ID
	    LOCK_MAP.lock(admin.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked update operation by Super admin ID " + admin.getId());
	    }

	    // Assume that the username never changed
	    // check last modification
	    if (admin.getLast_modified().equals(this.getSuperAdmin(admin.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		admin.setLast_modified(Formatters.formatDate(new Date()));
		session.merge(admin);
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Super admin" + admin.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of Super admin " + admin.toString()
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
		log.debug("Releasing LOCK by Super admin ID " + admin.getId());
	    }
	    // Unlock the lock by super admin ID
	    LOCK_MAP.unlock(admin.getId());

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
    public void deleteSuperAdmin(SuperAdmin admin) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	try {
	    // Lock by super admin ID
	    LOCK_MAP.lock(admin.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Super admin ID " + admin.getId());
	    }

	    // check last modification
	    if (admin.getLast_modified().equals(this.getSuperAdmin(admin.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(admin);
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Admin" + admin.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of Super admin " + admin.toString()
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
		log.debug("Releasing LOCK by Super admin ID " + admin.getId());
	    }
	    // Unlock the lock by super admin ID
	    LOCK_MAP.unlock(admin.getId());

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
    public SuperAdmin saveSuperAdmin(SuperAdmin admin) throws DuplicateRecordException, OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	SuperAdmin superAdminReturn = null;
	try {
	    // check whether the super admin user name already exist
	    if (this.isSuperAdminUserNameExist(admin.getUser_name())) {
		if (log.isDebugEnabled()) {
		    log.debug("Super admin username :" + admin.toString() + " already exist");
		}
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
	    } else {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		String dateTime = Formatters.formatDate(new Date());
		admin.setDate_time(dateTime);
		admin.setLast_modified(dateTime);

		session.save(admin);
		superAdminReturn = admin;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Super admin" + admin.toString());
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

	return superAdminReturn;
    }

    @Override
    public boolean isSuperAdminUserNameExist(String userName) {
	if (this.getSuperAdmin(userName) == null) {
	    if (log.isDebugEnabled()) {
		log.debug("Is Super admin " + userName + " Exist: False");
	    }
	    return false;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Is Super admin " + userName + " Exist: True");
	    }
	    return true;
	}
    }

}

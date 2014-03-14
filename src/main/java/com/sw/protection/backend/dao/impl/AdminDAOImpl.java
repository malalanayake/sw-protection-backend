package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
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
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    return null;
	}

    }

    @Override
    public Admin updateAdmin(Admin admin) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	Admin adminReturn = null;
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
		adminReturn = admin;
		tr.commit();

		if (log.isDebugEnabled()) {
		    log.debug("Update Admin" + admin.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of admin " + admin.toString() + " so cannot update");
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
		log.debug("Releasing LOCK by Admin ID " + admin.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(admin.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }

	}

	return adminReturn;
    }

    @Override
    public Admin deleteAdmin(Admin admin) throws RecordAlreadyModifiedException, OperationRollBackException {
	Transaction tr = null;
	RecordAlreadyModifiedException recordAlreadyModifiedException = null;
	OperationRollBackException operationRollBackException = null;
	Admin adminReturn = null;
	try {
	    // Lock by admin ID
	    LOCK_MAP.lock(admin.getId());
	    if (log.isDebugEnabled()) {
		log.debug("Locked delete operation by Admin ID " + admin.getId());
	    }

	    // check last modification
	    if (admin.getLast_modified().equals(this.getAdmin(admin.getUser_name()).getLast_modified())) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		tr = session.beginTransaction();
		session.delete(admin);
		adminReturn = admin;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Delete Admin" + admin.toString());
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("This is not the latest modification of admin " + admin.toString() + " so cannot delete");
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
		log.debug("Releasing LOCK by Admin ID " + admin.getId());
	    }
	    // Unlock the lock by admin ID
	    LOCK_MAP.unlock(admin.getId());

	    // throw the captured exceptions
	    if (recordAlreadyModifiedException != null) {
		throw recordAlreadyModifiedException;
	    }

	    if (operationRollBackException != null) {
		throw operationRollBackException;
	    }
	}
	return adminReturn;
    }

    @Override
    public Admin saveAdmin(Admin admin) throws DuplicateRecordException, OperationRollBackException {
	Transaction tr = null;
	OperationRollBackException operationRollBackException = null;
	DuplicateRecordException duplicateRecordException = null;
	Admin adminReturn = null;
	try {
	    // check whether the admin user name already exist
	    if (this.isAdminUserNameExist(admin.getUser_name()) || this.isAdminUserEmailExist(admin.getEmail())) {
		if (log.isDebugEnabled()) {
		    log.debug("Admin user:" + admin.toString() + " already exist");
		}
		// create DuplicateRecordException
		duplicateRecordException = new DuplicateRecordException();
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
		adminReturn = admin;
		tr.commit();
		if (log.isDebugEnabled()) {
		    log.debug("Save Admin" + admin.toString());
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
	return adminReturn;
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
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    return null;
	}
    }

    @Override
    public List<Admin> getAllAdminsWithPagination(int page, int recordePerPage) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    Criteria cr = session.createCriteria(Admin.class);
	    cr.setFirstResult((page - 1) * recordePerPage);
	    cr.setMaxResults(recordePerPage);
	    List<Admin> adminAll = cr.list();
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
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    return null;
	}
    }

    @Override
    public boolean isAdminUserEmailExist(String email) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = null;
	try {
	    tr = session.beginTransaction();
	    List<Admin> adminAll = session.getNamedQuery(Admin.Constants.NAME_QUERY_FIND_BY_EMAIL)
		    .setParameter(Admin.Constants.PARAM_USER_EMAIL, email).list();
	    tr.commit();

	    if (adminAll.isEmpty()) {
		if (log.isDebugEnabled()) {
		    log.debug("Admin email: " + email + " is not found");
		}
		return false;
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin email: " + email + " found");
		}
		return true;
	    }
	} catch (RuntimeException ex) {
	    log.error(ex);
	    if (tr != null) {
		tr.rollback(); // roll back the transaction due to runtime error
	    }
	    return false;
	}
    }

    @Override
    public boolean validateAdminforSave(Admin admin) {
	boolean status = false;
	if (admin.getUser_name() != null && admin.getUser_name() != "" && admin.getEmail() != null
		&& admin.getEmail() != "") {
	    status = true;
	} else {
	    status = false;
	}
	return status;
    }

    @Override
    public boolean validateAdminforUpdateandDelete(Admin admin) {
	boolean status = false;
	if (admin.getId() != null && admin.getUser_name() != null && admin.getUser_name() != ""
		&& admin.getEmail() != null && admin.getEmail() != "" && admin.getLast_modified() != null
		&& admin.getLast_modified() != "") {
	    // check whether the data is null or not
	    if (this.isAdminUserNameExist(admin.getUser_name())) {
		// check whether the given user name is not changed
		Admin saveAdmin = this.getAdmin(admin.getUser_name());
		if (saveAdmin.getId().equals(admin.getId())) {
		    status = true;
		    if (log.isDebugEnabled()) {
			log.debug("Validation Pass");
		    }
		} else {
		    status = false;
		    if (log.isDebugEnabled()) {
			log.debug("Validation fail due to User Name changed in given object- This is the saved ID:"
				+ saveAdmin.getId() + " This is the given ID:" + admin.getId());
		    }
		}
	    } else {
		status = false;
		if (log.isDebugEnabled()) {
		    log.debug("Validation fail due to Admin userName:" + admin.getUser_name() + " doesn't exist");
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

}

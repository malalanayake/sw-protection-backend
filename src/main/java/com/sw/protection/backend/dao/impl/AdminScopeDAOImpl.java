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
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.AdminScopeDAO;
import com.sw.protection.backend.entity.AdminScope;

/**
 * Admin Scope operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class AdminScopeDAOImpl implements AdminScopeDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public static final Logger log = Logger.getLogger(AdminScopeDAOImpl.class.getName());

	/**
	 * Maintain Locks over the cluster
	 */
	private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
			SharedInMemoryData.DB_LOCKS.ADMIN_SCOPE_DAO);

	@Override
	public List<AdminScope> getAllAdminScopes(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			List<AdminScope> adminScopes = session
					.getNamedQuery(AdminScope.Constants.NAME_QUERY_FIND_BY_USER_NAME)
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
			return null;
		}
	}

	@Override
	public AdminScope saveNewAdminScope(AdminScope adminScope) throws DuplicateRecordException,
			OperationRollBackException {
		Transaction tr = null;
		OperationRollBackException operationRollBackException = null;
		DuplicateRecordException duplicateRecordException = null;
		AdminScope adminScopeReturn = null;
		try {
			if (this.getAdminScope(adminScope.getAdmin().getUser_name(), adminScope.getApi_name()) == null) {
				Session session = sessionFactory.getCurrentSession();
				tr = session.beginTransaction();
				// set latest time on modification
				adminScope.setLast_modified(Formatters.formatDate(new Date()));
				session.save(adminScope);
				tr.commit();
				adminScopeReturn = adminScope;
				if (log.isDebugEnabled()) {
					log.debug("Save Admin scope" + adminScope.toString());
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Admin scope" + adminScope.toString() + " already exist");
				}
				// create DuplicateRecordException
				duplicateRecordException = new DuplicateRecordException();
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
		return adminScopeReturn;
	}

	@Override
	public AdminScope deleteAdminScope(AdminScope adminScope)
			throws RecordAlreadyModifiedException, OperationRollBackException {
		Transaction tr = null;
		AdminScope adminScopeReturn = null;
		OperationRollBackException operationRollBackException = null;
		RecordAlreadyModifiedException recordAlreadyModifiedException = null;
		try {
			// Lock by adminScope ID
			LOCK_MAP.lock(adminScope.getId());
			if (log.isDebugEnabled()) {
				log.debug("Locked delete operation by Admin ID " + adminScope.getId());
			}

			// check last modification
			if (adminScope.getLast_modified().equals(
					this.getAdminScope(adminScope.getAdmin().getUser_name(),
							adminScope.getApi_name()).getLast_modified())) {
				Session session = sessionFactory.getCurrentSession();
				tr = session.beginTransaction();
				session.delete(adminScope);
				tr.commit();
				adminScopeReturn = adminScope;
				if (log.isDebugEnabled()) {
					log.debug("Delete AdminScope" + adminScope.toString());
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("This is not the latest modification of admin scope "
							+ adminScope.toString() + " so cannot delete");
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
				log.debug("Releasing LOCK by Admin ID " + adminScope.getId());
			}
			// Unlock the lock by admin ID
			LOCK_MAP.unlock(adminScope.getId());

			// throw the captured exceptions
			if (recordAlreadyModifiedException != null) {
				throw recordAlreadyModifiedException;
			}

			if (operationRollBackException != null) {
				throw operationRollBackException;
			}
		}
		return adminScopeReturn;

	}

	@Override
	public AdminScope updateAdminScope(AdminScope adminScope)
			throws RecordAlreadyModifiedException, OperationRollBackException {
		Transaction tr = null;
		AdminScope adminScopeReturn = null;
		OperationRollBackException operationRollBackException = null;
		RecordAlreadyModifiedException recordAlreadyModifiedException = null;
		try {
			// Lock by admin ID
			LOCK_MAP.lock(adminScope.getAdmin().getId());
			if (log.isDebugEnabled()) {
				log.debug("Locked update operation by Admin ID " + adminScope.getAdmin().getId());
			}

			// check the modification time
			if (adminScope.getLast_modified().equals(
					getAdminScope(adminScope.getAdmin().getUser_name(), adminScope.getApi_name())
							.getLast_modified())) {
				Session session = sessionFactory.getCurrentSession();
				tr = session.beginTransaction();

				// set the latest date and time
				adminScope.setLast_modified(Formatters.formatDate(new Date()));
				session.merge(adminScope);
				tr.commit();
				adminScopeReturn = adminScope;

				if (log.isDebugEnabled()) {
					log.debug("Update admin scope to " + adminScope.toString());
				}

			} else {
				if (log.isDebugEnabled()) {
					log.debug("This is not the latest admin scope:" + adminScope.toString()
							+ " so this is not going to upate");
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
				log.debug("Releasing LOCK by Admin ID " + adminScope.getAdmin().getId());
			}
			// Unlock the lock by admin ID
			LOCK_MAP.unlock(adminScope.getAdmin().getId());

			// throw the captured exceptions
			if (recordAlreadyModifiedException != null) {
				throw recordAlreadyModifiedException;
			}

			if (operationRollBackException != null) {
				throw operationRollBackException;
			}
		}

		return adminScopeReturn;

	}

	@Override
	public AdminScope getAdminScope(String userName, String apiName) {
		Session session = sessionFactory.getCurrentSession();
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
					log.debug("Admin user " + userName
							+ " found with admin scope according to API name " + apiName);
				}
				return adminScopes.get(0);
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
	public boolean isAccessGrantedFor(String userName, String apiName, APIOperations operation) {
		AdminScope adminScope = null;
		adminScope = getAdminScope(userName, apiName);
		boolean result = false;
		if (adminScope == null) {// User doesn't have scope recode for given API
			if (log.isDebugEnabled()) {
				log.debug("Admin user " + userName + " doesn't have scope recode for API "
						+ apiName);
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
			log.debug("Admin user " + userName + " has scope recode for API " + apiName + " "
					+ operation + "=" + result);
		}
		return result;
	}

	@Override
	public boolean validateAdminScopeforSave(AdminScope adminScope) {
		boolean status = false;
		if (adminScope.getAdmin() != null) {
			if (adminScope.getAdmin().getUser_name() != ""
					&& adminScope.getAdmin().getUser_name() != null
					&& adminScope.getApi_name() != null && adminScope.getApi_name() != "") {
				status = true;
			} else {
				status = false;
			}
		} else {
			status = false;
		}
		return status;
	}

	@Override
	public boolean validateAdminScopeforUpdateandDelete(AdminScope adminScope) {
		boolean status = false;
		if (adminScope != null) {
			if (adminScope.getAdmin() != null) {
				if (adminScope.getAdmin().getUser_name() != null
						&& adminScope.getAdmin().getUser_name() != ""
						&& adminScope.getAdmin().getId() != null
						&& adminScope.getApi_name() != null && adminScope.getApi_name() != ""
						&& adminScope.getLast_modified() != null
						&& adminScope.getLast_modified() != "") {
					status = true;
				} else {
					status = false;
				}
			} else {
				// admin is null
				status = false;
			}
		} else {
			// admin scope is null
			status = false;
		}
		return status;
	}
}

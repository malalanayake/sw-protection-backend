package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
import com.sw.protection.backend.dao.CompanyClientDAO;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;

/**
 * Company Client operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class CompanyClientDAOImpl implements CompanyClientDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public static final Logger log = Logger.getLogger(CompanyClientDAOImpl.class.getName());

	/**
	 * Maintain Locks over the cluster
	 */
	private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
			SharedInMemoryData.DB_LOCKS.COMPANY_CLIENT_DAO);

	@Override
	public List<CompanyClient> getAllCompanyClients() {
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			List<CompanyClient> companyClientAll = session.getNamedQuery(
					CompanyClient.Constants.NAME_QUERY_FIND_COMPANY_CLIENT_ALL).list();
			tr.commit();

			if (companyClientAll.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("Company clients are not exist");
				}
				return null;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Found " + companyClientAll.size() + " Company clients");
				}
				return companyClientAll;
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
	public CompanyClient getCompanyClient(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			List<CompanyClient> companyClientAll = session
					.getNamedQuery(
							CompanyClient.Constants.NAME_QUERY_FIND_BY_COMPANY_CLIENT_USER_NAME)
					.setParameter(CompanyClient.Constants.PARAM_COMPANY_CLIENT_USER_NAME, userName)
					.list();
			tr.commit();

			if (companyClientAll.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("Company client's username: " + userName + " is not found");
				}
				return null;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Company client's username: " + userName + " found");
				}
				return companyClientAll.get(0);
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
	public CompanyClient updateCompanyClient(CompanyClient companyClient)
			throws RecordAlreadyModifiedException, OperationRollBackException {
		Transaction tr = null;
		RecordAlreadyModifiedException recordAlreadyModifiedException = null;
		OperationRollBackException operationRollBackException = null;
		CompanyClient companyClientReturn = null;
		try {
			// Lock by company client ID
			LOCK_MAP.lock(companyClient.getId());
			if (log.isDebugEnabled()) {
				log.debug("Locked update operation by Client ID " + companyClient.getId());
			}

			// Assume that the username never changed
			// check last modification
			if (companyClient.getLast_modified().equals(
					this.getCompanyClient(companyClient.getUser_name()).getLast_modified())) {
				Session session = sessionFactory.getCurrentSession();
				tr = session.beginTransaction();
				companyClient.setLast_modified(Formatters.formatDate(new Date()));
				session.merge(companyClient);
				tr.commit();
				companyClientReturn = companyClient;
				if (log.isDebugEnabled()) {
					log.debug("Update Company client " + companyClient.toString());
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("This is not the latest modification of company client "
							+ companyClient.toString() + " so cannot update");
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
				log.debug("Releasing LOCK by Client ID " + companyClient.getId());
			}
			// Unlock the lock by company client ID
			LOCK_MAP.unlock(companyClient.getId());

			// throw the captured exceptions
			if (recordAlreadyModifiedException != null) {
				throw recordAlreadyModifiedException;
			}

			if (operationRollBackException != null) {
				throw operationRollBackException;
			}
		}

		return companyClientReturn;
	}

	@Override
	public CompanyClient deleteCompanyClient(CompanyClient companyClient)
			throws RecordAlreadyModifiedException, OperationRollBackException {
		Transaction tr = null;
		RecordAlreadyModifiedException recordAlreadyModifiedException = null;
		OperationRollBackException operationRollBackException = null;
		CompanyClient companyClientReturn = null;
		try {
			// Lock by company client ID
			LOCK_MAP.lock(companyClient.getId());
			if (log.isDebugEnabled()) {
				log.debug("Locked delete operation by Company client ID " + companyClient.getId());
			}

			// check last modification
			if (companyClient.getLast_modified().equals(
					this.getCompanyClient(companyClient.getUser_name()).getLast_modified())) {
				Session session = sessionFactory.getCurrentSession();
				tr = session.beginTransaction();
				companyClient = (CompanyClient) session.get(CompanyClient.class,
						companyClient.getId());
				session.delete(companyClient);
				tr.commit();
				companyClientReturn = companyClient;
				if (log.isDebugEnabled()) {
					log.debug("Delete Company client " + companyClient.toString());
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("This is not the latest modification of company client "
							+ companyClient.toString() + " so cannot delete");
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
				log.debug("Releasing LOCK by Company client ID " + companyClient.getId());
			}
			// Unlock the lock by company user ID
			LOCK_MAP.unlock(companyClient.getId());

			// throw the captured exceptions
			if (recordAlreadyModifiedException != null) {
				throw recordAlreadyModifiedException;
			}

			if (operationRollBackException != null) {
				throw operationRollBackException;
			}
		}

		return companyClientReturn;
	}

	@Override
	public CompanyClient saveCompanyClient(CompanyClient companyClient)
			throws DuplicateRecordException, OperationRollBackException {
		Transaction tr = null;
		OperationRollBackException operationRollBackException = null;
		DuplicateRecordException duplicateRecordException = null;
		CompanyClient companyClientReturn = null;
		try {
			// check whether the company user name already exist
			if (this.isCompanyClientUserNameExist(companyClient.getUser_name())) {
				if (log.isDebugEnabled()) {
					log.debug("Company client username :" + companyClient.toString()
							+ " already exist");
				}
				// create DuplicateRecordException
				duplicateRecordException = new DuplicateRecordException();
			} else {
				Session session = sessionFactory.getCurrentSession();
				tr = session.beginTransaction();
				String dateTime = Formatters.formatDate(new Date());
				companyClient.setDate_time(dateTime);
				companyClient.setLast_modified(dateTime);
				// set last modified data if scope is not null
				if (companyClient.getCompanySWCopySet() != null) {
					for (CompanySWCopy companySWCopy : companyClient.getCompanySWCopySet()) {
						companySWCopy.setLast_modified(dateTime);
					}
				}
				session.save(companyClient);
				companyClientReturn = companyClient;
				tr.commit();
				if (log.isDebugEnabled()) {
					log.debug("Save Company client " + companyClient.toString());
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

		return companyClientReturn;
	}

	@Override
	public boolean isCompanyClientUserNameExist(String userName) {
		if (this.getCompanyClient(userName) == null) {
			if (log.isDebugEnabled()) {
				log.debug("Is Company client " + userName + " Exist: False");
			}
			return false;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Is Company client " + userName + " Exist: True");
			}
			return true;
		}
	}

	@Override
	public CompanyClient loadAllPropertiesOfCompanyClient(Long id) {
		Session session = sessionFactory.openSession();
		try {
			CompanyClient companyClient = new CompanyClient();
			companyClient = (CompanyClient) session.get(CompanyUser.class, id);
			if (log.isDebugEnabled()) {
				log.debug("Loaded Company client " + companyClient.toString());
			}
			return companyClient;
		} catch (RuntimeException ex) {
			log.error(ex);
			return null;
		}
	}

	@Override
	public boolean validateCompanyClientforSave(CompanyClient companyClient) {
		boolean status = false;
		if (companyClient.getUser_name() != null && companyClient.getUser_name() != ""
				&& companyClient.getEmail() != null && companyClient.getEmail() != ""
				&& companyClient.getCompany() != null) {
			status = true;
		} else {
			status = false;
		}
		return status;
	}

	@Override
	public boolean validateCompanyClientforUpdateandDelete(CompanyClient companyClient) {
		boolean status = false;
		if (companyClient.getId() != null && companyClient.getUser_name() != null
				&& companyClient.getUser_name() != "" && companyClient.getEmail() != null
				&& companyClient.getEmail() != "" && companyClient.getLast_modified() != null
				&& companyClient.getLast_modified() != "" && companyClient.getCompany() != null) {
			// check whether the data is null or not
			if (this.isCompanyClientUserNameExist(companyClient.getUser_name())) {
				// check whether the given user name is not changed
				CompanyClient saveCompanyClient = this.getCompanyClient(companyClient
						.getUser_name());
				if (saveCompanyClient.getId().equals(companyClient.getId())) {
					status = true;
					if (log.isDebugEnabled()) {
						log.debug("Validation Pass");
					}
				} else {
					status = false;
					if (log.isDebugEnabled()) {
						log.debug("Validation fail due to User Name changed in given object- This is the saved ID:"
								+ saveCompanyClient.getId()
								+ " This is the given ID:"
								+ companyClient.getId());
					}
				}
			} else {
				status = false;
				if (log.isDebugEnabled()) {
					log.debug("Validation fail due to CompanyClient userName:"
							+ companyClient.getUser_name() + " doesn't exist");
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

	@Override
	public List<CompanyClient> getAllCompanyClientsWithPagination(int page, int recordePerPage) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			Criteria cr = session.createCriteria(CompanyClient.class);
			cr.setFirstResult((page - 1) * recordePerPage);
			cr.setMaxResults(recordePerPage);
			List<CompanyClient> companyClientAll = cr.list();
			tr.commit();

			if (companyClientAll.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("Company Clients are not exist");
				}
				return null;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Found " + companyClientAll.size() + " Company Clients");
				}
				return companyClientAll;
			}
		} catch (RuntimeException ex) {
			log.error(ex);
			if (tr != null) {
				tr.rollback(); // roll back the transaction due to runtime error
			}
			return null;
		}
	}

}

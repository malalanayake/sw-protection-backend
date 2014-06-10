package com.sw.protection.backend.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hazelcast.core.IMap;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.TraceDAO;
import com.sw.protection.backend.entity.Trace;

/**
 * Trace operation implementation
 * 
 * @author dinuka
 * 
 */
@Repository
public class TraceDAOImpl implements TraceDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public static final Logger log = Logger.getLogger(TraceDAOImpl.class.getName());

	/**
	 * Maintain Locks over the cluster
	 */
	private static volatile IMap<Long, Object> LOCK_MAP = SharedInMemoryData.getInstance().getMap(
			SharedInMemoryData.DB_LOCKS.TRACE_DAO);

	@Override
	public List<Trace> getAllTraceByTypeAndUserName(ObjectType type, String userName) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			List<Trace> traceAll = session
					.getNamedQuery(Trace.Constants.NAME_QUERY_FIND_ALL_BY_TYPE_AND_USER_NAME)
					.setParameter(Trace.Constants.PARAM_TYPE_NAME, type)
					.setParameter(Trace.Constants.PARAM_TYPE_USER_NAME, userName).list();
			tr.commit();

			if (traceAll.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("Traces are not exist for Type: " + type + " and User name: "
							+ userName);
				}
				return null;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Found " + traceAll.size() + " Traces for Type: " + type
							+ " and User name: " + userName);
				}
				return traceAll;
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
	public List<Trace> getAllTraceByAPIName(String apiName) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			List<Trace> traceAll = session
					.getNamedQuery(Trace.Constants.NAME_QUERY_FIND_ALL_BY_API_NAME)
					.setParameter(Trace.Constants.PARAM_API_NAME, apiName).list();
			tr.commit();

			if (traceAll.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("Traces are not exist for Api name: " + apiName);
				}
				return null;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Found " + traceAll.size() + " Traces for Api name: " + apiName);
				}
				return traceAll;
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
	public List<Trace> getAllTraceByAffectedTypeAndUserName(ObjectType affectedType,
			String affectedUserName) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			List<Trace> traceAll = session
					.getNamedQuery(
							Trace.Constants.NAME_QUERY_FIND_ALL_BY_AFFECTED_TYPE_AND_USER_NAME)
					.setParameter(Trace.Constants.PARAM_AFFECTED_TYPE_NAME, affectedType)
					.setParameter(Trace.Constants.PARAM_AFFECTED_TYPE_USER_NAME, affectedUserName)
					.list();
			tr.commit();

			if (traceAll.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("Traces are not exist for affected Type: " + affectedType
							+ " and affected User name: " + affectedUserName);
				}
				return null;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Found " + traceAll.size() + " Traces for affected Type: "
							+ affectedType + " and affected User name: " + affectedUserName);
				}
				return traceAll;
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
	public Trace getTrace(Long ID) {
		Session session = sessionFactory.openSession();
		try {
			Trace trace = new Trace();
			trace = (Trace) session.get(Trace.class, ID);
			if (log.isDebugEnabled()) {
				log.debug("Loaded Trace " + trace.toString());
			}
			return trace;
		} catch (RuntimeException ex) {
			log.error(ex);
			return null;
		}
	}

	@Override
	public void deleteTrace(Trace trace) throws OperationRollBackException {
		Transaction tr = null;
		OperationRollBackException operationRollBackException = null;
		try {
			// Lock by trace ID
			LOCK_MAP.lock(trace.getId());
			if (log.isDebugEnabled()) {
				log.debug("Locked delete operation by Trace ID " + trace.getId());
			}

			// check whether the recode is exist
			if (getTrace(trace.getId()) != null) {
				Session session = sessionFactory.getCurrentSession();
				tr = session.beginTransaction();
				session.delete(trace);
				tr.commit();
				if (log.isDebugEnabled()) {
					log.debug("Delete Trace" + trace.toString());
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Trace is not exist " + trace.toString());
				}
				// TODO:Create Exception
			}
		} catch (RuntimeException ex) {
			log.error(ex);
			if (tr != null) {
				tr.rollback(); // roll back the transaction due to runtime error
				operationRollBackException = new OperationRollBackException(ex);
			}
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("Releasing LOCK by Trace ID " + trace.getId());
			}
			// Unlock the lock by trace ID
			LOCK_MAP.unlock(trace.getId());

			if (operationRollBackException != null) {
				throw operationRollBackException;
			}
		}

	}

	@Override
	public Trace saveTrace(Trace trace) {
		Transaction tr = null;
		Trace traceReturn = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			tr = session.beginTransaction();
			session.save(trace);
			traceReturn = trace;
			tr.commit();
			if (log.isDebugEnabled()) {
				log.debug("Save Trace " + trace.toString());
			}
		} catch (RuntimeException ex) {
			log.error(ex);
			if (tr != null) {
				tr.rollback(); // roll back the transaction due to runtime error
			}
			// TODO: Throw exception
		}
		return traceReturn;
	}

}

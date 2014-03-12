package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.entity.Trace;

/**
 * Interface which is provide the Trace operations.
 * 
 * @author dinuka
 * 
 */
public interface TraceDAO {
    /**
     * Get all Traces by type and type user name as an example type:
     * COMPANY_USER userName: companyUser1
     * 
     * @param type
     *            - Type of user
     * @param userName
     *            - user name of the operator
     * @return - List of Trace
     */
    public List<Trace> getAllTraceByTypeAndUserName(ObjectType type, String userName);

    /**
     * Get all Traces by API name as an example apiName: ADMIN_API
     * 
     * @param apiName
     *            - name of given API
     * @return - List of Trace
     */
    public List<Trace> getAllTraceByAPIName(String apiName);

    /**
     * Get all Traces by affected type and ID as an example affectedType:
     * ADMIN_USER, affectedUserName: adminUser1
     * 
     * @param affectedType
     *            - Type name
     * @param affectedUserName
     *            - object user name
     * @return - List of Trace
     */
    public List<Trace> getAllTraceByAffectedTypeAndUserName(ObjectType affectedType, String affectedUserName);

    /**
     * Get trace by trace id
     * 
     * @param ID
     * @return - Trace object
     */
    public Trace getTrace(Long ID);

    /**
     * Delete the Trace object
     * 
     * @param trace
     *            - Trace object to be deleted
     */
    public void deleteTrace(Trace trace) throws OperationRollBackException;

    /**
     * Save the Trace object
     * 
     * @param trace
     *            - Trace object to be save
     */
    public Trace saveTrace(Trace trace);
}

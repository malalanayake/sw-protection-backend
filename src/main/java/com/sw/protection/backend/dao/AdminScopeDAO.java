package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.entity.AdminScope;

/**
 * Interface which is provide the AdminScope operations.
 * 
 * @author dinuka
 */
public interface AdminScopeDAO {
    /**
     * Get all admin scope data
     * 
     * @param userName
     *            - given username
     * @return - List of Scopes
     */
    public List<AdminScope> getAllAdminScopes(String userName);

    /**
     * Save new admin scope data
     * 
     * @param adminScope
     *            - the admin scope going to be save
     */
    public AdminScope saveNewAdminScope(AdminScope adminScope) throws DuplicateRecordException,
	    OperationRollBackException;

    /**
     * Delete admin scope data
     * 
     * @param adminScope
     *            - the admin scope to be deleted
     */
    public void deleteAdminScope(AdminScope adminScope) throws RecordAlreadyModifiedException,
	    OperationRollBackException;

    /**
     * Update admin scope data
     * 
     * @param adminScope
     *            - edited admin scope
     */
    public void updateAdminScope(AdminScope adminScope) throws RecordAlreadyModifiedException,
	    OperationRollBackException;

    /**
     * Get specific admin scope data
     * 
     * @param userName
     *            - username
     * @param apiName
     *            - API name
     * @return - specific admin scope
     */
    public AdminScope getAdminScope(String userName, String apiName);

    /**
     * Check whether the admin has correct access
     * 
     * @param userName
     *            - username
     * @param apiName
     *            - relevent API name
     * @param operation
     *            - operation such as GET,POST,PUT,DELETE
     * @return
     */
    public boolean isAccessGrantedFor(String userName, String apiName, APIOperations operation);

}

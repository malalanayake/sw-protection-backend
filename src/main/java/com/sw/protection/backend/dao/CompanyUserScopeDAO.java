package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.entity.CompanyUserScope;

/**
 * Interface which is provide the Company User Scope operations.
 * 
 * @author dinuka
 */
public interface CompanyUserScopeDAO {
    /**
     * Get all company user scope data
     * 
     * @param userName
     *            - given username
     * @return - List of Scopes
     */
    public List<CompanyUserScope> getAllCompanyUserScopes(String userName);

    /**
     * Save new Company user scope data
     * 
     * @param companyUserScope
     *            - the company user scope going to be save
     */
    public CompanyUserScope saveNewCompanyUserScope(CompanyUserScope companyUserScope) throws DuplicateRecordException,
	    OperationRollBackException;

    /**
     * Delete Company user scope data
     * 
     * @param companyUserScope
     *            - the company user scope to be deleted
     */
    public CompanyUserScope deleteCompanyUserScope(CompanyUserScope companyUserScope)
	    throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Update Company user scope data
     * 
     * @param companyUserScope
     *            - edited company user scope
     */
    public CompanyUserScope updateCompanyUserScope(CompanyUserScope companyUserScope)
	    throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Get specific Company user scope data
     * 
     * @param userName
     *            - username
     * @param apiName
     *            - API name
     * @return - specific company user scope
     */
    public CompanyUserScope getCompanyUserScope(String userName, String apiName);

    /**
     * Check whether the Company user has correct access
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

    /**
     * Validating the CompanyUserScope data before save in service level
     * 
     * @param companyUserScope
     * @return
     */
    public boolean validateCompanyUserScopeforSave(CompanyUserScope companyUserScope);

    /**
     * Validating the CompanyUserScope data before update and delete in service
     * level
     * 
     * @param companyUserScope
     * @return
     */
    public boolean validateCompanyUserScopeforUpdateandDelete(CompanyUserScope companyUserScope);
}

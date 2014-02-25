package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.entity.CompanyUserScope;

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
    public void saveNewCompanyUserScope(CompanyUserScope companyUserScope);

    /**
     * Delete Company user scope data
     * 
     * @param companyUserScope
     *            - the company user scope to be deleted
     */
    public void deleteCompanyUserScope(CompanyUserScope companyUserScope);

    /**
     * Update Company user scope data
     * 
     * @param companyUserScope
     *            - edited company user scope
     */
    public void updateCompanyUserScope(CompanyUserScope companyUserScope);

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
}

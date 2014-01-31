package com.sw.protection.backend.dao;

import java.util.List;

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
    public void saveNewAdminScope(AdminScope adminScope);

    /**
     * Delete admin scope data
     * 
     * @param adminScope
     *            - the admin scope to be deleted
     */
    public void deleteAdminScope(AdminScope adminScope);

    /**
     * Update admin scope data
     * 
     * @param adminScope
     *            - edited admin scope
     */
    public void updateAdminScope(AdminScope adminScope);

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

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.CompanyUser;
import java.util.List;

/**
 * Interface which is provide the Company User operations.
 * 
 * @author dinuka
 */
public interface CompanyUserDAO {
    /**
     * Get all company users
     * 
     * @return - List of company users
     */
    public List<CompanyUser> getAllUsers();

    /**
     * Get specific company user
     * 
     * @param userName
     *            - user name
     * @return - Specific company user object
     */
    public CompanyUser getUser(String userName);

    /**
     * Update specific company user
     * 
     * @param user
     *            - Company user object with updated values
     */
    public void updateUser(CompanyUser user);

    /**
     * Delete specific user
     * 
     * @param user
     *            - Company user object to be deleted
     */
    public void deleteUser(CompanyUser user);

    /**
     * Save user
     * 
     * @param user
     *            - Company user object to be save
     */
    public void saveUser(CompanyUser user);

    /**
     * Check whether the company user name exist
     * 
     * @param userName
     *            - Company user's username
     * @return - True/False
     */
    public boolean isCompanyUserNameExist(String userName);

    /**
     * Load all company user scopes and return
     * 
     * @param id
     *            - Specific company user ID
     * @return - Specific company user
     */
    public CompanyUser loadAllPropertiesOfCompanyUser(Long id);
}

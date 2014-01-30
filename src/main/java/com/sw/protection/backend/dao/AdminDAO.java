/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.Admin;

import java.util.List;

/**
 * Interface which is provide the Admin operations.
 * 
 * @author dinuka
 */
public interface AdminDAO {
    /**
     * Get all admin users
     * 
     * @return Set of Admins
     */
    public List<Admin> getAllAdmins();

    /**
     * Get specific admin user
     * 
     * @param userName
     *            - Admin user name
     * @return - Specific Admin
     */
    public Admin getAdmin(String userName);

    /**
     * Load all properties of specific admin user
     * 
     * @param id
     *            - Admin ID
     * @return - Specific Admin
     */
    public Admin loadAllPropertiesOfAdmin(Long id);

    /**
     * Update admin user
     * 
     * @param admin
     *            - new Admin with updated values
     */
    public void updateAdmin(Admin admin);

    /**
     * Delete admin user
     * 
     * @param admin
     *            - Admin to be deleted
     */
    public void deleteAdmin(Admin admin);

    /**
     * Save admin user
     * 
     * @param admin
     *            - new Admin
     */
    public void saveAdmin(Admin admin);

    /**
     * Check whether the admin user name exist
     * 
     * @param userName
     *            - Admin username
     * @return - True/False
     */
    public boolean isAdminUserNameExist(String userName);
}

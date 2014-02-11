/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.CompanyUser;
import java.util.List;

/**
 * Interface which is provide the User operations.
 * 
 * @author dinuka
 */
public interface UserDAO {
    /*
     * Get all Users
     */
    public List<CompanyUser> getAllUsers();

    /*
     * Get specific user
     */
    public CompanyUser getUser(int companyId);

    /*
     * Update user
     */
    public void updateUser(CompanyUser user);

    /*
     * Delete user
     */
    public void deleteUser(CompanyUser user);

    /*
     * Save user
     */
    public void saveUser(CompanyUser user);
}

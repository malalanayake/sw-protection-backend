package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.SuperAdmin;

import java.util.List;

/**
 * Interface which is provide the Admin operations.
 * 
 * @author dinuka
 */
public interface SuperAdminDAO {
    /**
     * Get all super admin users
     * 
     * @return Set of Admins
     */
    public List<SuperAdmin> getAllAdmins();

    /**
     * Get specific super admin user
     * 
     * @param userName
     *            - Super Admin user name
     * @return - Specific Super Admin
     */
    public SuperAdmin getSuperAdmin(String userName);

    /**
     * Update super admin user
     * 
     * @param admin
     *            - new Super Admin with updated values
     * 
     */
    public void updateSuperAdmin(SuperAdmin admin);

    /**
     * Delete admin user
     * 
     * @param admin
     *            - Admin to be deleted
     */
    public void deleteSuperAdmin(SuperAdmin admin);

    /**
     * Save super admin user
     * 
     * @param admin
     *            - new super Admin
     */
    public void saveSuperAdmin(SuperAdmin admin);

    /**
     * Check whether the super admin user name exist
     * 
     * @param userName
     *            - Super Admin username
     * @return - True/False
     */
    public boolean isSuperAdminUserNameExist(String userName);
}

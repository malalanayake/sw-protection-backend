package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.entity.SuperAdmin;

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
    public void updateSuperAdmin(SuperAdmin admin) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Delete admin user
     * 
     * @param admin
     *            - Admin to be deleted
     */
    public void deleteSuperAdmin(SuperAdmin admin) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Save super admin user
     * 
     * @param admin
     *            - new super Admin
     */
    public void saveSuperAdmin(SuperAdmin admin) throws DuplicateRecordException, OperationRollBackException;

    /**
     * Check whether the super admin user name exist
     * 
     * @param userName
     *            - Super Admin username
     * @return - True/False
     */
    public boolean isSuperAdminUserNameExist(String userName);
}

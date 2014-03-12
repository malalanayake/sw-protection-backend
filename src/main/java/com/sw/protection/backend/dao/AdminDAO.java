package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.entity.Admin;

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
     * Get all admin with pagination
     * 
     * @param page
     *            - page number
     * @param recordePerPage
     *            - maximum recodes per page
     * @return
     */
    public List<Admin> getAllAdminsWithPagination(int page, int recordePerPage);

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
    public void updateAdmin(Admin admin) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Delete admin user
     * 
     * @param admin
     *            - Admin to be deleted
     */
    public void deleteAdmin(Admin admin) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Save admin user
     * 
     * @param admin
     *            - new Admin
     */
    public Admin saveAdmin(Admin admin) throws DuplicateRecordException, OperationRollBackException;

    /**
     * Check whether the admin user name exist
     * 
     * @param userName
     *            - Admin username
     * @return - True/False
     */
    public boolean isAdminUserNameExist(String userName);

    /**
     * Check whether the admin user email exist
     * 
     * @param email
     *            - Admin Email
     * @return - True/False
     */
    public boolean isAdminUserEmailExist(String email);
}

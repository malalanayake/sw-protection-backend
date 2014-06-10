package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.entity.CompanyUser;

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
	 * Get all CompanyUsers with pagination
	 * 
	 * @param page
	 *            - page number
	 * @param recordePerPage
	 *            - maximum recodes per page
	 * @return
	 */
	public List<CompanyUser> getAllCompanyUsersWithPagination(int page, int recordePerPage);

	/**
	 * Update specific company user
	 * 
	 * @param user
	 *            - Company user object with updated values
	 */
	public CompanyUser updateUser(CompanyUser user) throws RecordAlreadyModifiedException,
			OperationRollBackException;

	/**
	 * Delete specific user
	 * 
	 * @param user
	 *            - Company user object to be deleted
	 */
	public CompanyUser deleteUser(CompanyUser user) throws RecordAlreadyModifiedException,
			OperationRollBackException;

	/**
	 * Save user
	 * 
	 * @param user
	 *            - Company user object to be save
	 */
	public CompanyUser saveUser(CompanyUser user) throws DuplicateRecordException,
			OperationRollBackException;

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

	/**
	 * This method is used to validate the given CompanyUser. This is going to
	 * use in service class before saving the object
	 * 
	 * @param admin
	 *            - given Company object
	 * @return
	 */
	public boolean validateCompanyUserforSave(CompanyUser companyUser);

	/**
	 * This method is used to validate the given CompanyUser. This is going to
	 * use in service class before update or delete the object
	 * 
	 * @param admin
	 *            - given Company object
	 * @return
	 */
	public boolean validateCompanyUserforUpdateandDelete(CompanyUser companyUser);
}

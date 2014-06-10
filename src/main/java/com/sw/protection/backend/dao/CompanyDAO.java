package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.entity.Company;

/**
 * Interface which is provide the CompanyDAO operations.
 * 
 * @author dinuka
 */
public interface CompanyDAO {
	/**
	 * Get All company data
	 * 
	 * @return - List of all company objects
	 */
	public List<Company> getAllCompanies();

	/**
	 * Get specific company data
	 * 
	 * @param companyUserName
	 *            - Company user name
	 * @return - specific company object
	 */
	public Company getCompany(String companyUserName);

	/**
	 * Get all Companies with pagination
	 * 
	 * @param page
	 *            - page number
	 * @param recordePerPage
	 *            - maximum recodes per page
	 * @return
	 */
	public List<Company> getAllCompaniesWithPagination(int page, int recordePerPage);

	/**
	 * Update company
	 * 
	 * @param company
	 *            - Company object with updated values
	 */
	public Company updateCompany(Company company) throws RecordAlreadyModifiedException,
			OperationRollBackException;

	/**
	 * Delete specific company
	 * 
	 * @param company
	 *            - Company object to be deleted
	 */
	public Company deleteCompany(Company company) throws RecordAlreadyModifiedException,
			OperationRollBackException;

	/**
	 * Save company data
	 * 
	 * @param company
	 *            - New company object to be saved
	 */
	public Company saveCompany(Company company) throws DuplicateRecordException,
			OperationRollBackException;

	/**
	 * Load all properties of company
	 * 
	 * @param id
	 *            - specific company object
	 * @return - Company object with all related data
	 */
	public Company loadAllPropertiesOfCompany(Long id);

	/**
	 * Check whether the company user name already exist
	 * 
	 * @param userName
	 *            - user name
	 * @return - true/false
	 */
	public boolean isCompanyUserNameExist(String userName);

	/**
	 * This method is used to validate the given Company. This is going to use
	 * in service class before saving the object
	 * 
	 * @param admin
	 *            - given Company object
	 * @return
	 */
	public boolean validateCompanyforSave(Company company);

	/**
	 * This method is used to validate the given Company. This is going to use
	 * in service class before update or delete the object
	 * 
	 * @param admin
	 *            - given Company object
	 * @return
	 */
	public boolean validateCompanyforUpdateandDelete(Company company);
}

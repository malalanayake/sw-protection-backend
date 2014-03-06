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
     * Update company
     * 
     * @param company
     *            - Company object with updated values
     */
    public void updateCompany(Company company) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Delete specific company
     * 
     * @param company
     *            - Company object to be deleted
     */
    public void deleteCompany(Company company) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Save company data
     * 
     * @param company
     *            - New company object to be saved
     */
    public void saveCompany(Company company) throws DuplicateRecordException, OperationRollBackException;

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
}

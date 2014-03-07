package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.entity.CompanyClient;

/**
 * Interface which is provide the ComapanyClient operations.
 * 
 * @author dinuka
 */
public interface CompanyClientDAO {
    /**
     * Get list of all company clients
     * 
     * @return - List of company clients
     */
    public List<CompanyClient> getAllCompanyClients();

    /**
     * Get specific company client
     * 
     * @param userName
     *            - client user name
     * @return - Specific client object
     */
    public CompanyClient getCompanyClient(String userName);

    /**
     * Update specifc client
     * 
     * @param companyClient
     *            - Client object to be updated
     */
    public void updateCompanyClient(CompanyClient companyClient) throws RecordAlreadyModifiedException,
	    OperationRollBackException;

    /**
     * Delete specific client object
     * 
     * @param companyClient
     *            - Specific client object to be deleted
     */
    public void deleteCompanyClient(CompanyClient companyClient) throws RecordAlreadyModifiedException,
	    OperationRollBackException;

    /**
     * Save Company client
     * 
     * @param companyClient
     *            - Company Client object to be saved
     */
    public void saveCompanyClient(CompanyClient companyClient) throws DuplicateRecordException,
	    OperationRollBackException;

    /**
     * Check whether the company client already exist
     * 
     * @param userName
     *            - Client user name
     * @return - TRUE/FALSE
     */
    public boolean isCompanyClientUserNameExist(String userName);

    /**
     * Load all properties of company clients
     * 
     * @param id
     *            - Specific company client ID
     * @return - Specific company client with all relevant data
     */
    public CompanyClient loadAllPropertiesOfCompanyClient(Long id);
}

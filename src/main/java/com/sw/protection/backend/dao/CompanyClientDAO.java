package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.entity.CompanyClient;

/**
 * Interface which is provide the ComapanyClient operations.
 * 
 * @author dinuka
 */
public interface CompanyClientDAO {
    /*
     * Get all company clients
     */
    public List<CompanyClient> getAllCompanyClients();

    /*
     * Get specific company client
     */
    public CompanyClient getCompanyClient(String userName);

    /*
     * Update company client
     */
    public void updateCompanyClient(CompanyClient companyClient);

    /*
     * Delete company client
     */
    public void deleteCompanyClient(CompanyClient companyClient);

    /*
     * Save company client
     */
    public void saveCompanyClient(CompanyClient companyClient);
}

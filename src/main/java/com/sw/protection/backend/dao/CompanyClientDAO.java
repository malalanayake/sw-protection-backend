/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.CompanyClient;
import java.util.List;

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
    public CompanyClient getCompanyClient(int clientId);

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

package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.entity.CompanySW;

/**
 * Interface which is provide the Company Software operations.
 * 
 * @author dinuka
 */
public interface CompanySWDAO {
    /**
     * Get all company softwares
     * 
     * @return - List of softwares
     */
    public List<CompanySW> getAllCompanySWs();

    /**
     * Get specific company software
     * 
     * @param companyUserName
     *            - company user name
     * @param softwareName
     *            - Software name
     * @return - Specific company software object
     */
    public CompanySW getCompanySW(String companyUserName, String softwareName);

    /**
     * Update company software
     * 
     * @param companySW
     *            - software going to be updated
     */
    public void updateCompanySW(CompanySW companySW);

    /**
     * Delete specific company software
     * 
     * @param companySW
     *            - software going to be deleted
     */
    public void deleteCompanySW(CompanySW companySW);

    /**
     * Save company software
     * 
     * @param companySW
     *            - software going to be saved
     */
    public void saveCompanySW(CompanySW companySW);

    /**
     * Check whether the company software is exist
     * 
     * @param companyUserName
     *            - company user name
     * @param softwareName
     *            - software name
     * @return - TRUE/FALSE
     */
    public boolean isCompanySWExist(String companyUserName, String softwareName);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.entity.CompanySW;

/**
 * Interface which is provide the Company Software operations.
 * 
 * @author dinuka
 */
public interface CompanySWDAO {
    /*
     * Get all company softwares
     */
    public List<CompanySW> getAllCompanySWs();

    /*
     * Get specific company software
     */
    public CompanySW getCompanySW(int companyId);

    /*
     * Update company software
     */
    public void updateCompanySW(CompanySW companySW);

    /*
     * Delete company software
     */
    public void deleteCompanySW(CompanySW companySW);

    /*
     * Save company software
     */
    public void saveCompanySW(CompanySW companySW);
}

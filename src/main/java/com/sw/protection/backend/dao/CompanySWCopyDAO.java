/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.entity.CompanySWCopy;

/**
 * Interface which is provide the Company Software Copy operations.
 * 
 * @author dinuka
 */
public interface CompanySWCopyDAO {
    /*
     * Get all company software copies
     */
    public List<CompanySWCopy> getAllCompanySWCopies();

    /*
     * Get specific company software copy
     */
    public CompanySWCopy getCompanySWCopy(int companyId);

    /*
     * Update company software copy
     */
    public void updateCompanySWCopy(CompanySWCopy companySWCopy);

    /*
     * Delete company software copy
     */
    public void deleteCompanySWCopy(CompanySWCopy companySWCopy);

    /*
     * Save company software copy
     */
    public void saveCompanySWCopy(CompanySWCopy companySWCopy);
}

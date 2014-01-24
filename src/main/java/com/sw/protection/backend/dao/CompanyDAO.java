/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.Company;
import java.util.List;

/**
 *
 * @author dinuka
 */
public interface CompanyDAO {
  /*
   Get all companies 
    */
   public List<Company> getAllCompanies();
   
   /*
   Get specific company
   */
   public Company getCompany(int companyId);
   
   /*
   Update company
   */
   public void updateCompany(Company company);
   
   /*
   Delete company
   */
   public void deleteCompany(Company company); 
   
   /*
   Save company
   */
   public void saveCompany(Company company); 
}

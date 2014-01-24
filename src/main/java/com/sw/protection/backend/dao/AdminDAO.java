/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.Admin;
import java.util.List;

/**
 *
 * @author dinuka
 */
public interface AdminDAO {
   /*
   Get all admin users
    */
   public List<Admin> getAllAdmins();
   
   /*
   Get specific admin user
   */
   public Admin getAdmin(String userName);
   
   /*
   Update admin user
   */
   public void updateAdmin(Admin admin);
   
   /*
   Delete admin user
   */
   public void deleteAdmin(Admin admin);  
   
   /*
   Save admin user
   */
   public void saveAdmin(Admin admin);
}

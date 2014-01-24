/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao;

import com.sw.protection.backend.entity.User;
import java.util.List;

/**
 *
 * @author dinuka
 */
public interface UserDAO {
     /*
   Get all Users 
    */
   public List<User> getAllUsers();
   
   /*
   Get specific user
   */
   public User getUser(int companyId);
   
   /*
   Update user
   */
   public void updateUser(User user);
   
   /*
   Delete user
   */
   public void deleteUser(User user); 
   
   /*
   Save user
   */
   public void saveUser(User user); 
}

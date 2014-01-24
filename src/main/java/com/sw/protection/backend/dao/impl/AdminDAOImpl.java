/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao.impl;

import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author dinuka
 */
public class AdminDAOImpl implements AdminDAO{
    private Session session;

    @Override
    public List<Admin> getAllAdmins() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Admin getAdmin(String userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateAdmin(Admin admin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAdmin(Admin admin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveAdmin(Admin admin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

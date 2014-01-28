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
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * 
 * @author dinuka
 */
public class AdminDAOImpl implements AdminDAO {
    private Session session;

    @Override
    public List<Admin> getAllAdmins() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Admin getAdmin(String userName) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();	
	Transaction tr = session.beginTransaction();
	List<Admin> adminAll = session.getNamedQuery(
		Admin.Constants.NAME_QUERY_FIND_BY_USER_NAME).setParameter(
		Admin.Constants.PARAM_USER_NAME, userName).list();
	tr.commit();
	if (adminAll.isEmpty()) {
	    return null;
	} else {
	    return adminAll.get(0);
	}

    }

    @Override
    public void updateAdmin(Admin admin) {
	throw new UnsupportedOperationException("Not supported yet."); // To
	// change
	// body
	// of
	// generated
	// methods,
	// choose
	// Tools
	// |
	// Templates.
    }

    @Override
    public void deleteAdmin(Admin admin) {
	throw new UnsupportedOperationException("Not supported yet."); // To
	// change
	// body
	// of
	// generated
	// methods,
	// choose
	// Tools
	// |
	// Templates.
    }

    @Override
    public void saveAdmin(Admin admin) {
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tr = session.beginTransaction();
	session.save(admin);
	tr.commit();
    }

    @Override
    public Admin loadAllPropertiesOfAdmin(Long id) {
	session = HibernateUtil.getSessionFactory().openSession();
	Admin admin = new Admin();
	admin = (Admin) session.get(Admin.class, id);	
	return admin;

    }

    @Override
    public boolean isAdminUserNameExist(String userName) {
	if (this.getAdmin(userName) == null) {
	    return false;
	} else {
	    return true;
	}
    }

}

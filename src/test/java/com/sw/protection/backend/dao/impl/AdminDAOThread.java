package com.sw.protection.backend.dao.impl;

import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;

public class AdminDAOThread implements Runnable {
    Admin admin = new Admin();

    AdminDAO adminDAO;

    public AdminDAOThread(Admin newAdmin, AdminDAO adminDAO) {
	admin = newAdmin;
	this.adminDAO = adminDAO;
    }

    @Override
    public void run() {
	try {
	    adminDAO.updateAdmin(admin);
	} catch (Exception ex) {

	}
    }

}

package com.sw.protection.backend.dao.impl;

import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;

public class AdminDAOThread implements Runnable {
    Admin admin = new Admin();

    public AdminDAOThread(Admin newAdmin) {
	admin = newAdmin;
    }

    @Override
    public void run() {
	AdminDAO adminDao = new AdminDAOImpl();
	adminDao.updateAdmin(admin);
    }

}

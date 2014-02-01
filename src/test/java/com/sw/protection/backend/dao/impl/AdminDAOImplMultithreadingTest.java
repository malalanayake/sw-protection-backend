package com.sw.protection.backend.dao.impl;

import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;

public class AdminDAOImplMultithreadingTest implements Runnable {
    Admin admin = new Admin();

    public AdminDAOImplMultithreadingTest(Admin newAdmin) {
	admin = newAdmin;
    }

    @Override
    public void run() {
	AdminDAO adminDao = new AdminDAOImpl();
	adminDao.updateAdmin(admin);
    }

}

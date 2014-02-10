package com.sw.protection.backend.dao.impl;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
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

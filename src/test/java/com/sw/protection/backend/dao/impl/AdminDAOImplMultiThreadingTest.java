package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;

@Test(groups = { "AdminDAOImplMultiThreadingTest" }, dependsOnGroups = { "AdminScopeDAOImplTest" })
public class AdminDAOImplMultiThreadingTest {
    public Logger log = Logger.getLogger(AdminDAOImplMultiThreadingTest.class.getName());

    Admin admin = new Admin();

    @BeforeClass()
    public void setupParamValidation() {
	// Test class setup code with autowired classes goes here
    }

    public void concurencyTesting() {
	Admin pre_admin = new Admin();
	pre_admin.setUser_name("dinuka");
	pre_admin.setPass_word("pw");
	pre_admin.setEmail("dinuka@234.com");
	pre_admin.setName("Dinuka");
	pre_admin.setApi_key(UUID.randomUUID().toString());
	pre_admin.setDate_time(Formatters.formatDate(new Date()));
	pre_admin.setLast_modified(Formatters.formatDate(new Date()));

	Admin pre_admin2 = new Admin();
	pre_admin2.setUser_name("malinda");
	pre_admin2.setPass_word("pw");
	pre_admin2.setEmail("malinda@234.com");
	pre_admin2.setName("Malinda");
	pre_admin2.setApi_key(UUID.randomUUID().toString());
	pre_admin2.setDate_time(Formatters.formatDate(new Date()));
	pre_admin2.setLast_modified(Formatters.formatDate(new Date()));

	AdminDAO adminDao = AppContext.getInstance().getBean(AdminDAO.class);
	try {
	    adminDao.saveAdmin(pre_admin);
	    adminDao.saveAdmin(pre_admin2);
	} catch (Exception ex) {

	}

	ExecutorService executor = Executors.newFixedThreadPool(10);
	Admin admin1 = adminDao.getAdmin("dinuka");

	admin1 = adminDao.loadAllPropertiesOfAdmin(admin1.getId());
	for (int i = 0; i < 10; i++) {

	    admin1.setEmail("thread" + i + "@gmail.com");

	    AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	    Runnable worker = new AdminDAOThread(admin1, adminDAO);
	    log.info("Start Editing thread " + i);
	    executor.execute(worker);
	}
	executor.shutdown();

	AdminDAO adminDao3 = AppContext.getInstance().getBean(AdminDAO.class);
	// same recode access from different threads form different objects
	ExecutorService executor3 = Executors.newFixedThreadPool(10);
	Admin admin13 = adminDao3.getAdmin("dinuka");

	admin13 = adminDao3.loadAllPropertiesOfAdmin(admin13.getId());
	for (int i = 0; i < 10; i++) {

	    admin13.setEmail("thread" + i + "@gmail.com");

	    AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);

	    Runnable worker = new AdminDAOThread(admin13, adminDAO);
	    log.info("Start Editing thread " + i);
	    executor3.execute(worker);
	}
	executor3.shutdown();

	AdminDAO adminDao1 = AppContext.getInstance().getBean(AdminDAO.class);
	ExecutorService executor1 = Executors.newFixedThreadPool(10);
	Admin admin2 = adminDao1.getAdmin("malinda");

	admin2 = adminDao1.loadAllPropertiesOfAdmin(admin2.getId());

	for (int i = 0; i < 10; i++) {

	    admin2.setEmail("thread" + i + "@gmail.com");

	    AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);

	    Runnable worker = new AdminDAOThread(admin2, adminDAO);
	    log.info("Start Editing thread " + i);
	    executor1.execute(worker);
	}
	executor1.shutdown();

	// wait until thread shutdown
	try {
	    executor.awaitTermination(1, TimeUnit.MINUTES);
	    executor1.awaitTermination(1, TimeUnit.MINUTES);
	    executor3.awaitTermination(1, TimeUnit.MINUTES);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// Wait until all threads are finish

    }

    @Test(dependsOnMethods = { "concurencyTesting" })
    public void testDeleteAllAdmins() {
	log.info("Start Test Delete Admin");
	String userName1 = "malinda";
	String userName2 = "dinuka";

	Admin admin1 = new Admin();
	Admin admin2 = new Admin();
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	admin1 = adminDAO.getAdmin(userName1);
	admin2 = adminDAO.getAdmin(userName2);
	try {
	    adminDAO.deleteAdmin(admin1);
	    assertEquals(adminDAO.isAdminUserNameExist(userName1), false);
	    adminDAO.deleteAdmin(admin2);
	    assertEquals(adminDAO.isAdminUserNameExist(userName2), false);
	} catch (Exception ex) {

	}
    }

}
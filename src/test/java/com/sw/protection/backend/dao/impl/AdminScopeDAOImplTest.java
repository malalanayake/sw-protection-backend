package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.ClearLocks;
import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.dao.AdminScopeDAO;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;

@Test(groups = { "AdminScopeDAOImplTest" }, dependsOnGroups = { "AdminDAOImplNGTest" })
public class AdminScopeDAOImplTest {
    public static final Logger log = Logger.getLogger(AdminScopeDAOImplTest.class.getName());

    @BeforeClass
    public static void setUpClass() throws Exception {
	log.info("Setup the initial data");
	Admin admin = new Admin();
	admin.setUser_name("dinuka");
	admin.setPass_word("pw");
	admin.setEmail("dinuka@234.com");
	admin.setName("Dinuka");
	admin.setApi_key(UUID.randomUUID().toString());
	admin.setDate_time(Formatters.formatDate(new Date()));

	Set<AdminScope> adminScopSet = admin.getAdminScopeSet();
	AdminScope adminScope1 = new AdminScope();
	adminScope1.setAdmin(admin);
	adminScope1.setApi_name(APINames.ADMIN);
	adminScope1.setDel(true);
	adminScope1.setGet(true);
	adminScope1.setPost(true);
	adminScope1.setPut(true);

	AdminScope adminScope2 = new AdminScope();
	adminScope2.setAdmin(admin);
	adminScope2.setApi_name(APINames.COMPANY);
	adminScope2.setDel(true);
	adminScope2.setGet(false);
	adminScope2.setPost(true);
	adminScope2.setPut(false);

	adminScopSet.add(adminScope1);
	adminScopSet.add(adminScope2);
	admin.setAdminScopeSet(adminScopSet);
	AdminDAO instance = new AdminDAOImpl();
	instance.saveAdmin(admin);

	Admin admin2 = new Admin();
	admin2.setUser_name("malinda");
	admin2.setPass_word("pw");
	admin2.setEmail("malinda@234.com");
	admin2.setName("Malinda");
	admin2.setApi_key(UUID.randomUUID().toString());
	admin2.setDate_time(Formatters.formatDate(new Date()));

	Set<AdminScope> adminScopSet2 = admin2.getAdminScopeSet();
	AdminScope adminScope12 = new AdminScope();
	adminScope12.setAdmin(admin2);
	adminScope12.setApi_name(APINames.ADMIN);
	adminScope12.setDel(true);
	adminScope12.setGet(true);
	adminScope12.setPost(true);
	adminScope12.setPut(true);

	AdminScope adminScope22 = new AdminScope();
	adminScope22.setAdmin(admin2);
	adminScope22.setApi_name(APINames.COMPANY);
	adminScope22.setDel(true);
	adminScope22.setGet(false);
	adminScope22.setPost(true);
	adminScope22.setPut(false);

	adminScopSet2.add(adminScope12);
	adminScopSet2.add(adminScope22);
	admin2.setAdminScopeSet(adminScopSet2);
	AdminDAO instance2 = new AdminDAOImpl();
	instance2.saveAdmin(admin2);
    }

    /**
     * Test get all admin scopes according to specific admin user
     */
    @Test()
    public void getAllAdminScopes() {
	log.info("Test Start get all admin scopes");
	AdminScopeDAO instance = new AdminScopeDAOImpl();
	List<AdminScope> list = instance.getAllAdminScopes("dinuka");
	log.info("Admin Scope List size:" + list.size());
	assertEquals(list.size(), 2);
	for (AdminScope scope : list) {
	    log.info(scope.toString());
	}
    }

    /**
     * Test save admin scope data
     */
    @Test(dependsOnMethods = { "getAllAdminScopes" })
    public void saveAdminScopes() {
	log.info("Test Start save admin scopes");
	AdminDAO adminDAO = new AdminDAOImpl();
	Admin admin = adminDAO.getAdmin("dinuka");

	AdminScopeDAO adminScopeDAO = new AdminScopeDAOImpl();

	AdminScope adminScope = new AdminScope();
	adminScope.setAdmin(admin);
	adminScope.setApi_name(APINames.USER);
	adminScope.setGet(false);
	adminScope.setPost(false);
	adminScope.setPut(false);
	adminScope.setDel(false);
	adminScopeDAO.saveNewAdminScope(adminScope);

	List<AdminScope> list = adminScopeDAO.getAllAdminScopes("dinuka");
	log.info("Admin Scope List size:" + list.size());
	assertEquals(list.size(), 3);

	Admin adminNew = adminDAO.loadAllPropertiesOfAdmin(admin.getId());
	log.info("Admin Scope List size:" + list.size());
	assertEquals(adminNew.getAdminScopeSet().size(), 3);
	for (AdminScope scope : adminNew.getAdminScopeSet()) {
	    log.info(scope.toString());
	}
    }

    /**
     * Test update admin scope data
     */
    @Test(dependsOnMethods = { "saveAdminScopes" })
    public void updateAdminScope() {
	log.info("Test Start update admin scopes");
	AdminDAO adminDAO = new AdminDAOImpl();
	Admin admin = adminDAO.getAdmin("dinuka");

	AdminScopeDAO adminScopeDAO = new AdminScopeDAOImpl();

	AdminScope adminScope = adminScopeDAO.getAdminScope("dinuka", APINames.USER);
	log.info("Admin Scope :" + adminScope.toString());
	assertEquals(adminScope.isDel(), false);
	assertEquals(adminScope.isGet(), false);
	assertEquals(adminScope.isPut(), false);
	assertEquals(adminScope.isPost(), false);

	adminScope.setDel(true);
	adminScope.setPut(true);
	adminScope.setPost(true);
	adminScope.setGet(true);

	adminScopeDAO.updateAdminScope(adminScope);
	AdminScope latest = adminScopeDAO.getAdminScope("dinuka", APINames.USER);
	log.info("Uptodate admin Scope :" + latest.toString());
	assertEquals(latest.isDel(), true);
	assertEquals(latest.isGet(), true);
	assertEquals(latest.isPut(), true);
	assertEquals(latest.isPost(), true);

    }

    @Test(dependsOnMethods = { "updateAdminScope" })
    public void isAccessGrantedFor() {
	AdminScopeDAO adminScopeDAO = new AdminScopeDAOImpl();
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.USER, APIOperations.GET), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.USER, APIOperations.DELETE), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.USER, APIOperations.POST), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.USER, APIOperations.PUT), true);

	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY, APIOperations.GET), false);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY, APIOperations.DELETE), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY, APIOperations.POST), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY, APIOperations.PUT), false);

	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.ADMIN, APIOperations.GET), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.ADMIN, APIOperations.DELETE), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.ADMIN, APIOperations.POST), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.ADMIN, APIOperations.PUT), true);

    }

    @Test(dependsOnMethods = { "isAccessGrantedFor" })
    public void concurencyTesting() {
	// AdminDAOImplMultithreadingTest adminDAOImplMultithreadingTest1 = new
	// AdminDAOImplMultithreadingTest();
	// AdminDAOImplMultithreadingTest adminDAOImplMultithreadingTest2 = new
	// AdminDAOImplMultithreadingTest();
	// AdminDAO adminDao = new AdminDAOImpl();
	// Admin admin1 = adminDao.getAdmin("dinuka");
	// admin1 = adminDao.loadAllPropertiesOfAdmin(admin1.getId());
	// admin1.setEmail("thread1@gmail.com");
	// adminDAOImplMultithreadingTest1.setAdmin(admin1);
	// Thread one = new Thread(adminDAOImplMultithreadingTest1);
	//
	// // Admin admin2 = adminDao.getAdmin("dinuka");
	// // admin2 = adminDao.loadAllPropertiesOfAdmin(admin2.getId());
	// admin1.setEmail("thread2@gmail.com");
	// adminDAOImplMultithreadingTest2.setAdmin(admin1);
	// Thread two = new Thread(adminDAOImplMultithreadingTest2);
	// for (int i = 0; i < 10; i++) {
	// one.start();
	// two.start();
	// }
	AdminDAO adminDao = new AdminDAOImpl();
	ExecutorService executor = Executors.newFixedThreadPool(10);
	Admin admin1 = adminDao.getAdmin("dinuka");

	admin1 = adminDao.loadAllPropertiesOfAdmin(admin1.getId());
	for (int i = 0; i < 10; i++) {

	    admin1.setEmail("thread" + i + "@gmail.com");
	    Runnable worker = new AdminDAOImplMultithreadingTest(admin1);
	    log.info("Start Editing thread " + i);
	    executor.execute(worker);
	}
	executor.shutdown();

	AdminDAO adminDao1 = new AdminDAOImpl();
	ExecutorService executor1 = Executors.newFixedThreadPool(10);
	Admin admin2 = adminDao1.getAdmin("malinda");

	admin2 = adminDao1.loadAllPropertiesOfAdmin(admin2.getId());

	for (int i = 0; i < 10; i++) {

	    admin2.setEmail("thread" + i + "@gmail.com");
	    Runnable worker = new AdminDAOImplMultithreadingTest(admin2);
	    log.info("Start Editing thread " + i);
	    executor1.execute(worker);
	}
	executor1.shutdown();
	// Wait until all threads are finish
	
	Thread executeClearLocks = new Thread(new ClearLocks());
	executeClearLocks.start();
    }
}

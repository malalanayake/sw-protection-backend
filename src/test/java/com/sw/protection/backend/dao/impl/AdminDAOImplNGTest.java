/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;

/**
 * Declare AdminDAO tests
 * 
 * @author dinuka
 */
@Test(groups = { "AdminDAOImplNGTest" })
public class AdminDAOImplNGTest {
    public static final Logger log = Logger.getLogger(AdminDAOImplNGTest.class.getName());

    public AdminDAOImplNGTest() {
    }

    @DataProvider(name = "adminData")
    public Object[][] AdminData() {
	Object[][] IndiaIncomeArray = {
		{ "Dinuka", "malalanayake", "pw", "dinuka.malalanayake@gmail.com", APINames.USER, true, false, true,
			false },
		{ "Malinda", "malinda", "pw908", "malinda@yahoo.com", APINames.USER, false, true, false, true } };
	return (IndiaIncomeArray);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
	HibernateUtil.setHost(DBTestProperties.HOST);
	HibernateUtil.setPort(DBTestProperties.PORT);
	HibernateUtil.setUsername(DBTestProperties.USER);
	HibernateUtil.setPassword(DBTestProperties.PW);
	HibernateUtil.setDbname(DBTestProperties.DBNAME);
	HibernateUtil.init();
	SharedInMemoryData.getInstance();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getAllAdmins method, of class AdminDAOImpl.
     */
    @Test
    public void testGetAllAdmins() {
	// System.out.println("getAllAdmins");
	// AdminDAOImpl instance = new AdminDAOImpl();
	// List expResult = null;
	// List result = instance.getAllAdmins();
	// assertEquals(result, expResult);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
    }

    /**
     * Test of getAdmin method, of class AdminDAOImpl.
     */
    @Test(dependsOnMethods = { "testSaveAdmin" })
    public void testGetAdmin() {
	log.info("Start Test Get Admin");
	String userName = "malalanayake";
	AdminDAO instance = new AdminDAOImpl();
	Admin expResult = new Admin();
	expResult.setUser_name("malalanayake");
	expResult.setEmail("dinuka.malalanayake@gmail.com");

	Admin result = instance.getAdmin(userName);
	assertEquals(result.getUser_name(), expResult.getUser_name());
	assertEquals(result.getEmail(), expResult.getEmail());

	result = instance.loadAllPropertiesOfAdmin(result.getId());

	Set<AdminScope> adminScopeSet = result.getAdminScopeSet();
	AdminScope firstAdminScope = new AdminScope();
	for (AdminScope adminScope : adminScopeSet) {
	    firstAdminScope = adminScope;
	}
	assertEquals(firstAdminScope.getApi_name(), APINames.USER);

    }

    /**
     * Test of updateAdmin method, of class AdminDAOImpl.
     */
    @Test(dependsOnMethods = { "testGetAdmin" })
    public void testUpdateAdmin() {
	log.info("Start Test Update Admin");
	String userName = "malalanayake";
	AdminDAO instance = new AdminDAOImpl();
	Admin expResult = new Admin();
	Admin result = new Admin();
	expResult = instance.getAdmin(userName);
	expResult = instance.loadAllPropertiesOfAdmin(expResult.getId());
	expResult.setEmail("testmail@yahoo.com");
	expResult.setName("Kasuni");
	instance.updateAdmin(expResult);

	result = instance.getAdmin("malalanayake");

	assertEquals(result.getId(), expResult.getId());
	assertEquals(result.getEmail(), expResult.getEmail());
	assertEquals(result.getUser_name(), expResult.getUser_name());
	assertEquals(result.getPass_word(), expResult.getPass_word());
    }

    @Test(dependsOnMethods = { "testUpdateAdmin" })
    public void isAdminUserNameExist() {
	log.info("getAllAdmins");
	AdminDAOImpl instance = new AdminDAOImpl();
	assertEquals(instance.isAdminUserNameExist("malalanayake"), true);
	assertEquals(instance.isAdminUserNameExist("dinuka"), false);
    }

    /**
     * Test of deleteAdmin method, of class AdminDAOImpl.
     */
    @Test(dependsOnMethods = { "isAdminUserNameExist" })
    public void testDeleteAdmin() {
	log.info("Start Test Delete Admin");
	String userName1 = "malinda";
	String userName2 = "malalanayake";
	String userName3 = "TestAdminWithoutScope";
	AdminDAO instance = new AdminDAOImpl();
	Admin admin1 = new Admin();
	Admin admin2 = new Admin();
	Admin admin3 = new Admin();
	admin1 = instance.getAdmin(userName1);
	admin2 = instance.getAdmin(userName2);
	admin3 = instance.getAdmin(userName3);

	instance.deleteAdmin(admin1);
	assertEquals(instance.isAdminUserNameExist(userName1), false);
	instance.deleteAdmin(admin2);
	assertEquals(instance.isAdminUserNameExist(userName2), false);
	instance.deleteAdmin(admin3);
	assertEquals(instance.isAdminUserNameExist(userName3), false);
    }

    /**
     * Test of saveAdmin method, of class AdminDAOImpl.
     */
    @Test(dataProvider = "adminData")
    public void testSaveAdmin(String name, String userName, String pw, String email, String api_name, boolean get,
	    boolean post, boolean put, boolean del) {
	log.info("Start Test Save Admin");
	Admin admin = new Admin();
	admin.setUser_name(userName);
	admin.setPass_word(pw);
	admin.setEmail(email);
	admin.setName(name);
	admin.setApi_key(UUID.randomUUID().toString());

	AdminScope adminScope = new AdminScope();
	adminScope.setAdmin(admin);
	adminScope.setApi_name(api_name);
	adminScope.setDel(del);
	adminScope.setGet(get);
	adminScope.setPost(post);
	adminScope.setPut(put);

	Set<AdminScope> adminScopSet = admin.getAdminScopeSet();
	adminScopSet.add(adminScope);
	admin.setAdminScopeSet(adminScopSet);
	AdminDAO instance = new AdminDAOImpl();
	log.info("" + admin.toString());
	instance.saveAdmin(admin);

    }

    /**
     * Test Admin without saving the Scope set
     */
    @Test
    public void testSaveAdminWithoutScope() {
	log.info("Start Test Save Admin without scope");
	Admin admin = new Admin();
	admin.setUser_name("TestAdminWithoutScope");
	admin.setPass_word("Test");
	admin.setEmail("dinuka@123.com");
	admin.setName("Test Admin");
	admin.setApi_key(UUID.randomUUID().toString());

	AdminDAO instance = new AdminDAOImpl();
	log.info("" + admin.toString());
	instance.saveAdmin(admin);

    }
}

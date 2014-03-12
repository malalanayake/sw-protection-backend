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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
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
@Test(groups = { "AdminDAOImplTest" })
public class AdminDAOImplTest {
    public static final Logger log = Logger.getLogger(AdminDAOImplTest.class.getName());

    public AdminDAOImplTest() {
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
	log.info("Delete all Admins");
	AdminDAO instance = new AdminDAOImpl();
	for (Admin admin : instance.getAllAdmins()) {
	    instance.deleteAdmin(admin);
	}
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
	Admin failResult = new Admin();
	Admin result = new Admin();
	expResult = instance.getAdmin(userName);
	expResult = instance.loadAllPropertiesOfAdmin(expResult.getId());
	failResult = instance.getAdmin(userName);
	failResult = instance.loadAllPropertiesOfAdmin(failResult.getId());
	expResult.setEmail("testmail@yahoo.com");
	expResult.setName("Kasuni");
	try {
	    instance.updateAdmin(expResult);
	} catch (Exception ex) {

	}

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    instance.updateAdmin(failResult);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

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
	try {
	    instance.deleteAdmin(admin1);
	    assertEquals(instance.isAdminUserNameExist(userName1), false);
	    instance.deleteAdmin(admin2);
	    assertEquals(instance.isAdminUserNameExist(userName2), false);
	} catch (Exception ex) {

	}

	Admin admin4 = new Admin();
	admin4 = instance.getAdmin(userName3);
	admin4 = instance.loadAllPropertiesOfAdmin(admin4.getId());
	admin4.setEmail("lateModified@gmail.com");

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    // update late modified
	    instance.updateAdmin(admin4);
	    // going to delete through early loaded admin data
	    instance.deleteAdmin(admin3);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());
	assertEquals(instance.isAdminUserNameExist(userName3), true);

	// Again get load only the Admin data and delete
	Admin admin5 = new Admin();
	admin5 = instance.getAdmin(userName3);
	try {
	    instance.deleteAdmin(admin5);
	} catch (Exception ex) {

	}
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
	try {
	    instance.saveAdmin(admin);
	} catch (Exception ex) {

	}

	// Check the DuplicateRecordException behavior
	String exceptionClass = "";
	try {
	    instance.saveAdmin(admin);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DuplicateRecordException.class.toString());

	// Check the DuplicateRecordException behavior
	String exceptionClass2 = "";
	try {
	    admin.setUser_name(UUID.randomUUID().toString());
	    instance.saveAdmin(admin);
	} catch (Exception ex) {
	    exceptionClass2 = ex.getClass().toString();
	}
	assertEquals(exceptionClass2, DuplicateRecordException.class.toString());

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
	try {
	    instance.saveAdmin(admin);
	} catch (Exception ex) {

	}

    }

    @Test(dependsOnMethods = { "testDeleteAdmin" })
    public void testPagination() {
	log.info("Start Test Pagination");
	Admin admin = new Admin();
	admin.setPass_word("Test");
	admin.setName("Test Admin");
	admin.setApi_key(UUID.randomUUID().toString());

	AdminDAO instance = new AdminDAOImpl();
	log.info("" + admin.toString());
	try {
	    for (int i = 0; i < 23; i++) {
		admin.setUser_name("TestUser" + i);
		admin.setEmail("dinuka" + i + "@123.com");
		instance.saveAdmin(admin);
	    }
	} catch (Exception ex) {

	}

	assertEquals(instance.getAllAdminsWithPagination(1, 20).size(), 20);
	assertEquals(instance.getAllAdminsWithPagination(2, 20).size(), 3);
	assertEquals(instance.getAllAdminsWithPagination(3, 20), null);

    }

}

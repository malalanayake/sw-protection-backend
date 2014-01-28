/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sw.protection.backend.dao.impl;

import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.DBTestProperties;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Declare AdminDAO tests
 * 
 * @author dinuka
 */
public class AdminDAOImplNGTest {

    public AdminDAOImplNGTest() {
    }

    @DataProvider(name = "adminData")
    public Object[][] AdminData() {
	Object[][] IndiaIncomeArray = {
		{ "Dinuka", "malalanayake", "pw",
			"dinuka.malalanayake@gmail.com", APINames.USER, true,
			false, true, false },
		{ "Malinda", "malinda", "pw908", "malinda@yahoo.com",
			APINames.USER, false, true, false, true } };
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
	System.out.println("Start Test Get Admin");
	String userName = "malalanayake";
	AdminDAO instance = new AdminDAOImpl();
	Admin expResult = new Admin();
	expResult.setUser_name("malalanayake");
	expResult.setEmail("dinuka.malalanayake@gmail.com");
	
	Admin result = instance.getAdmin(userName);	
	assertEquals(result.getUser_name(), expResult.getUser_name());
	assertEquals(result.getEmail(), expResult.getEmail());
	//Set<AdminScope> adminScopeSet =result.getAdminScopeSet();
	//System.out.println(result.getAdminScopeSet().size());
	//assertEquals(adminScopeSet,null);
	
	
	result = instance.loadAllPropertiesOfAdmin(result.getId());
	
	System.out.println(result.getAdminScopeSet().size());
	Set<AdminScope> adminScopeSet =result.getAdminScopeSet();
	AdminScope firstAdminScope = new AdminScope();
	for(AdminScope adminScope:adminScopeSet){
	    firstAdminScope = adminScope;
	}
	assertEquals(firstAdminScope.getApi_name(),APINames.USER);
	
	
    }

    /**
     * Test of updateAdmin method, of class AdminDAOImpl.
     */
    @Test(dependsOnMethods = { "testGetAdmin" })
    public void testUpdateAdmin() {
	System.out.println("Start Test Update Admin");
	// Admin admin = null;
	// AdminDAOImpl instance = new AdminDAOImpl();
	// instance.updateAdmin(admin);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
    }
    
    @Test(dependsOnMethods = { "testUpdateAdmin" })
    public void isAdminUserNameExist() {
	 System.out.println("getAllAdmins");
	 AdminDAOImpl instance = new AdminDAOImpl();
	 assertEquals(instance.isAdminUserNameExist("malalanayake"),true);
	 assertEquals(instance.isAdminUserNameExist("dinuka"),false);
    }

    /**
     * Test of deleteAdmin method, of class AdminDAOImpl.
     */
    @Test(dependsOnMethods = { "isAdminUserNameExist" })
    public void testDeleteAdmin() {
	System.out.println("Start Test Delete Admin");
	// Admin admin = null;
	// AdminDAOImpl instance = new AdminDAOImpl();
	// instance.deleteAdmin(admin);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
    }

    /**
     * Test of saveAdmin method, of class AdminDAOImpl.
     */
    @Test(dataProvider = "adminData")
    public void testSaveAdmin(String name, String userName, String pw,
	    String email, String api_name, boolean get, boolean post,
	    boolean put, boolean del) {
	System.out.println("Start Test Save Admin");
	Admin admin = new Admin();
	admin.setUser_name(userName);
	admin.setPass_word(pw);
	admin.setEmail(email);
	admin.setName(name);
	admin.setApi_key(UUID.randomUUID().toString());
	admin.setDate_time(Formatters.formatDate(new Date()));

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
	System.out.print("" + admin.toString());
	instance.saveAdmin(admin);
    }

}

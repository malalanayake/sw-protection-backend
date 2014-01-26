/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.dao.impl;

import com.sw.protection.backend.config.DBTestProperties;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author dinuka
 */
public class AdminDAOImplNGTest {
    
    public AdminDAOImplNGTest() {
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
//        System.out.println("getAllAdmins");
//        AdminDAOImpl instance = new AdminDAOImpl();
//        List expResult = null;
//        List result = instance.getAllAdmins();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdmin method, of class AdminDAOImpl.
     */
    @Test
    public void testGetAdmin() {
//        System.out.println("getAdmin");
//        String userName = "";
//        AdminDAOImpl instance = new AdminDAOImpl();
//        Admin expResult = null;
//        Admin result = instance.getAdmin(userName);
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updateAdmin method, of class AdminDAOImpl.
     */
    @Test
    public void testUpdateAdmin() {
//        System.out.println("updateAdmin");
//        Admin admin = null;
//        AdminDAOImpl instance = new AdminDAOImpl();
//        instance.updateAdmin(admin);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteAdmin method, of class AdminDAOImpl.
     */
    @Test
    public void testDeleteAdmin() {
//        System.out.println("deleteAdmin");
//        Admin admin = null;
//        AdminDAOImpl instance = new AdminDAOImpl();
//        instance.deleteAdmin(admin);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of saveAdmin method, of class AdminDAOImpl.
     */
    @Test
    public void testSaveAdmin() {
        System.out.println("Test Save Admin");
        Admin admin = new Admin();
        admin.setUsername("Test-User");
        admin.setPassword("Test-Pw");
        AdminDAO instance = new AdminDAOImpl();
        instance.saveAdmin(admin);
    }
    
}

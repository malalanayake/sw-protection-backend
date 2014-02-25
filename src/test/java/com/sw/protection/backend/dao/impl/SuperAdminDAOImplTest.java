package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.dao.SuperAdminDAO;
import com.sw.protection.backend.entity.SuperAdmin;

@Test(groups = { "SuperAdminDAOImplTest" }, dependsOnGroups = { "AdminScopeDAOImplTest" })
public class SuperAdminDAOImplTest {
    public static final Logger log = Logger.getLogger(SuperAdminDAOImplTest.class.getName());

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

    @Test
    public void saveSuperAdmin() {
	log.info("Start test save Super admin");
	SuperAdminDAO superAdminDao = new SuperAdminDAOImpl();
	SuperAdmin superAdmin1 = new SuperAdmin();
	superAdmin1.setName("super_dinuka");
	superAdmin1.setUser_name("dinuka");
	superAdmin1.setPass_word("test1");
	superAdmin1.setEmail("dinuka@gmail.com");
	superAdmin1.setApi_key(UUID.randomUUID().toString());
	superAdminDao.saveSuperAdmin(superAdmin1);

	SuperAdmin superAdmin2 = new SuperAdmin();
	superAdmin2.setName("super_malinda");
	superAdmin2.setUser_name("malinda");
	superAdmin2.setPass_word("test2");
	superAdmin2.setEmail("malinda@gmail.com");
	superAdmin2.setApi_key(UUID.randomUUID().toString());
	superAdminDao.saveSuperAdmin(superAdmin2);

    }

    @Test(dependsOnMethods = { "saveSuperAdmin" })
    public void getAllAdmins() {
	log.info("Start test get all Super admin users");
	SuperAdminDAO superAdminDao = new SuperAdminDAOImpl();
	List<SuperAdmin> superAdminList = superAdminDao.getAllAdmins();
	assertEquals(superAdminList.size(), 2);
    }

    @Test(dependsOnMethods = { "getAllAdmins" })
    public void updateSuperAdmin() {
	log.info("Start test update Super admin users");
	SuperAdminDAO superAdminDao = new SuperAdminDAOImpl();
	SuperAdmin superAdmin1 = new SuperAdmin();
	superAdmin1 = superAdminDao.getSuperAdmin("dinuka");

	assertEquals(superAdmin1.getEmail(), "dinuka@gmail.com");
	assertEquals(superAdmin1.getUser_name(), "dinuka");
	assertEquals(superAdmin1.getPass_word(), "test1");
	assertEquals(superAdmin1.getName(), "super_dinuka");

	superAdmin1.setEmail("dinukanew@gmail.com");
	superAdmin1.setPass_word("testnew1");
	superAdmin1.setName("Dinuka Malalanayake");

	superAdminDao.updateSuperAdmin(superAdmin1);

	SuperAdmin superAdmin2 = new SuperAdmin();
	superAdmin2 = superAdminDao.getSuperAdmin("dinuka");

	assertEquals(superAdmin2.getEmail(), "dinukanew@gmail.com");
	assertEquals(superAdmin2.getUser_name(), "dinuka");
	assertEquals(superAdmin2.getPass_word(), "testnew1");
	assertEquals(superAdmin2.getName(), "Dinuka Malalanayake");
    }

    @Test(dependsOnMethods = { "updateSuperAdmin" })
    public void deleteSuperAdmin() {
	log.info("Start test delete Super admin users");
	SuperAdminDAO superAdminDao = new SuperAdminDAOImpl();
	SuperAdmin superAdmin1 = new SuperAdmin();
	superAdmin1 = superAdminDao.getSuperAdmin("dinuka");

	assertEquals(superAdminDao.isSuperAdminUserNameExist("dinuka"), true);
	superAdminDao.deleteSuperAdmin(superAdmin1);
	assertEquals(superAdminDao.isSuperAdminUserNameExist("dinuka"), false);

	superAdmin1 = superAdminDao.getSuperAdmin("malinda");

	assertEquals(superAdminDao.isSuperAdminUserNameExist("malinda"), true);
	superAdminDao.deleteSuperAdmin(superAdmin1);
	assertEquals(superAdminDao.isSuperAdminUserNameExist("malinda"), false);
    }

}

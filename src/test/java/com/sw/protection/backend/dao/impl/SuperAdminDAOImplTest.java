package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.SuperAdminDAO;
import com.sw.protection.backend.entity.SuperAdmin;

@Test(groups = { "SuperAdminDAOImplTest" }, dependsOnGroups = { "AdminDAOImplMultiThreadingTest" })
public class SuperAdminDAOImplTest {
	public static final Logger log = Logger.getLogger(SuperAdminDAOImplTest.class.getName());

	@BeforeClass()
	public void setupParamValidation() {
		SharedInMemoryData.getInstance();
	}

	@Test
	public void saveSuperAdmin() {
		log.info("Start test save Super admin");
		SuperAdminDAO superAdminDao = AppContext.getInstance().getBean(SuperAdminDAO.class);
		SuperAdmin superAdmin1 = new SuperAdmin();
		superAdmin1.setName("super_dinuka");
		superAdmin1.setUser_name("dinuka");
		superAdmin1.setPass_word("test1");
		superAdmin1.setEmail("dinuka@gmail.com");
		superAdmin1.setApi_key(UUID.randomUUID().toString());
		try {
			superAdminDao.saveSuperAdmin(superAdmin1);
		} catch (Exception ex) {

		}

		// Check DuplicateRecordException
		String checkException = "";
		try {
			superAdminDao.saveSuperAdmin(superAdmin1);
		} catch (Exception ex) {
			checkException = ex.getClass().toString();
		}
		assertEquals(checkException, DuplicateRecordException.class.toString());

		SuperAdmin superAdmin2 = new SuperAdmin();
		superAdmin2.setName("super_malinda");
		superAdmin2.setUser_name("malinda");
		superAdmin2.setPass_word("test2");
		superAdmin2.setEmail("malinda@gmail.com");
		superAdmin2.setApi_key(UUID.randomUUID().toString());
		try {
			superAdminDao.saveSuperAdmin(superAdmin2);
		} catch (Exception ex) {

		}

	}

	@Test(dependsOnMethods = { "saveSuperAdmin" })
	public void getAllAdmins() {
		log.info("Start test get all Super admin users");
		SuperAdminDAO superAdminDao = AppContext.getInstance().getBean(SuperAdminDAO.class);
		List<SuperAdmin> superAdminList = superAdminDao.getAllAdmins();
		assertEquals(superAdminList.size(), 2);
	}

	@Test(dependsOnMethods = { "getAllAdmins" })
	public void updateSuperAdmin() {
		log.info("Start test update Super admin users");
		SuperAdmin superAdmin1 = new SuperAdmin();
		SuperAdmin superAdminAlredayModified = new SuperAdmin();
		SuperAdminDAO superAdminDao = AppContext.getInstance().getBean(SuperAdminDAO.class);
		superAdmin1 = superAdminDao.getSuperAdmin("dinuka");
		superAdminAlredayModified = superAdminDao.getSuperAdmin("dinuka");

		assertEquals(superAdmin1.getEmail(), "dinuka@gmail.com");
		assertEquals(superAdmin1.getUser_name(), "dinuka");
		assertEquals(superAdmin1.getPass_word(), "test1");
		assertEquals(superAdmin1.getName(), "super_dinuka");

		superAdmin1.setEmail("dinukanew@gmail.com");
		superAdmin1.setPass_word("testnew1");
		superAdmin1.setName("Dinuka Malalanayake");
		try {
			superAdminDao.updateSuperAdmin(superAdmin1);
		} catch (Exception ex) {

		}

		// Check RecordAlreadyModifiedException
		String checkException = "";
		try {
			superAdminDao.updateSuperAdmin(superAdminAlredayModified);
		} catch (Exception ex) {
			checkException = ex.getClass().toString();
		}
		assertEquals(checkException, RecordAlreadyModifiedException.class.toString());

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
		SuperAdmin superAdmin1 = new SuperAdmin();
		SuperAdmin superAdminAlredayModified = new SuperAdmin();
		SuperAdminDAO superAdminDao = AppContext.getInstance().getBean(SuperAdminDAO.class);
		superAdmin1 = superAdminDao.getSuperAdmin("dinuka");
		superAdminAlredayModified = superAdminDao.getSuperAdmin("dinuka");
		superAdminAlredayModified.setEmail("updateemail@gmail.com");

		assertEquals(superAdminDao.isSuperAdminUserNameExist("dinuka"), true);

		// Check RecordAlreadyModifiedException
		String checkException = "";
		try {
			superAdminDao.updateSuperAdmin(superAdminAlredayModified);
			superAdminDao.deleteSuperAdmin(superAdmin1);
		} catch (Exception ex) {
			checkException = ex.getClass().toString();
		}
		assertEquals(checkException, RecordAlreadyModifiedException.class.toString());

		superAdmin1 = superAdminDao.getSuperAdmin("dinuka");
		try {
			superAdminDao.deleteSuperAdmin(superAdmin1);
		} catch (Exception ex) {

		}
		assertEquals(superAdminDao.isSuperAdminUserNameExist("dinuka"), false);

		superAdmin1 = superAdminDao.getSuperAdmin("malinda");

		assertEquals(superAdminDao.isSuperAdminUserNameExist("malinda"), true);
		try {
			superAdminDao.deleteSuperAdmin(superAdmin1);
		} catch (Exception ex) {

		}
		assertEquals(superAdminDao.isSuperAdminUserNameExist("malinda"), false);
	}

}

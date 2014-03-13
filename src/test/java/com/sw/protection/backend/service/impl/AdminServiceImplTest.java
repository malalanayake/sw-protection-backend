package com.sw.protection.backend.service.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.decoder.impl.JSONDecoder;
import com.sw.protection.backend.encoder.impl.JSONEncoder;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.service.AdminService;

@Test(groups = { "AdminServiceImplTest" }, dependsOnGroups = { "DecoderFactoryImplTest" })
public class AdminServiceImplTest {
    public static final Logger log = Logger.getLogger(AdminServiceImplTest.class.getName());

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
    public static void tearDown() throws Exception {
	AdminService adminService = AdminServiceImpl.getInstance();
	String listofAdmins = adminService.getAllAdmins(EncoderDecoderType.JSON, 1, 20);
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	List<Admin> list = (List<Admin>) decoder.decodeObjectList(ObjectType.ADMIN, listofAdmins);
	String encodedAdmin = "";
	for (Admin admin : list) {
	    encodedAdmin = encoder.encodeObject(ObjectType.ADMIN, admin);
	    adminService.deleteAdmin(EncoderDecoderType.JSON, encodedAdmin);
	}
    }

    @Test
    public void saveAdmin() {
	AdminService adminService = AdminServiceImpl.getInstance();
	String adminEncoded = "{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\"}";
	String adminReturn = "";
	try {
	    adminReturn = adminService.saveAdmin(EncoderDecoderType.JSON, adminEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(adminReturn.contains("id"), true);

	String duplicateExcep = "";
	try {
	    adminReturn = adminService.saveAdmin(EncoderDecoderType.JSON, adminEncoded);
	} catch (Exception e) {
	    duplicateExcep = e.getClass().toString();
	}
	assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

	String decodingExcep = "";
	try {
	    adminReturn = adminService.saveAdmin(EncoderDecoderType.JSON, adminEncoded + "}{");
	} catch (Exception e) {
	    decodingExcep = e.getClass().toString();
	}
	assertEquals(decodingExcep, DecodingException.class.toString());

	String adminEncodedNew = "{\"name\":\"super_dinuka\",\"user_name\":\"malinda\",\"email\":\"dinuka2@gmail.com\"}";
	String operationRollBackExcep = "";
	try {
	    adminReturn = adminService.saveAdmin(EncoderDecoderType.JSON, adminEncodedNew);
	} catch (Exception e) {
	    operationRollBackExcep = e.getClass().toString();
	}
	assertEquals(operationRollBackExcep, OperationRollBackException.class.toString());

	String adminEncoded2 = "{\"name\":\"super_dinuka\",\"user_name\":\"malinda\",\"pass_word\":\"test1\",\"email\":\"dinuka2@gmail.com\"}";
	String adminReturn2 = "";
	try {
	    adminReturn2 = adminService.saveAdmin(EncoderDecoderType.JSON, adminEncoded2);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(adminReturn2.contains("id"), true);
    }

    @Test(dependsOnMethods = { "saveAdmin" })
    public void getAdmin() {
	AdminService adminService = AdminServiceImpl.getInstance();
	String adminEncoded = "{\"user_name\":\"dinuka\"}";
	String adminReturn = "";
	try {
	    adminReturn = adminService.getAdmin(EncoderDecoderType.JSON, adminEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(adminReturn.contains("api_key"), true);
    }

    @Test(dependsOnMethods = { "getAdmin" })
    public void getAllAdmins() {
	AdminService adminService = AdminServiceImpl.getInstance();
	String adminReturn = "";
	JSONDecoder decoder = new JSONDecoder();
	List<Admin> admins = null;
	try {
	    adminReturn = adminService.getAllAdmins(EncoderDecoderType.JSON, 1, 20);
	    admins = (List<Admin>) decoder.decodeObjectList(ObjectType.ADMIN, adminReturn);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(admins.size(), 2);

	try {
	    adminReturn = adminService.getAllAdmins(EncoderDecoderType.JSON, 2, 1);
	    admins = (List<Admin>) decoder.decodeObjectList(ObjectType.ADMIN, adminReturn);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(admins.size(), 1);

    }

    @Test(dependsOnMethods = { "getAllAdmins" })
    public void updateAdmin() {
	AdminService adminService = AdminServiceImpl.getInstance();
	String adminEncoded = "{\"user_name\":\"dinuka\"}";
	String adminReturn = "";
	try {
	    adminReturn = adminService.getAdmin(EncoderDecoderType.JSON, adminEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(adminReturn.contains("api_key"), true);
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	Admin admin = null;
	Admin admin2 = null;
	try {
	    admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminReturn);
	    admin2 = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminReturn);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	String encodedString = "";
	String finalString = "";
	// check by changing the user name
	admin.setUser_name("malinda");
	String requiredDataExp = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    finalString = adminService.updateAdmin(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    requiredDataExp = e.getClass().toString();
	}
	assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

	// check by changing the user name
	admin.setUser_name("dinuka");
	admin.setEmail("dinuka2@gmail.com");
	String requiredDataExpEmail = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    finalString = adminService.updateAdmin(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    requiredDataExpEmail = e.getClass().toString();
	}
	assertEquals(requiredDataExpEmail, OperationRollBackException.class.toString());

	admin.setName("New_Name");
	admin.setEmail("test@malalanayake.com");
	admin.setUser_name("dinuka");
	encodedString = "";
	finalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    finalString = adminService.updateAdmin(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {

	}
	System.out.print(finalString);
	assertEquals(finalString.contains("New_Name"), true);
	assertEquals(finalString.contains("test@malalanayake.com"), true);

	admin2.setName("New_Name");
	admin2.setEmail("test@malalanayake.com");
	admin2.setUser_name("dinuka");
	encodedString = "";
	finalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin2);
	    adminService.updateAdmin(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    finalString = e.getClass().toString();
	}

	assertEquals(finalString, RecordAlreadyModifiedException.class.toString());
    }

    @Test(dependsOnMethods = { "updateAdmin" })
    public void deleteAdmin() {
	AdminService adminService = AdminServiceImpl.getInstance();
	String adminEncoded = "{\"user_name\":\"dinuka\"}";
	String adminReturn = "";
	try {
	    adminReturn = adminService.getAdmin(EncoderDecoderType.JSON, adminEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(adminReturn.contains("api_key"), true);
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	Admin admin = null;
	Admin admin2 = null;
	try {
	    admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminReturn);
	    admin2 = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminReturn);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	String encodedString = "";
	String finalString = "";
	// check by changing the user name
	admin.setUser_name("malinda");
	String requiredDataExp = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    finalString = adminService.deleteAdmin(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    requiredDataExp = e.getClass().toString();
	}
	assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

	admin.setName("New_Name");
	admin.setEmail("test@malalanayake.com");
	admin.setUser_name("dinuka");
	encodedString = "";
	String updatedfinalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    updatedfinalString = adminService.updateAdmin(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {

	}
	System.out.print(updatedfinalString);
	assertEquals(updatedfinalString.contains("New_Name"), true);
	assertEquals(updatedfinalString.contains("test@malalanayake.com"), true);

	encodedString = "";
	finalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin2);
	    adminService.deleteAdmin(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    finalString = e.getClass().toString();
	}

	assertEquals(finalString, RecordAlreadyModifiedException.class.toString());

	encodedString = "";
	finalString = "";
	try {
	    adminService.deleteAdmin(EncoderDecoderType.JSON, updatedfinalString);
	} catch (Exception e) {
	    finalString = e.getClass().toString();
	}

	adminReturn = "";
	try {
	    adminReturn = adminService.getAdmin(EncoderDecoderType.JSON, adminEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(adminReturn, "");
    }

}

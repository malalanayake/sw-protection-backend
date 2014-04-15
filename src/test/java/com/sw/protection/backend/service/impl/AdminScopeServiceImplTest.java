package com.sw.protection.backend.service.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.decoder.impl.JSONDecoder;
import com.sw.protection.backend.encoder.impl.JSONEncoder;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.service.AdminScopeService;
import com.sw.protection.backend.service.AdminService;

@Test(groups = { "AdminScopeServiceImplTest" }, dependsOnGroups = { "AdminServiceImplTest" })
public class AdminScopeServiceImplTest {
    public static final Logger log = Logger.getLogger(AdminScopeServiceImplTest.class.getName());
    private static String adminReturn = "";

    @BeforeClass
    public static void setUpClass() throws Exception {
	SharedInMemoryData.getInstance();
	AdminService adminService = AppContext.getInstance().getBean(AdminService.class);
	String adminEncoded = "{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\"}";

	try {
	    adminReturn = adminService.saveAdmin(EncoderDecoderType.JSON, adminEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @AfterClass
    public void tearDown() throws Exception {
	AdminService adminService = AppContext.getInstance().getBean(AdminService.class);
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
    public void saveAdminScope() {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);
	String adminScopeEncoded = "{\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"admin\":" + adminReturn + "}";
	String adminScopeReturn = "";
	try {
	    adminScopeReturn = adminScopeService.saveAdminScope(EncoderDecoderType.JSON, adminScopeEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(adminScopeReturn.contains("id"), true);
    }

    @Test(dependsOnMethods = { "saveAdminScope" })
    public void getAdminScopes() {

    }

    @Test(dependsOnMethods = { "getAdminScopes" })
    public void updateAdminScope() {

    }

    @Test(dependsOnMethods = { "updateAdminScope" })
    public void deleteAdminScope() {

    }
}

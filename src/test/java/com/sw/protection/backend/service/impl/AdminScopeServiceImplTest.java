package com.sw.protection.backend.service.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.decoder.impl.JSONDecoder;
import com.sw.protection.backend.encoder.impl.JSONEncoder;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;
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

	String duplicateExcep = "";
	try {
	    adminScopeReturn = adminScopeService.saveAdminScope(EncoderDecoderType.JSON, adminScopeEncoded);
	} catch (Exception e) {
	    duplicateExcep = e.getClass().toString();
	}
	assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

	String decodingExcep = "";
	try {
	    adminScopeReturn = adminScopeService.saveAdminScope(EncoderDecoderType.JSON, adminScopeEncoded + "}{");
	} catch (Exception e) {
	    decodingExcep = e.getClass().toString();
	}
	assertEquals(decodingExcep, DecodingException.class.toString());

	String adminScopeEncodedTwo = "{\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,\"admin\":{}}";
	String requiredDataNotfoundExcep = "";
	try {
	    adminScopeReturn = adminScopeService.saveAdminScope(EncoderDecoderType.JSON, adminScopeEncodedTwo);
	} catch (Exception e) {
	    requiredDataNotfoundExcep = e.getClass().toString();
	}
	assertEquals(requiredDataNotfoundExcep, RequiredDataNotFoundException.class.toString());

	adminScopeEncoded = "{\"api_name\":\"user\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"admin\":" + adminReturn + "}";
	adminScopeReturn = "";
	try {
	    adminScopeReturn = adminScopeService.saveAdminScope(EncoderDecoderType.JSON, adminScopeEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(adminScopeReturn.contains("id"), true);
    }

    @Test(dependsOnMethods = { "saveAdminScope" })
    public void getAdminScopes() {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);
	String adminScopeReturn = "";
	try {
	    adminScopeReturn = adminScopeService.getAdminScopes(EncoderDecoderType.JSON, "dinuka");
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(adminScopeReturn.contains("\"api_name\":\"admin\""), true);
	assertEquals(adminScopeReturn.contains("\"api_name\":\"user\""), true);

	String requiredDataNotfoundExcep = "";
	try {
	    adminScopeReturn = adminScopeService.getAdminScopes(EncoderDecoderType.JSON, "");
	} catch (Exception e) {
	    requiredDataNotfoundExcep = e.getClass().toString();
	}
	assertEquals(requiredDataNotfoundExcep, RequiredDataNotFoundException.class.toString());
    }

    @Test(dependsOnMethods = { "getAdminScopes" })
    public void updateAdminScope() {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);
	String adminScopeReturn = "";
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	try {
	    String adminScopeEncoded = adminScopeService.getAdminScope(EncoderDecoderType.JSON, "dinuka", "admin");
	    assertEquals(adminScopeEncoded.contains("\"get\":true"), true);
	    assertEquals(adminScopeEncoded.contains("\"post\":true"), true);
	    assertEquals(adminScopeEncoded.contains("\"put\":true"), true);
	    assertEquals(adminScopeEncoded.contains("\"del\":true"), true);

	    AdminScope decodedAdminScope = (AdminScope) decoder.decodeObject(ObjectType.ADMIN_SCOPE, adminScopeEncoded);
	    decodedAdminScope.setDel(false);
	    decodedAdminScope.setPost(false);
	    decodedAdminScope.setPut(false);
	    decodedAdminScope.setGet(false);
	    String adminScopeFinalEncoded = encoder.encodeObject(ObjectType.ADMIN_SCOPE, decodedAdminScope);
	    adminScopeReturn = adminScopeService.updateAdminScope(EncoderDecoderType.JSON, adminScopeFinalEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(adminScopeReturn.contains("id"), true);
	assertEquals(adminScopeReturn.contains("\"get\":true"), false);
	assertEquals(adminScopeReturn.contains("\"post\":true"), false);
	assertEquals(adminScopeReturn.contains("\"put\":true"), false);
	assertEquals(adminScopeReturn.contains("\"del\":true"), false);
    }

    @Test(dependsOnMethods = { "updateAdminScope" })
    public void deleteAdminScope() {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);
	String adminScopeReturn = "";
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	try {
	    String adminScopeEncoded = adminScopeService.getAdminScope(EncoderDecoderType.JSON, "dinuka", "admin");
	    assertEquals(adminScopeEncoded.contains("id"), true);
	    assertEquals(adminScopeEncoded.contains("\"get\":true"), false);
	    assertEquals(adminScopeEncoded.contains("\"post\":true"), false);
	    assertEquals(adminScopeEncoded.contains("\"put\":true"), false);
	    assertEquals(adminScopeEncoded.contains("\"del\":true"), false);

	    AdminScope decodedAdminScope = (AdminScope) decoder.decodeObject(ObjectType.ADMIN_SCOPE, adminScopeEncoded);

	    String adminScopeFinalEncoded = encoder.encodeObject(ObjectType.ADMIN_SCOPE, decodedAdminScope);
	    adminScopeReturn = adminScopeService.deleteAdminScope(EncoderDecoderType.JSON, adminScopeFinalEncoded);
	    adminScopeEncoded = adminScopeService.getAdminScope(EncoderDecoderType.JSON, "dinuka", "admin");
	    assertEquals(adminScopeEncoded, "");
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    @Test(dependsOnMethods = { "deleteAdminScope" })
    public void isAccessGrantedFor() {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);
	String adminScopeReturn = "";
	try {
	    adminScopeReturn = adminScopeService.isAccessGrantedFor("dinuka", "user", APIOperations.GET.toString());

	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(adminScopeReturn, "true");

	try {
	    adminScopeReturn = adminScopeService.isAccessGrantedFor("dinuka", "admin", APIOperations.GET.toString());

	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(adminScopeReturn, "false");
    }
}

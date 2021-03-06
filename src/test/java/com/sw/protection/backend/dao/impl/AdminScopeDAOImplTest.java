package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.dao.AdminScopeDAO;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;

@Test(groups = { "AdminScopeDAOImplTest" }, dependsOnGroups = { "AdminDAOImplTest" })
public class AdminScopeDAOImplTest {
    public static final Logger log = Logger.getLogger(AdminScopeDAOImplTest.class.getName());

    @BeforeClass()
    public void setupParamValidation() {
	// Test class setup code with autowired classes goes here
    }

    @BeforeClass
    public void setUpClass() throws Exception {
	log.info("Setup the initial data");
	Admin admin = new Admin();
	admin.setUser_name("dinuka");
	admin.setPass_word("pw");
	admin.setEmail("dinuka@234.com");
	admin.setName("Dinuka");
	admin.setApi_key(UUID.randomUUID().toString());

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
	AdminDAO instance = AppContext.getInstance().getBean(AdminDAO.class);
	instance.saveAdmin(admin);

	Admin admin2 = new Admin();
	admin2.setUser_name("malinda");
	admin2.setPass_word("pw");
	admin2.setEmail("malinda@234.com");
	admin2.setName("Malinda");
	admin2.setApi_key(UUID.randomUUID().toString());

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
	AdminDAO instance2 = AppContext.getInstance().getBean(AdminDAO.class);
	instance2.saveAdmin(admin2);
    }

    /**
     * Test get all admin scopes according to specific admin user
     */
    @Test()
    public void getAllAdminScopes() {
	log.info("Test Start get all admin scopes");
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	List<AdminScope> list = adminScopeDAO.getAllAdminScopes("dinuka");
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
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	Admin admin = adminDAO.getAdmin("dinuka");

	AdminScope adminScope = new AdminScope();
	adminScope.setAdmin(admin);
	adminScope.setApi_name(APINames.COMPANY_USER);
	adminScope.setGet(false);
	adminScope.setPost(false);
	adminScope.setPut(false);
	adminScope.setDel(false);
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	try {
	    adminScopeDAO.saveNewAdminScope(adminScope);
	} catch (Exception ex) {

	}

	// Check DuplicateRecordException
	String checkException = "";
	try {
	    adminScopeDAO.saveNewAdminScope(adminScope);
	} catch (Exception ex) {
	    checkException = ex.getClass().toString();
	}
	assertEquals(checkException, DuplicateRecordException.class.toString());

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
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	Admin admin = adminDAO.getAdmin("dinuka");

	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	AdminScope adminScope = adminScopeDAO.getAdminScope("dinuka", APINames.COMPANY_USER);
	AdminScope adminAlredayModifiedScope = adminScopeDAO.getAdminScope("dinuka", APINames.COMPANY_USER);
	log.info("Admin Scope :" + adminScope.toString());
	assertEquals(adminScope.isDel(), false);
	assertEquals(adminScope.isGet(), false);
	assertEquals(adminScope.isPut(), false);
	assertEquals(adminScope.isPost(), false);

	adminScope.setDel(true);
	adminScope.setPut(true);
	adminScope.setPost(true);
	adminScope.setGet(true);
	try {
	    adminScopeDAO.updateAdminScope(adminScope);
	} catch (Exception ex) {

	}

	// Check RecordAlreadyModifiedException
	String checkException = "";
	try {
	    adminScopeDAO.updateAdminScope(adminAlredayModifiedScope);
	} catch (Exception ex) {
	    checkException = ex.getClass().toString();
	}
	assertEquals(checkException, RecordAlreadyModifiedException.class.toString());

	AdminScope latest = adminScopeDAO.getAdminScope("dinuka", APINames.COMPANY_USER);
	log.info("Uptodate admin Scope :" + latest.toString());
	assertEquals(latest.isDel(), true);
	assertEquals(latest.isGet(), true);
	assertEquals(latest.isPut(), true);
	assertEquals(latest.isPost(), true);

    }

    @Test(dependsOnMethods = { "updateAdminScope" })
    public void isAccessGrantedFor() {
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY_USER, APIOperations.GET), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY_USER, APIOperations.DELETE), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY_USER, APIOperations.POST), true);
	assertEquals(adminScopeDAO.isAccessGrantedFor("dinuka", APINames.COMPANY_USER, APIOperations.PUT), true);

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
    public void testDeleteAdminScope() {
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	AdminScope adminScope = adminScopeDAO.getAdminScope("dinuka", APINames.ADMIN);
	AdminScope adminAlredayModifiedScope = adminScopeDAO.getAdminScope("dinuka", APINames.ADMIN);
	adminAlredayModifiedScope.setDel(false);

	// Check RecordAlreadyModifiedException
	String checkException = "";
	try {
	    adminScopeDAO.updateAdminScope(adminAlredayModifiedScope);
	    adminScopeDAO.deleteAdminScope(adminScope);
	} catch (Exception ex) {
	    checkException = ex.getClass().toString();
	}
	assertEquals(checkException, RecordAlreadyModifiedException.class.toString());

	AdminScope adminTObeDeletedScope = adminScopeDAO.getAdminScope("dinuka", APINames.ADMIN);
	try {
	    adminScopeDAO.deleteAdminScope(adminTObeDeletedScope);
	} catch (Exception ex) {

	}

	AdminScope afterAdminScope = adminScopeDAO.getAdminScope("dinuka", APINames.ADMIN);
	assertEquals(afterAdminScope, null);
    }

    @Test(dependsOnMethods = { "testDeleteAdminScope" })
    public void testDeleteAllAdmins() {
	log.info("Start Test Delete Admin");
	String userName1 = "malinda";
	String userName2 = "dinuka";
	AdminDAO instance = AppContext.getInstance().getBean(AdminDAO.class);
	Admin admin1 = new Admin();
	Admin admin2 = new Admin();
	admin1 = instance.getAdmin(userName1);
	admin2 = instance.getAdmin(userName2);
	try {
	    instance.deleteAdmin(admin1);
	    assertEquals(instance.isAdminUserNameExist(userName1), false);
	    instance.deleteAdmin(admin2);
	    assertEquals(instance.isAdminUserNameExist(userName2), false);
	} catch (Exception ex) {

	}
    }

}

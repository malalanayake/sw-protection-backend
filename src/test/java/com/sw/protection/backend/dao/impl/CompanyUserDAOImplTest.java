package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.dao.CompanyUserDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;

@Test(groups = { "CompanyUserDAOImplTest" }, dependsOnGroups = { "CompanyDAOImplTest" })
public class CompanyUserDAOImplTest {

    public static final Logger log = Logger.getLogger(CompanyUserDAOImplTest.class.getName());

    @BeforeClass
    public void setUpClass() throws Exception {
	SharedInMemoryData.getInstance();
    }

    @Test
    public void saveUser() {
	log.info("Start Test save Company user");
	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	Company company = new Company();

	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key(UUID.randomUUID().toString());
	try {
	    companyDAO.saveCompany(company);
	} catch (Exception ex) {

	}
	// Check the DuplicateRecordException behavior
	String exceptionClass = "";
	try {
	    companyDAO.saveCompany(company);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DuplicateRecordException.class.toString());

	company = companyDAO.getCompany("sysensor");
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	CompanyUser companyUser = new CompanyUser();
	companyUser.setUser_name("dinuka");
	companyUser.setName("Dinuka Malalanayake");
	companyUser.setPass_word("test1");
	companyUser.setEmail("dinuka@gmail.com");
	companyUser.setApi_key(UUID.randomUUID().toString());
	companyUser.setCompany(company);

	Set<CompanyUserScope> companyUserScopeSet = companyUser.getUserScopeSet();
	CompanyUserScope companyUserScope = new CompanyUserScope();
	companyUserScope.setApi_name(APINames.COMPANY_SW);
	companyUserScope.setDel(true);
	companyUserScope.setGet(true);
	companyUserScope.setPost(true);
	companyUserScope.setPut(true);
	companyUserScope.setCompanyUser(companyUser);
	companyUserScopeSet.add(companyUserScope);
	companyUser.setUserScopeSet(companyUserScopeSet);
	try {
	    companyUserDAO.saveUser(companyUser);
	} catch (Exception ex) {

	}

    }

    @Test(dependsOnMethods = { "saveUser" })
    public void getAllUsers() {
	log.info("Start Test get All Company user");
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	assertEquals(companyUserDAO.getAllUsers().size(), 1);

	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	assertEquals(companyUser.getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyUser.getCompany().getUser_name(), "sysensor");
	assertEquals(companyUser.getCompany().getPass_word(), "test1");
	assertEquals(companyUser.getCompany().getEmail(), "sysensor@gmail.com");

	assertEquals(companyUser.getName(), "Dinuka Malalanayake");
	assertEquals(companyUser.getPass_word(), "test1");
	assertEquals(companyUser.getEmail(), "dinuka@gmail.com");

	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	Company company = companyDAO.getCompany("sysensor");
	company = companyDAO.loadAllPropertiesOfCompany(company.getId());
	assertEquals(company.getCompanyUserSet().size(), 1);

    }

    @Test(dependsOnMethods = { "getAllUsers" })
    public void loadAllPropertiesOfCompanyUser() {
	log.info("Start Test load Company user");
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	companyUser = companyUserDAO.loadAllPropertiesOfCompanyUser(companyUser.getId());
	assertEquals(companyUser.getUserScopeSet().size(), 1);
    }

    @Test(dependsOnMethods = { "loadAllPropertiesOfCompanyUser" })
    public void updateUser() {
	log.info("Start Test update Company user");
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	CompanyUser companyUserAlreadyUptodate = companyUserDAO.getUser("dinuka");
	companyUser = companyUserDAO.loadAllPropertiesOfCompanyUser(companyUser.getId());
	companyUserAlreadyUptodate = companyUserDAO.loadAllPropertiesOfCompanyUser(companyUserAlreadyUptodate.getId());

	assertEquals(companyUser.getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyUser.getCompany().getUser_name(), "sysensor");
	assertEquals(companyUser.getCompany().getPass_word(), "test1");
	assertEquals(companyUser.getCompany().getEmail(), "sysensor@gmail.com");

	assertEquals(companyUser.getName(), "Dinuka Malalanayake");
	assertEquals(companyUser.getPass_word(), "test1");
	assertEquals(companyUser.getEmail(), "dinuka@gmail.com");

	companyUser.setName("Dinuka");
	companyUser.setPass_word("test2");
	companyUser.setEmail("dinukanew@gmail.com");
	try {
	    companyUserDAO.updateUser(companyUser);
	} catch (Exception ex) {

	}

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companyUserDAO.updateUser(companyUserAlreadyUptodate);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	CompanyUser companyUser1 = companyUserDAO.getUser("dinuka");
	assertEquals(companyUser1.getName(), "Dinuka");
	assertEquals(companyUser1.getPass_word(), "test2");
	assertEquals(companyUser1.getEmail(), "dinukanew@gmail.com");
    }

    @Test(dependsOnMethods = { "updateUser" })
    public void deleteUser() {
	log.info("Start Test delete Company user");
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");

	CompanyUser companyUserAlreadyUptodate = companyUserDAO.getUser("dinuka");
	companyUserAlreadyUptodate = companyUserDAO.loadAllPropertiesOfCompanyUser(companyUserAlreadyUptodate.getId());
	companyUserAlreadyUptodate.setEmail("uptodateemail@gmail.com");

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companyUserDAO.updateUser(companyUserAlreadyUptodate);
	    companyUserDAO.deleteUser(companyUser);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	companyUser = companyUserDAO.getUser("dinuka");
	try {
	    companyUserDAO.deleteUser(companyUser);
	} catch (Exception ex) {

	}
	assertEquals(companyUserDAO.getUser("dinuka"), null);

	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	Company company = companyDAO.getCompany("sysensor");
	assertEquals(company.getUser_name(), "sysensor");
	try {
	    companyDAO.deleteCompany(company);
	} catch (Exception ex) {

	}
	assertEquals(companyDAO.getCompany("sysensor"), null);
    }
}

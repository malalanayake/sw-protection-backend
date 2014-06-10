package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
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
import com.sw.protection.backend.dao.CompanyUserScopeDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;

@Test(groups = { "CompanyUserScopeDAOImplTest" }, dependsOnGroups = { "CompanyUserDAOImplTest" })
public class CompanyUserScopeDAOImplTest {
	public static final Logger log = Logger.getLogger(CompanyUserScopeDAOImplTest.class.getName());

	@BeforeClass
	public void setUpClass() throws Exception {
		SharedInMemoryData.getInstance();
	}

	@Test
	public void saveNewCompanyUserScope() {
		log.info("Start Test save Company user scope");
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
		try {
			companyUserDAO.saveUser(companyUser);
		} catch (Exception ex) {

		}

		companyUser = companyUserDAO.getUser("dinuka");

		CompanyUserScope companyUserScope = new CompanyUserScope();
		companyUserScope.setApi_name(APINames.COMPANY_SW);
		companyUserScope.setDel(true);
		companyUserScope.setGet(true);
		companyUserScope.setPost(true);
		companyUserScope.setPut(true);
		companyUserScope.setCompanyUser(companyUser);

		CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(
				CompanyUserScopeDAO.class);
		try {
			companyUserScopeDAO.saveNewCompanyUserScope(companyUserScope);
		} catch (Exception ex) {

		}

	}

	@Test(dependsOnMethods = { "saveNewCompanyUserScope" })
	public void getAllCompanyUserScopes() {
		CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(
				CompanyUserScopeDAO.class);
		List<CompanyUserScope> companyUserScopeList = companyUserScopeDAO
				.getAllCompanyUserScopes("dinuka");
		assertEquals(companyUserScopeList.size(), 1);
		List<CompanyUserScope> companyUserScopeList2 = companyUserScopeDAO
				.getAllCompanyUserScopes("malinda");
		assertEquals(companyUserScopeList2, null);
	}

	@Test(dependsOnMethods = { "getAllCompanyUserScopes" })
	public void updateCompanyUserScope() {
		CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(
				CompanyUserScopeDAO.class);
		CompanyUserScope companyUserScope = companyUserScopeDAO.getCompanyUserScope("dinuka",
				APINames.COMPANY_SW);
		CompanyUserScope companyUserScopeAlreadyModified = companyUserScopeDAO.getCompanyUserScope(
				"dinuka", APINames.COMPANY_SW);
		assertEquals(companyUserScope.getApi_name(), APINames.COMPANY_SW);
		assertEquals(companyUserScope.isDel(), true);
		assertEquals(companyUserScope.isPost(), true);
		assertEquals(companyUserScope.isPut(), true);
		assertEquals(companyUserScope.isGet(), true);

		companyUserScope.setDel(false);
		companyUserScope.setPut(false);
		try {
			companyUserScopeDAO.updateCompanyUserScope(companyUserScope);
		} catch (Exception ex) {

		}

		// Check the RecordAlreadyModifiedException behavior
		String exceptionClass = "";
		try {
			companyUserScopeDAO.updateCompanyUserScope(companyUserScopeAlreadyModified);
		} catch (Exception ex) {
			exceptionClass = ex.getClass().toString();
		}
		assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

		CompanyUserScope companyUserScope2 = companyUserScopeDAO.getCompanyUserScope("dinuka",
				APINames.COMPANY_SW);
		assertEquals(companyUserScope2.getApi_name(), APINames.COMPANY_SW);
		assertEquals(companyUserScope2.isDel(), false);
		assertEquals(companyUserScope2.isPost(), true);
		assertEquals(companyUserScope2.isPut(), false);
		assertEquals(companyUserScope2.isGet(), true);

	}

	@Test(dependsOnMethods = { "updateCompanyUserScope" })
	public void deleteCompanyUserScope() {
		CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(
				CompanyUserScopeDAO.class);
		CompanyUserScope companyUserScope = companyUserScopeDAO.getCompanyUserScope("dinuka",
				APINames.COMPANY_SW);
		CompanyUserScope companyUserScopeAlreadyModified = companyUserScopeDAO.getCompanyUserScope(
				"dinuka", APINames.COMPANY_SW);
		companyUserScopeAlreadyModified.setPost(false);

		// Check the RecordAlreadyModifiedException behavior
		String exceptionClass = "";
		try {
			companyUserScopeDAO.updateCompanyUserScope(companyUserScopeAlreadyModified);
			companyUserScopeDAO.deleteCompanyUserScope(companyUserScope);
		} catch (Exception ex) {
			exceptionClass = ex.getClass().toString();
		}
		assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

		companyUserScope = companyUserScopeDAO.getCompanyUserScope("dinuka", APINames.COMPANY_SW);
		try {
			companyUserScopeDAO.deleteCompanyUserScope(companyUserScope);
		} catch (Exception ex) {

		}

		CompanyUserScope companyUserScope2 = companyUserScopeDAO.getCompanyUserScope("dinuka",
				APINames.COMPANY_SW);
		assertEquals(companyUserScope2, null);

		CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
		CompanyUser companyUser = companyUserDAO.getUser("dinuka");
		assertEquals(companyUser.getCompany().getName(), "Sysensor IT Solutions");
		assertEquals(companyUser.getCompany().getUser_name(), "sysensor");
		assertEquals(companyUser.getCompany().getPass_word(), "test1");
		assertEquals(companyUser.getCompany().getEmail(), "sysensor@gmail.com");

		assertEquals(companyUser.getName(), "Dinuka Malalanayake");
		assertEquals(companyUser.getPass_word(), "test1");
		assertEquals(companyUser.getEmail(), "dinuka@gmail.com");
		try {
			companyUserDAO.deleteUser(companyUser);
		} catch (Exception ex) {

		}
		CompanyUser companyUser2 = companyUserDAO.getUser("dinuka");
		assertEquals(companyUser2, null);

		CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
		Company company = companyDAO.getCompany("sysensor");
		assertEquals(company.getName(), "Sysensor IT Solutions");
		assertEquals(company.getUser_name(), "sysensor");
		assertEquals(company.getPass_word(), "test1");
		assertEquals(company.getEmail(), "sysensor@gmail.com");
		try {
			companyDAO.deleteCompany(company);
		} catch (Exception ex) {

		}
		Company company2 = companyDAO.getCompany("sysensor");
		assertEquals(company2, null);
	}

}

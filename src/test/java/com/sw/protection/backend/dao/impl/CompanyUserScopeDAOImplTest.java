package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
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
    public void saveNewCompanyUserScope() {
	log.info("Start Test save Company user scope");
	CompanyDAO companyDAO = new CompanyDAOImpl();
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

	company = companyDAO.getCompany("sysensor");
	CompanyUserDAO companyUserDAO = new CompanyUserDAOImpl();
	CompanyUser companyUser = new CompanyUser();
	companyUser.setUser_name("dinuka");
	companyUser.setName("Dinuka Malalanayake");
	companyUser.setPass_word("test1");
	companyUser.setEmail("dinuka@gmail.com");
	companyUser.setApi_key(UUID.randomUUID().toString());
	companyUser.setCompany(company);
	companyUserDAO.saveUser(companyUser);

	companyUser = companyUserDAO.getUser("dinuka");

	CompanyUserScope companyUserScope = new CompanyUserScope();
	companyUserScope.setApi_name(APINames.SOFTWARE);
	companyUserScope.setDel(true);
	companyUserScope.setGet(true);
	companyUserScope.setPost(true);
	companyUserScope.setPut(true);
	companyUserScope.setCompanyUser(companyUser);

	CompanyUserScopeDAO companyUserScopeDAO = new CompanyUserScopeDAOImpl();
	companyUserScopeDAO.saveNewCompanyUserScope(companyUserScope);

    }

    @Test(dependsOnMethods = { "saveNewCompanyUserScope" })
    public void getAllCompanyUserScopes() {
	CompanyUserScopeDAO companyUserScopeDAO = new CompanyUserScopeDAOImpl();
	List<CompanyUserScope> companyUserScopeList = companyUserScopeDAO.getAllCompanyUserScopes("dinuka");
	assertEquals(companyUserScopeList.size(), 1);
	List<CompanyUserScope> companyUserScopeList2 = companyUserScopeDAO.getAllCompanyUserScopes("malinda");
	assertEquals(companyUserScopeList2, null);
    }

    @Test(dependsOnMethods = { "getAllCompanyUserScopes" })
    public void updateCompanyUserScope() {
	CompanyUserScopeDAO companyUserScopeDAO = new CompanyUserScopeDAOImpl();
	CompanyUserScope companyUserScope = companyUserScopeDAO.getCompanyUserScope("dinuka", APINames.SOFTWARE);
	assertEquals(companyUserScope.getApi_name(), APINames.SOFTWARE);
	assertEquals(companyUserScope.isDel(), true);
	assertEquals(companyUserScope.isPost(), true);
	assertEquals(companyUserScope.isPut(), true);
	assertEquals(companyUserScope.isGet(), true);

	companyUserScope.setDel(false);
	companyUserScope.setPut(false);
	companyUserScopeDAO.updateCompanyUserScope(companyUserScope);

	CompanyUserScope companyUserScope2 = companyUserScopeDAO.getCompanyUserScope("dinuka", APINames.SOFTWARE);
	assertEquals(companyUserScope2.getApi_name(), APINames.SOFTWARE);
	assertEquals(companyUserScope2.isDel(), false);
	assertEquals(companyUserScope2.isPost(), true);
	assertEquals(companyUserScope2.isPut(), false);
	assertEquals(companyUserScope2.isGet(), true);

    }

    @Test(dependsOnMethods = { "updateCompanyUserScope" })
    public void deleteCompanyUserScope() {
	CompanyUserScopeDAO companyUserScopeDAO = new CompanyUserScopeDAOImpl();
	CompanyUserScope companyUserScope = companyUserScopeDAO.getCompanyUserScope("dinuka", APINames.SOFTWARE);
	companyUserScopeDAO.deleteCompanyUserScope(companyUserScope);
	CompanyUserScope companyUserScope2 = companyUserScopeDAO.getCompanyUserScope("dinuka", APINames.SOFTWARE);
	assertEquals(companyUserScope2, null);

	CompanyUserDAO companyUserDAO = new CompanyUserDAOImpl();
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	assertEquals(companyUser.getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyUser.getCompany().getUser_name(), "sysensor");
	assertEquals(companyUser.getCompany().getPass_word(), "test1");
	assertEquals(companyUser.getCompany().getEmail(), "sysensor@gmail.com");

	assertEquals(companyUser.getName(), "Dinuka Malalanayake");
	assertEquals(companyUser.getPass_word(), "test1");
	assertEquals(companyUser.getEmail(), "dinuka@gmail.com");

	companyUserDAO.deleteUser(companyUser);
	CompanyUser companyUser2 = companyUserDAO.getUser("dinuka");
	assertEquals(companyUser2, null);

	CompanyDAO companyDAO = new CompanyDAOImpl();
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

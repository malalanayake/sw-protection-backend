package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Set;
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
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;

@Test(groups = { "CompanyUserDAOImplTest" }, dependsOnGroups = { "CompanyDAOImplTest" })
public class CompanyUserDAOImplTest {

    public static final Logger log = Logger.getLogger(CompanyUserDAOImplTest.class.getName());

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
    public void saveUser() {
	log.info("Start Test save Company user");
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = new Company();
	
	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key(UUID.randomUUID().toString());
	companyDAO.saveCompany(company);
	
	company = companyDAO.getCompany("sysensor");
	CompanyUserDAO companyUserDAO = new CompanyUserDAOImpl();
	CompanyUser companyUser = new CompanyUser();
	companyUser.setUser_name("dinuka");
	companyUser.setName("Dinuka Malalanayake");
	companyUser.setPass_word("test1");
	companyUser.setEmail("dinuka@gmail.com");
	companyUser.setApi_key(UUID.randomUUID().toString());
	companyUser.setCompany(company);
	
	Set<CompanyUserScope> companyUserScopeSet = companyUser.getUserScopeSet();
	CompanyUserScope companyUserScope = new CompanyUserScope();
	companyUserScope.setApi_name(APINames.SOFTWARE);
	companyUserScope.setDel(true);
	companyUserScope.setGet(true);
	companyUserScope.setPost(true);
	companyUserScope.setPut(true);
	companyUserScope.setCompanyUser(companyUser);
	companyUserScopeSet.add(companyUserScope);
	companyUser.setUserScopeSet(companyUserScopeSet);
	
	companyUserDAO.saveUser(companyUser);

    }

    @Test(dependsOnMethods = { "saveUser" })
    public void getAllUsers() {
	log.info("Start Test get All Company user");
	CompanyUserDAO companyUserDAO = new CompanyUserDAOImpl();	
	assertEquals(companyUserDAO.getAllUsers().size(), 1);
	
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	assertEquals(companyUser.getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyUser.getCompany().getUser_name(), "sysensor");
	assertEquals(companyUser.getCompany().getPass_word(), "test1");
	assertEquals(companyUser.getCompany().getEmail(), "sysensor@gmail.com");
	
	assertEquals(companyUser.getName(), "Dinuka Malalanayake");
	assertEquals(companyUser.getPass_word(), "test1");
	assertEquals(companyUser.getEmail(), "dinuka@gmail.com");
	
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");
	company = companyDAO.loadAllPropertiesOfCompany(company.getId());
	assertEquals(company.getCompanyUserSet().size(), 1);
	
    }

    @Test(dependsOnMethods = { "getAllUsers" })
    public void loadAllPropertiesOfCompanyUser() {
	log.info("Start Test load Company user");
	CompanyUserDAO companyUserDAO = new CompanyUserDAOImpl();
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	companyUser = companyUserDAO.loadAllPropertiesOfCompanyUser(companyUser.getId());
	assertEquals(companyUser.getUserScopeSet().size(), 1);
    }

    @Test(dependsOnMethods = { "loadAllPropertiesOfCompanyUser" })
    public void updateUser() {
	log.info("Start Test update Company user");
	CompanyUserDAO companyUserDAO = new CompanyUserDAOImpl();
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	companyUser = companyUserDAO.loadAllPropertiesOfCompanyUser(companyUser.getId());
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
	companyUserDAO.updateUser(companyUser);
	
	CompanyUser companyUser1 = companyUserDAO.getUser("dinuka"); 	
	assertEquals(companyUser1.getName(), "Dinuka");
	assertEquals(companyUser1.getPass_word(), "test2");
	assertEquals(companyUser1.getEmail(), "dinukanew@gmail.com");
    }

    @Test(dependsOnMethods = { "updateUser" })
    public void deleteUser() {
	log.info("Start Test delete Company user");
	CompanyUserDAO companyUserDAO = new CompanyUserDAOImpl();
	CompanyUser companyUser = companyUserDAO.getUser("dinuka");
	companyUserDAO.deleteUser(companyUser);
	assertEquals(companyUserDAO.getUser("dinuka"), null);
	
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");
	assertEquals(company.getUser_name(), "sysensor");
	companyDAO.deleteCompany(company);
	assertEquals(companyDAO.getCompany("sysensor"), null);
    }
}

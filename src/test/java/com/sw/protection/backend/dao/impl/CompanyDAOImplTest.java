package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanyUser;

@Test(groups = { "CompanyDAOImplTest" }, dependsOnGroups = { "SuperAdminDAOImplTest" })
public class CompanyDAOImplTest {

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
    public void saveCompany() {
	log.info("Start Test save company");
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = new Company();

	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key(UUID.randomUUID().toString());

	Set<CompanyUser> companyUserSet = company.getCompanyUserSet();
	CompanyUser companyUser = new CompanyUser();
	companyUser.setName("Dinuka Malalanayake");
	companyUser.setUser_name("dinuka");
	companyUser.setPass_word("test1");
	companyUser.setEmail("dinuka@gmail.com");
	companyUser.setApi_key(UUID.randomUUID().toString());
	companyUser.setCompany(company);
	companyUserSet.add(companyUser);
	company.setCompanyUserSet(companyUserSet);

	Set<CompanyClient> companyClientSet = company.getCompanyClientSet();
	CompanyClient companyClient = new CompanyClient();
	companyClient.setName("Client Dinuka");
	companyClient.setUser_name("client1");
	companyClient.setPass_word("password");
	companyClient.setEmail("client@gmail.com");
	companyClient.setCompany(company);
	companyClientSet.add(companyClient);
	company.setCompanyClientSet(companyClientSet);

	Set<CompanySW> companySWSet = company.getCompanySWSet();
	CompanySW companySW = new CompanySW();
	companySW.setName("Application 1");
	companySW.setDescription("Small accounting system");
	companySW.setCompany(company);
	companySWSet.add(companySW);
	company.setCompanySWSet(companySWSet);

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
    }

    @Test(dependsOnMethods = { "saveCompany" })
    public void getAllCompanies() {
	log.info("Start Test get all companies");
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");

	assertEquals(company.getName(), "Sysensor IT Solutions");
	assertEquals(company.getUser_name(), "sysensor");
	assertEquals(company.getPass_word(), "test1");
	assertEquals(company.getEmail(), "sysensor@gmail.com");

    }

    @Test(dependsOnMethods = { "getAllCompanies" })
    public void loadAllPropertiesOfCompany() {
	log.info("Start Test load all properties of company");
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");
	Company company1 = companyDAO.loadAllPropertiesOfCompany(company.getId());

	assertEquals(company1.getName(), "Sysensor IT Solutions");
	assertEquals(company1.getUser_name(), "sysensor");
	assertEquals(company1.getPass_word(), "test1");
	assertEquals(company1.getEmail(), "sysensor@gmail.com");
	assertEquals(company1.getCompanyUserSet().size(), 1);
	assertEquals(company1.getCompanyClientSet().size(), 1);
	assertEquals(company1.getCompanySWSet().size(), 1);

    }

    @Test(dependsOnMethods = { "loadAllPropertiesOfCompany" })
    public void updateCompany() {
	log.info("Start Test update company");
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");
	Company companyAlreadyModi = companyDAO.getCompany("sysensor");
	company = companyDAO.loadAllPropertiesOfCompany(company.getId());
	companyAlreadyModi = companyDAO.loadAllPropertiesOfCompany(companyAlreadyModi.getId());

	assertEquals(company.getName(), "Sysensor IT Solutions");
	assertEquals(company.getUser_name(), "sysensor");
	assertEquals(company.getPass_word(), "test1");
	assertEquals(company.getEmail(), "sysensor@gmail.com");

	company.setName("Sysensor IT");
	company.setPass_word("test2");
	company.setEmail("sysensorit@gmail.com");
	String api_key = UUID.randomUUID().toString();
	company.setApi_key(api_key);
	try {
	    companyDAO.updateCompany(company);
	} catch (Exception ex) {

	}

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companyDAO.updateCompany(companyAlreadyModi);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	Company company1 = companyDAO.getCompany("sysensor");
	assertEquals(company1.getName(), "Sysensor IT");
	assertEquals(company1.getUser_name(), "sysensor");
	assertEquals(company1.getPass_word(), "test2");
	assertEquals(company1.getEmail(), "sysensorit@gmail.com");
	assertEquals(company1.getApi_key(), api_key);
    }

    @Test(dependsOnMethods = { "updateCompany" })
    public void deleteCompany() {
	log.info("Start Test delete Company");
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");

	Company companyAlreadyModi = companyDAO.getCompany("sysensor");
	companyAlreadyModi = companyDAO.loadAllPropertiesOfCompany(companyAlreadyModi.getId());
	companyAlreadyModi.setEmail("uptodateemail@gmail.com");

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companyDAO.updateCompany(companyAlreadyModi);
	    companyDAO.deleteCompany(company);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	company = companyDAO.getCompany("sysensor");
	try {
	    companyDAO.deleteCompany(company);
	} catch (Exception ex) {

	}
	assertEquals(companyDAO.getCompany("sysensor"), null);
    }
}

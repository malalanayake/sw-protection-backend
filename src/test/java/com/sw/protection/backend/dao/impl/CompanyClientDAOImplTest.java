package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.dao.CompanyClientDAO;
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;

@Test(groups = { "CompanyClientDAOImplTest" }, dependsOnGroups = { "CompanyUserScopeDAOImplTest" })
public class CompanyClientDAOImplTest {
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
    public void saveCompanyClient() {
	log.info("Start Test save Company client");
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

	CompanyClientDAO companyClientDAO = new CompanyClientDAOImpl();
	CompanyClient companyClient = new CompanyClient();
	companyClient.setName("Client Dinuka");
	companyClient.setUser_name("client1");
	companyClient.setPass_word("password");
	companyClient.setEmail("client@gmail.com");
	companyClient.setCompany(company);
	try {
	    companyClientDAO.saveCompanyClient(companyClient);
	} catch (Exception ex) {

	}

	// Check the DuplicateRecordException behavior
	String exceptionClass = "";
	try {
	    companyClientDAO.saveCompanyClient(companyClient);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DuplicateRecordException.class.toString());
    }

    @Test(dependsOnMethods = { "saveCompanyClient" })
    public void getAllCompanyClients() {
	log.info("Start Test get all Company client");
	CompanyClientDAO companyClientDAO = new CompanyClientDAOImpl();
	List<CompanyClient> companyClients = companyClientDAO.getAllCompanyClients();
	assertEquals(companyClients.size(), 1);

	CompanyClient companyClient = companyClientDAO.getCompanyClient("client1");
	assertEquals(companyClient.getName(), "Client Dinuka");
	assertEquals(companyClient.getUser_name(), "client1");
	assertEquals(companyClient.getPass_word(), "password");
	assertEquals(companyClient.getEmail(), "client@gmail.com");
    }

    @Test(dependsOnMethods = { "getAllCompanyClients" })
    public void updateCompanyClient() {
	log.info("Start Test update Company client");
	CompanyClientDAO companyClientDAO = new CompanyClientDAOImpl();
	CompanyClient companyClient = companyClientDAO.getCompanyClient("client1");
	CompanyClient companyClientAlredayUptodate = companyClientDAO.getCompanyClient("client1");
	companyClient.setName("Client Malinda");
	companyClient.setPass_word("password1");
	companyClient.setEmail("malinda@gmail.com");
	try {
	    companyClientDAO.updateCompanyClient(companyClient);
	} catch (Exception ex) {

	}

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companyClientDAO.updateCompanyClient(companyClientAlredayUptodate);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	CompanyClient companyClient2 = companyClientDAO.getCompanyClient("client1");
	assertEquals(companyClient2.getName(), "Client Malinda");
	assertEquals(companyClient2.getUser_name(), "client1");
	assertEquals(companyClient2.getPass_word(), "password1");
	assertEquals(companyClient2.getEmail(), "malinda@gmail.com");
    }

    @Test(dependsOnMethods = { "updateCompanyClient" })
    public void deleteCompanyClient() {
	log.info("Start Test delete Company client");
	CompanyClientDAO companyClientDAO = new CompanyClientDAOImpl();
	CompanyClient companyClient = companyClientDAO.getCompanyClient("client1");
	CompanyClient companyClientAlredayUptodate = companyClientDAO.getCompanyClient("client1");
	companyClientAlredayUptodate.setEmail("updateemail@gmail.com");
	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companyClientDAO.updateCompanyClient(companyClientAlredayUptodate);
	    companyClientDAO.deleteCompanyClient(companyClient);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	companyClient = companyClientDAO.getCompanyClient("client1");
	try {

	    companyClientDAO.deleteCompanyClient(companyClient);
	} catch (Exception ex) {

	}

	assertEquals(companyClientDAO.getCompanyClient("client1"), null);

	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");
	assertEquals(company.getUser_name(), "sysensor");
	try {
	    companyDAO.deleteCompany(company);
	} catch (Exception ex) {

	}
	assertEquals(companyDAO.getCompany("sysensor"), null);
    }

}

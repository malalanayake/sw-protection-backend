package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.dao.CompanySWDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanySW;

@Test(groups = { "CompanySWDAOImplTest" }, dependsOnGroups = { "CompanyClientDAOImplTest" })
public class CompanySWDAOImplTest {
    public static final Logger log = Logger.getLogger(CompanySWDAOImplTest.class.getName());

    @BeforeClass
    public void setUpClass() throws Exception {
	SharedInMemoryData.getInstance();
    }

    @Test
    public void saveCompanySW() {
	log.info("Start Test save Company Software");
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

	CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
	CompanySW companySW = new CompanySW();
	companySW.setName("Application 1");
	companySW.setDescription("Small accounting system");
	companySW.setCompany(company);
	try {
	    companySWDAO.saveCompanySW(companySW);
	} catch (Exception ex) {

	}

	// Check the DuplicateRecordException behavior
	String exceptionClass = "";
	try {
	    companySWDAO.saveCompanySW(companySW);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DuplicateRecordException.class.toString());
    }

    @Test(dependsOnMethods = { "saveCompanySW" })
    public void getAllCompanySWs() {
	log.info("Start Test get all Company Software");
	CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
	List<CompanySW> comanySWList = companySWDAO.getAllCompanySWs();

	assertEquals(comanySWList.size(), 1);
	CompanySW companySW = companySWDAO.getCompanySW("sysensor", "Application 1");
	assertEquals(companySW.getName(), "Application 1");
	assertEquals(companySW.getDescription(), "Small accounting system");

    }

    @Test(dependsOnMethods = { "getAllCompanySWs" })
    public void updateCompanySW() {
	log.info("Start Test update Company Software");
	CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
	CompanySW companySW = companySWDAO.getCompanySW("sysensor", "Application 1");
	CompanySW companySWAlreadyModi = companySWDAO.getCompanySW("sysensor", "Application 1");
	companySW.setDescription("Application for stock management");
	try {
	    companySWDAO.updateCompanySW(companySW);
	} catch (Exception ex) {

	}

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companySWDAO.updateCompanySW(companySWAlreadyModi);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	CompanySW companySW2 = companySWDAO.getCompanySW("sysensor", "Application 1");

	assertEquals(companySW2.getName(), "Application 1");
	assertEquals(companySW.getDescription(), "Application for stock management");
    }

    @Test(dependsOnMethods = { "updateCompanySW" })
    public void deleteCompanySW() {
	log.info("Start Test update Company Software");
	CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
	CompanySW companySW = companySWDAO.getCompanySW("sysensor", "Application 1");
	CompanySW companySWAlreadyModi = companySWDAO.getCompanySW("sysensor", "Application 1");
	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companySWDAO.updateCompanySW(companySWAlreadyModi);
	    companySWDAO.deleteCompanySW(companySW);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	companySW = companySWDAO.getCompanySW("sysensor", "Application 1");
	try {
	    companySWDAO.deleteCompanySW(companySW);
	} catch (Exception ex) {

	}

	CompanySW companySW2 = companySWDAO.getCompanySW("sysensor", "Application 1");
	assertEquals(companySW2, null);

	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	Company company = companyDAO.getCompany("sysensor");
	try {
	    companyDAO.deleteCompany(company);
	} catch (Exception ex) {

	}
	Company company2 = companyDAO.getCompany("sysensor");
	assertEquals(company2, null);
    }

}

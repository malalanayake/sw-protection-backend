package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.dao.CompanySWDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanySW;

@Test(groups = { "CompanySWDAOImplTest" }, dependsOnGroups = { "CompanyClientDAOImplTest" })
public class CompanySWDAOImplTest {
    public static final Logger log = Logger.getLogger(CompanySWDAOImplTest.class.getName());

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
    public void saveCompanySW() {
	log.info("Start Test save Company Software");
	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = new Company();

	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key(UUID.randomUUID().toString());
	companyDAO.saveCompany(company);

	CompanySWDAO companySWDAO = new CompanySWDAOImpl();
	CompanySW companySW = new CompanySW();
	companySW.setName("Application 1");
	companySW.setDescription("Small accounting system");
	companySW.setCompany(company);

	companySWDAO.saveCompanySW(companySW);
    }

    @Test(dependsOnMethods = { "saveCompanySW" })
    public void getAllCompanySWs() {
	log.info("Start Test get all Company Software");
	CompanySWDAO companySWDAO = new CompanySWDAOImpl();
	List<CompanySW> comanySWList = companySWDAO.getAllCompanySWs();

	assertEquals(comanySWList.size(), 1);
	CompanySW companySW = companySWDAO.getCompanySW("sysensor", "Application 1");
	assertEquals(companySW.getName(), "Application 1");
	assertEquals(companySW.getDescription(), "Small accounting system");

    }

    @Test(dependsOnMethods = { "getAllCompanySWs" })
    public void updateCompanySW() {
	log.info("Start Test update Company Software");
	CompanySWDAO companySWDAO = new CompanySWDAOImpl();
	CompanySW companySW = companySWDAO.getCompanySW("sysensor", "Application 1");
	companySW.setDescription("Application for stock management");

	companySWDAO.updateCompanySW(companySW);
	CompanySW companySW2 = companySWDAO.getCompanySW("sysensor", "Application 1");

	assertEquals(companySW2.getName(), "Application 1");
	assertEquals(companySW.getDescription(), "Application for stock management");
    }

    @Test(dependsOnMethods = { "updateCompanySW" })
    public void deleteCompanySW() {
	log.info("Start Test update Company Software");
	CompanySWDAO companySWDAO = new CompanySWDAOImpl();
	CompanySW companySW = companySWDAO.getCompanySW("sysensor", "Application 1");
	companySWDAO.deleteCompanySW(companySW);

	CompanySW companySW2 = companySWDAO.getCompanySW("sysensor", "Application 1");
	assertEquals(companySW2, null);

	CompanyDAO companyDAO = new CompanyDAOImpl();
	Company company = companyDAO.getCompany("sysensor");
	companyDAO.deleteCompany(company);
	Company company2 = companyDAO.getCompany("sysensor");
	assertEquals(company2, null);
    }

}
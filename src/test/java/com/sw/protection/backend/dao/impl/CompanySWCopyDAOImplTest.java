package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.CompanyClientDAO;
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.dao.CompanySWCopyDAO;
import com.sw.protection.backend.dao.CompanySWDAO;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;

@Test(groups = { "CompanySWCopyDAOImplTest" }, dependsOnGroups = { "CompanySWDAOImplTest" })
public class CompanySWCopyDAOImplTest {
    public static final Logger log = Logger.getLogger(CompanySWCopyDAOImplTest.class.getName());

    @BeforeClass
    public void setUpClass() throws Exception {
	SharedInMemoryData.getInstance();
    }

    @Test
    public void saveCompanySWCopy() {
	log.info("Start Test save company software copy");
	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
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

	CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
	CompanySW companySW2 = companySWDAO.getCompanySW("sysensor", "Application 1");

	CompanyClientDAO companyClientDAO = AppContext.getInstance().getBean(CompanyClientDAO.class);
	CompanyClient companyClient2 = companyClientDAO.getCompanyClient("client1");

	CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance().getBean(CompanySWCopyDAO.class);
	CompanySWCopy companySWCopy = new CompanySWCopy();
	companySWCopy.setCompany_client(companyClient2);
	companySWCopy.setCompany_sw(companySW2);
	companySWCopy.setExpire_date(Formatters.formatDate(new Date()));
	companySWCopy.setMother_board("M_BOARD_ID");
	companySWCopy.setHd("HD_ID");
	companySWCopy.setMac("MAC_ID");
	try {
	    companySWCopyDAO.saveCompanySWCopy(companySWCopy);
	} catch (Exception ex) {

	}

	// Check the DuplicateRecordException behavior
	String exceptionClass = "";
	try {
	    companySWCopyDAO.saveCompanySWCopy(companySWCopy);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DuplicateRecordException.class.toString());
    }

    @Test(dependsOnMethods = { "saveCompanySWCopy" })
    public void getAllCompanySWCopies() {
	log.info("Start Test get all company software copy");
	CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance().getBean(CompanySWCopyDAO.class);
	List<CompanySWCopy> allSwCopyList = companySWCopyDAO.getAllCompanySWCopies();
	assertEquals(allSwCopyList.size(), 1);

	CompanySWCopy companySWCopy = companySWCopyDAO.getCompanySWCopy("client1", "Application 1", "M_BOARD_ID",
		"HD_ID", "MAC_ID");

	assertEquals(companySWCopy.getCompany_client().getUser_name(), "client1");
	assertEquals(companySWCopy.getCompany_sw().getName(), "Application 1");
	assertEquals(companySWCopy.getMother_board(), "M_BOARD_ID");
	assertEquals(companySWCopy.getHd(), "HD_ID");
	assertEquals(companySWCopy.getMac(), "MAC_ID");

    }

    @Test(dependsOnMethods = { "getAllCompanySWCopies" })
    public void updateCompanySWCopy() {
	log.info("Start Test update company software copy");
	CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance().getBean(CompanySWCopyDAO.class);
	CompanySWCopy companySWCopy = companySWCopyDAO.getCompanySWCopy("client1", "Application 1", "M_BOARD_ID",
		"HD_ID", "MAC_ID");
	CompanySWCopy companySWCopyAlreadyModi = companySWCopyDAO.getCompanySWCopy("client1", "Application 1",
		"M_BOARD_ID", "HD_ID", "MAC_ID");

	companySWCopy.setExpire_date(Formatters.formatDate(new Date()));
	try {
	    companySWCopyDAO.updateCompanySWCopy(companySWCopy);
	} catch (Exception ex) {

	}

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companySWCopyDAO.updateCompanySWCopy(companySWCopyAlreadyModi);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	CompanySWCopy companySWCopy2 = companySWCopyDAO.getCompanySWCopy("client1", "Application 1", "M_BOARD_ID",
		"HD_ID", "MAC_ID");

	assertEquals(companySWCopy.getCompany_client().getUser_name(), "client1");
	assertEquals(companySWCopy.getCompany_sw().getName(), "Application 1");
	assertEquals(companySWCopy.getMother_board(), "M_BOARD_ID");
	assertEquals(companySWCopy.getHd(), "HD_ID");
	assertEquals(companySWCopy.getMac(), "MAC_ID");

    }

    @Test(dependsOnMethods = { "updateCompanySWCopy" })
    public void deleteCompanySWCopy() {
	log.info("Start Test delete company software copy");
	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance().getBean(CompanySWCopyDAO.class);
	CompanySWCopy companySWCopy = companySWCopyDAO.getCompanySWCopy("client1", "Application 1", "M_BOARD_ID",
		"HD_ID", "MAC_ID");
	CompanySWCopy companySWCopyAlreadyModi = companySWCopyDAO.getCompanySWCopy("client1", "Application 1",
		"M_BOARD_ID", "HD_ID", "MAC_ID");

	// Check the RecordAlreadyModifiedException behavior
	String exceptionClass = "";
	try {
	    companySWCopyDAO.updateCompanySWCopy(companySWCopyAlreadyModi);
	    companySWCopyDAO.deleteCompanySWCopy(companySWCopy);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

	companySWCopy = companySWCopyDAO.getCompanySWCopy("client1", "Application 1", "M_BOARD_ID", "HD_ID", "MAC_ID");
	try {
	    companySWCopyDAO.deleteCompanySWCopy(companySWCopy);
	} catch (Exception ex) {

	}

	Company company = companyDAO.getCompany("sysensor");
	try {
	    companyDAO.deleteCompany(company);
	} catch (Exception ex) {

	}
	assertEquals(companyDAO.getCompany("sysensor"), null);
    }

}

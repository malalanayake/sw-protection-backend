package com.sw.protection.backend.service.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.decoder.impl.JSONDecoder;
import com.sw.protection.backend.encoder.impl.JSONEncoder;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.service.CompanySWService;
import com.sw.protection.backend.service.CompanyService;

@Test(groups = { "CompanySWServiceImplTest" }, dependsOnGroups = { "CompanyUserScopeServiceImplTest" })
public class CompanySWServiceImplTest {

    public static String companyReturn = "";

    @BeforeClass
    public static void setUpClass() throws Exception {
	SharedInMemoryData.getInstance();
	CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
	String companyEncoded = "{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test1\",\"email\":\"sysensor@gmail.com\"}";
	companyReturn = "";
	try {
	    companyReturn = companyService.saveCompany(EncoderDecoderType.JSON, companyEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @AfterClass
    public void tearDown() throws Exception {
	CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
	String listofCompanies = companyService.getAllCompanies(EncoderDecoderType.JSON, 1, 20);
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	List<Company> list = (List<Company>) decoder.decodeObjectList(ObjectType.COMPANY, listofCompanies);
	String encodedCompany = "";
	for (Company company : list) {
	    encodedCompany = encoder.encodeObject(ObjectType.COMPANY, company);
	    companyService.deleteCompany(EncoderDecoderType.JSON, encodedCompany);
	}
    }

    @Test
    public void saveCompanySW() {
	CompanySWService companySwService = AppContext.getInstance().getBean(CompanySWService.class);
	String companyEncoded = "{\"name\":\"Application 1\",\"description\":\"Small accounting system\","
		+ "\"company\":" + companyReturn + "}";
	String companySWReturn = "";
	try {
	    companySWReturn = companySwService.saveCompanySW(EncoderDecoderType.JSON, companyEncoded);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(companySWReturn.contains("id"), true);

	String duplicateExcep = "";
	try {
	    companySWReturn = companySwService.saveCompanySW(EncoderDecoderType.JSON, companyEncoded);
	} catch (Exception e) {
	    duplicateExcep = e.getClass().toString();
	}
	assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

	String decodingExcep = "";
	try {
	    companySWReturn = companySwService.saveCompanySW(EncoderDecoderType.JSON, companyEncoded + "}{");
	} catch (Exception e) {
	    decodingExcep = e.getClass().toString();
	}
	assertEquals(decodingExcep, DecodingException.class.toString());

	String companyUserEncoded2 = "{\"name\":\"Application 2\",\"description\":\"Small accounting system\","
		+ "\"company\":" + companyReturn + "}";
	String companyUserReturn2 = "";
	try {
	    companyUserReturn2 = companySwService.saveCompanySW(EncoderDecoderType.JSON, companyUserEncoded2);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(companyUserReturn2.contains("id"), true);
    }

    @Test(dependsOnMethods = { "saveCompanySW" })
    public void getCompanySW() {
	CompanySWService companyUserService = AppContext.getInstance().getBean(CompanySWService.class);
	String companyUserReturn = "";
	try {
	    companyUserReturn = companyUserService.getCompanySW(EncoderDecoderType.JSON, "sysensor", "Application 2");
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(companyUserReturn.contains("id"), true);
	assertEquals(companyUserReturn.contains("company"), true);
    }

    @Test(dependsOnMethods = { "getCompanySW" })
    public void getAllCompanySW() {
	CompanySWService companySwService = AppContext.getInstance().getBean(CompanySWService.class);
	String companySWReturn = "";
	JSONDecoder decoder = new JSONDecoder();
	List<CompanySW> companySWs = null;
	try {
	    companySWReturn = companySwService.getAllCompanySW(EncoderDecoderType.JSON, 1, 20);
	    companySWs = (List<CompanySW>) decoder.decodeObjectList(ObjectType.SOFTWARE, companySWReturn);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(companySWs.size(), 2);

	try {
	    companySWReturn = companySwService.getAllCompanySW(EncoderDecoderType.JSON, 2, 1);
	    companySWs = (List<CompanySW>) decoder.decodeObjectList(ObjectType.SOFTWARE, companySWReturn);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	assertEquals(companySWs.size(), 1);
    }

    @Test(dependsOnMethods = { "getAllCompanySW" })
    public void updateCompanySW() {
	CompanySWService companySWService = AppContext.getInstance().getBean(CompanySWService.class);
	String companySWReturn = "";
	try {
	    companySWReturn = companySWService.getCompanySW(EncoderDecoderType.JSON, "sysensor", "Application 2");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(companySWReturn.contains("id"), true);
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	CompanySW companySW = null;
	CompanySW companySW2 = null;
	try {
	    companySW = (CompanySW) decoder.decodeObject(ObjectType.SOFTWARE, companySWReturn);
	    companySW2 = (CompanySW) decoder.decodeObject(ObjectType.SOFTWARE, companySWReturn);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	String encodedString = "";
	String finalString = "";
	// check by changing the user name
	companySW.setName("Application 1");
	String requiredDataExp = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySW);
	    finalString = companySWService.updateCompanySW(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    requiredDataExp = e.getClass().toString();
	}
	assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

	companySW.setName("Application 2");
	companySW.setDescription("Application 1 description");
	encodedString = "";
	finalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySW);
	    finalString = companySWService.updateCompanySW(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {

	}

	assertEquals(finalString.contains("Application 1 description"), true);

	companySW2.setDescription("New_Description");
	encodedString = "";
	finalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySW2);
	    companySWService.updateCompanySW(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    finalString = e.getClass().toString();
	}

	assertEquals(finalString, RecordAlreadyModifiedException.class.toString());
    }

    @Test(dependsOnMethods = { "updateCompanySW" })
    public void deleteCompanySW() {
	CompanySWService companySWService = AppContext.getInstance().getBean(CompanySWService.class);
	String companySWReturn = "";
	try {
	    companySWReturn = companySWService.getCompanySW(EncoderDecoderType.JSON, "sysensor", "Application 1");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(companySWReturn.contains("id"), true);
	JSONDecoder decoder = new JSONDecoder();
	JSONEncoder encoder = new JSONEncoder();
	CompanySW companySw = null;
	CompanySW companySw2 = null;
	try {
	    companySw = (CompanySW) decoder.decodeObject(ObjectType.SOFTWARE, companySWReturn);
	    companySw2 = (CompanySW) decoder.decodeObject(ObjectType.SOFTWARE, companySWReturn);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	String encodedString = "";
	String finalString = "";
	// check by changing the user name
	companySw.setName("malinda1");
	String requiredDataExp = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySw);
	    finalString = companySWService.deleteCompanySW(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    requiredDataExp = e.getClass().toString();
	}
	assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

	companySw.setName("Application 1");
	companySw.setDescription("App1");
	encodedString = "";
	String updatedfinalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySw);
	    updatedfinalString = companySWService.updateCompanySW(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {

	}

	assertEquals(updatedfinalString.contains("App1"), true);

	encodedString = "";
	finalString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySw2);
	    companySWService.deleteCompanySW(EncoderDecoderType.JSON, encodedString);
	} catch (Exception e) {
	    finalString = e.getClass().toString();
	}

	assertEquals(finalString, RecordAlreadyModifiedException.class.toString());

	encodedString = "";
	finalString = "";
	try {
	    companySWService.deleteCompanySW(EncoderDecoderType.JSON, updatedfinalString);
	} catch (Exception e) {
	    finalString = e.getClass().toString();
	}

	companySWReturn = "";
	try {
	    companySWReturn = companySWService.getCompanySW(EncoderDecoderType.JSON, "sysensor", "Application 1");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertEquals(companySWReturn, "");
    }
}

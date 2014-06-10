package com.sw.protection.backend.service.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.decoder.impl.JSONDecoder;
import com.sw.protection.backend.encoder.impl.JSONEncoder;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.service.CompanyService;

@Test(groups = { "CompanyServiceImplTest" }, dependsOnGroups = { "AdminScopeServiceImplTest" })
public class CompanyServiceImplTest {
	@BeforeClass
	public static void setUpClass() throws Exception {
		SharedInMemoryData.getInstance();
	}

	@AfterClass
	public void tearDown() throws Exception {
		CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
		String listofCompanies = companyService.getAllCompanies(EncoderDecoderType.JSON, 1, 20);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		List<Company> list = (List<Company>) decoder.decodeObjectList(ObjectType.COMPANY,
				listofCompanies);
		String encodedCompany = "";
		for (Company company : list) {
			encodedCompany = encoder.encodeObject(ObjectType.COMPANY, company);
			companyService.deleteCompany(EncoderDecoderType.JSON, encodedCompany);
		}
	}

	@Test
	public void saveCompany() {
		CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
		String companyEncoded = "{\"name\":\"SySensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test1\",\"email\":\"sysensor@gmail.com\"}";
		String companyReturn = "";
		try {
			companyReturn = companyService.saveCompany(EncoderDecoderType.JSON, companyEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyReturn.contains("id"), true);

		String duplicateExcep = "";
		try {
			companyReturn = companyService.saveCompany(EncoderDecoderType.JSON, companyEncoded);
		} catch (Exception e) {
			duplicateExcep = e.getClass().toString();
		}
		assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

		String decodingExcep = "";
		try {
			companyReturn = companyService.saveCompany(EncoderDecoderType.JSON, companyEncoded
					+ "}{");
		} catch (Exception e) {
			decodingExcep = e.getClass().toString();
		}
		assertEquals(decodingExcep, DecodingException.class.toString());

		String companyEncodedNew = "{\"name\":\"SysensorIT\",\"user_name\":\"sysensorit\",\"email\":\"sysensor@gmail.com\"}";
		String operationRollBackExcep = "";
		try {
			companyReturn = companyService.saveCompany(EncoderDecoderType.JSON, companyEncodedNew);
		} catch (Exception e) {
			operationRollBackExcep = e.getClass().toString();
		}
		assertEquals(operationRollBackExcep, OperationRollBackException.class.toString());

		String adminEncoded2 = "{\"name\":\"Sysensor\",\"user_name\":\"sysensorit\",\"pass_word\":\"test1\",\"email\":\"sysensor@gmail.com\"}";
		String adminReturn2 = "";
		try {
			adminReturn2 = companyService.saveCompany(EncoderDecoderType.JSON, adminEncoded2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(adminReturn2.contains("id"), true);
	}

	@Test(dependsOnMethods = { "saveCompany" })
	public void getAllCompanies() {
		CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
		String companyReturn = "";
		JSONDecoder decoder = new JSONDecoder();
		List<Company> companies = null;
		try {
			companyReturn = companyService.getAllCompanies(EncoderDecoderType.JSON, 1, 20);
			companies = (List<Company>) decoder.decodeObjectList(ObjectType.COMPANY, companyReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companies.size(), 2);

		try {
			companyReturn = companyService.getAllCompanies(EncoderDecoderType.JSON, 2, 1);
			companies = (List<Company>) decoder.decodeObjectList(ObjectType.COMPANY, companyReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companies.size(), 1);
	}

	@Test(dependsOnMethods = { "getAllCompanies" })
	public void getCompany() {
		CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
		String adminEncoded = "{\"user_name\":\"sysensor\"}";
		String adminReturn = "";
		try {
			adminReturn = companyService.getCompany(EncoderDecoderType.JSON, adminEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(adminReturn.contains("api_key"), true);
		assertEquals(adminReturn.contains("pass_word"), false);
	}

	@Test(dependsOnMethods = { "getCompany" })
	public void updateCompany() {
		CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
		String companyEncoded = "{\"user_name\":\"sysensor\"}";
		String companyReturn = "";
		try {
			companyReturn = companyService.getCompany(EncoderDecoderType.JSON, companyEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyReturn.contains("api_key"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		Company company = null;
		Company company2 = null;
		try {
			company = (Company) decoder.decodeObject(ObjectType.COMPANY, companyReturn);
			company2 = (Company) decoder.decodeObject(ObjectType.COMPANY, companyReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the user name
		company.setUser_name("malinda");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
			finalString = companyService.updateCompany(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		company.setName("New_Name");
		company.setEmail("test@malalanayake.com");
		company.setUser_name("sysensor");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
			finalString = companyService.updateCompany(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {

		}

		assertEquals(finalString.contains("New_Name"), true);
		assertEquals(finalString.contains("test@malalanayake.com"), true);

		company2.setName("New_Name");
		company2.setEmail("test@malalanayake.com");
		company2.setUser_name("sysensor");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY, company2);
			companyService.updateCompany(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());
	}

	@Test(dependsOnMethods = { "updateCompany" })
	public void deleteCompany() {
		CompanyService companyService = AppContext.getInstance().getBean(CompanyService.class);
		String companyEncoded = "{\"user_name\":\"sysensorit\"}";
		String companyReturn = "";
		try {
			companyReturn = companyService.getCompany(EncoderDecoderType.JSON, companyEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyReturn.contains("api_key"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		Company company = null;
		Company company2 = null;
		try {
			company = (Company) decoder.decodeObject(ObjectType.COMPANY, companyReturn);
			company2 = (Company) decoder.decodeObject(ObjectType.COMPANY, companyReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the user name
		company.setUser_name("malinda");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
			finalString = companyService.deleteCompany(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		company.setName("New_Name");
		company.setEmail("test@malalanayake.com");
		company.setUser_name("sysensorit");
		encodedString = "";
		String updatedfinalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
			updatedfinalString = companyService.updateCompany(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {

		}

		assertEquals(updatedfinalString.contains("New_Name"), true);
		assertEquals(updatedfinalString.contains("test@malalanayake.com"), true);

		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY, company2);
			companyService.deleteCompany(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());

		encodedString = "";
		finalString = "";
		try {
			companyService.deleteCompany(EncoderDecoderType.JSON, updatedfinalString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		companyReturn = "";
		try {
			companyReturn = companyService.getCompany(EncoderDecoderType.JSON, companyEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyReturn, "");
	}
}

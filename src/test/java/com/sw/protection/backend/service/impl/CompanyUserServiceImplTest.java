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
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.service.CompanyService;
import com.sw.protection.backend.service.CompanyUserService;

@Test(groups = { "CompanyUserServiceImplTest" }, dependsOnGroups = { "CompanyServiceImplTest" })
public class CompanyUserServiceImplTest {

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
		List<Company> list = (List<Company>) decoder.decodeObjectList(ObjectType.COMPANY,
				listofCompanies);
		String encodedCompany = "";
		for (Company company : list) {
			encodedCompany = encoder.encodeObject(ObjectType.COMPANY, company);
			companyService.deleteCompany(EncoderDecoderType.JSON, encodedCompany);
		}
	}

	@Test
	public void saveCompanyUser() {
		CompanyUserService companyService = AppContext.getInstance().getBean(
				CompanyUserService.class);
		String companyEncoded = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		String companyUserReturn = "";
		try {
			companyUserReturn = companyService.saveCompanyUser(EncoderDecoderType.JSON,
					companyEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUserReturn.contains("id"), true);

		String duplicateExcep = "";
		try {
			companyUserReturn = companyService.saveCompanyUser(EncoderDecoderType.JSON,
					companyEncoded);
		} catch (Exception e) {
			duplicateExcep = e.getClass().toString();
		}
		assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

		String decodingExcep = "";
		try {
			companyUserReturn = companyService.saveCompanyUser(EncoderDecoderType.JSON,
					companyEncoded + "}{");
		} catch (Exception e) {
			decodingExcep = e.getClass().toString();
		}
		assertEquals(decodingExcep, DecodingException.class.toString());

		String companyUserEncodedNew = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"malinda\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		String operationRollBackExcep = "";
		try {
			companyUserReturn = companyService.saveCompanyUser(EncoderDecoderType.JSON,
					companyUserEncodedNew);
		} catch (Exception e) {
			operationRollBackExcep = e.getClass().toString();
		}
		assertEquals(operationRollBackExcep, OperationRollBackException.class.toString());

		String companyUserEncoded2 = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"malinda\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		String companyUserReturn2 = "";
		try {
			companyUserReturn2 = companyService.saveCompanyUser(EncoderDecoderType.JSON,
					companyUserEncoded2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUserReturn2.contains("id"), true);
	}

	@Test(dependsOnMethods = { "saveCompanyUser" })
	public void getAllCompanyUsers() {
		CompanyUserService companyUserService = AppContext.getInstance().getBean(
				CompanyUserService.class);
		String companyUserReturn = "";
		JSONDecoder decoder = new JSONDecoder();
		List<CompanyUser> companyUsers = null;
		try {
			companyUserReturn = companyUserService.getAllCompanyUsers(EncoderDecoderType.JSON, 1,
					20);
			companyUsers = (List<CompanyUser>) decoder.decodeObjectList(ObjectType.COMPANY_USER,
					companyUserReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUsers.size(), 2);

		try {
			companyUserReturn = companyUserService
					.getAllCompanyUsers(EncoderDecoderType.JSON, 2, 1);
			companyUsers = (List<CompanyUser>) decoder.decodeObjectList(ObjectType.COMPANY_USER,
					companyUserReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUsers.size(), 1);
	}

	@Test(dependsOnMethods = { "getAllCompanyUsers" })
	public void getCompanyUser() {
		CompanyUserService companyUserService = AppContext.getInstance().getBean(
				CompanyUserService.class);
		String companyUserEncoded = "{\"user_name\":\"dinuka\"}";
		String companyUserReturn = "";
		try {
			companyUserReturn = companyUserService.getCompanyUser(EncoderDecoderType.JSON,
					companyUserEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUserReturn.contains("api_key"), true);
		assertEquals(companyUserReturn.contains("pass_word"), false);
	}

	@Test(dependsOnMethods = { "getCompanyUser" })
	public void updateCompanyUser() {
		CompanyUserService companyUserService = AppContext.getInstance().getBean(
				CompanyUserService.class);
		String companyUserEncoded = "{\"user_name\":\"dinuka\"}";
		String companyUserReturn = "";
		try {
			companyUserReturn = companyUserService.getCompanyUser(EncoderDecoderType.JSON,
					companyUserEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyUserReturn.contains("api_key"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		CompanyUser companyUser = null;
		CompanyUser companyUser2 = null;
		try {
			companyUser = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER,
					companyUserReturn);
			companyUser2 = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER,
					companyUserReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the user name
		companyUser.setUser_name("malinda1");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
			finalString = companyUserService.updateCompanyUser(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		companyUser.setName("New_Name");
		companyUser.setEmail("test@malalanayake.com");
		companyUser.setUser_name("dinuka");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
			finalString = companyUserService.updateCompanyUser(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {

		}

		assertEquals(finalString.contains("New_Name"), true);
		assertEquals(finalString.contains("test@malalanayake.com"), true);

		companyUser2.setName("New_Name");
		companyUser2.setEmail("test@malalanayake.com");
		companyUser2.setUser_name("dinuka");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser2);
			companyUserService.updateCompanyUser(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());
	}

	@Test(dependsOnMethods = { "updateCompanyUser" })
	public void deleteCompanyUser() {
		CompanyUserService companyUserService = AppContext.getInstance().getBean(
				CompanyUserService.class);
		String companyUserEncoded = "{\"user_name\":\"dinuka\"}";
		String companyUserReturn = "";
		try {
			companyUserReturn = companyUserService.getCompanyUser(EncoderDecoderType.JSON,
					companyUserEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyUserReturn.contains("api_key"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		CompanyUser companyUser = null;
		CompanyUser companyUser2 = null;
		try {
			companyUser = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER,
					companyUserReturn);
			companyUser2 = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER,
					companyUserReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the user name
		companyUser.setUser_name("malinda1");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
			finalString = companyUserService.deleteCompanyUser(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		companyUser.setName("New_Name");
		companyUser.setEmail("test@malalanayake.com");
		companyUser.setUser_name("dinuka");
		encodedString = "";
		String updatedfinalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
			updatedfinalString = companyUserService.updateCompanyUser(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {

		}

		assertEquals(updatedfinalString.contains("New_Name"), true);
		assertEquals(updatedfinalString.contains("test@malalanayake.com"), true);

		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser2);
			companyUserService.deleteCompanyUser(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());

		encodedString = "";
		finalString = "";
		try {
			companyUserService.deleteCompanyUser(EncoderDecoderType.JSON, updatedfinalString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		companyUserReturn = "";
		try {
			companyUserReturn = companyUserService.getCompanyUser(EncoderDecoderType.JSON,
					companyUserEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyUserReturn, "");
	}
}

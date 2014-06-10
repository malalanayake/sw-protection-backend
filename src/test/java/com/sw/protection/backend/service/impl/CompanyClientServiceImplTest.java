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
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.service.CompanyClientService;
import com.sw.protection.backend.service.CompanyService;

@Test(groups = { "CompanyClientServiceImplTest" }, dependsOnGroups = { "CompanySWServiceImplTest" })
public class CompanyClientServiceImplTest {

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
	public void saveCompanyClient() {
		CompanyClientService companyClientService = AppContext.getInstance().getBean(
				CompanyClientService.class);
		String companyEncoded = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		String companyClientReturn = "";
		try {
			companyClientReturn = companyClientService.saveCompanyClient(EncoderDecoderType.JSON,
					companyEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyClientReturn.contains("id"), true);

		String duplicateExcep = "";
		try {
			companyClientReturn = companyClientService.saveCompanyClient(EncoderDecoderType.JSON,
					companyEncoded);
		} catch (Exception e) {
			duplicateExcep = e.getClass().toString();
		}
		assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

		String decodingExcep = "";
		try {
			companyClientReturn = companyClientService.saveCompanyClient(EncoderDecoderType.JSON,
					companyEncoded + "}{");
		} catch (Exception e) {
			decodingExcep = e.getClass().toString();
		}
		assertEquals(decodingExcep, DecodingException.class.toString());

		String companyClientEncodedNew = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"malinda\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		String operationRollBackExcep = "";
		try {
			companyClientReturn = companyClientService.saveCompanyClient(EncoderDecoderType.JSON,
					companyClientEncodedNew);
		} catch (Exception e) {
			operationRollBackExcep = e.getClass().toString();
		}
		assertEquals(operationRollBackExcep, OperationRollBackException.class.toString());

		String companyClientEncoded2 = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"malinda\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		String companyUserReturn2 = "";
		try {
			companyUserReturn2 = companyClientService.saveCompanyClient(EncoderDecoderType.JSON,
					companyClientEncoded2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUserReturn2.contains("id"), true);
	}

	@Test(dependsOnMethods = { "saveCompanyClient" })
	public void getCompanyClient() {
		CompanyClientService companyClientService = AppContext.getInstance().getBean(
				CompanyClientService.class);
		String companyClientReturn = "";
		try {
			companyClientReturn = companyClientService.getCompanyClient(EncoderDecoderType.JSON,
					"dinuka");
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyClientReturn.contains("\"user_name\":\"dinuka\""), true);
		assertEquals(companyClientReturn.contains("api_key"), true);
		assertEquals(companyClientReturn.contains("pass_word"), false);
	}

	@Test(dependsOnMethods = { "getCompanyClient" })
	public void getAllCompanyClients() {
		CompanyClientService companyClientService = AppContext.getInstance().getBean(
				CompanyClientService.class);
		String companyClientReturn = "";
		JSONDecoder decoder = new JSONDecoder();
		List<CompanyClient> companyClients = null;
		try {
			companyClientReturn = companyClientService.getAllCompanyClients(
					EncoderDecoderType.JSON, 1, 20);
			companyClients = (List<CompanyClient>) decoder.decodeObjectList(ObjectType.CLIENT,
					companyClientReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyClients.size(), 2);

		try {
			companyClientReturn = companyClientService.getAllCompanyClients(
					EncoderDecoderType.JSON, 2, 1);
			companyClients = (List<CompanyClient>) decoder.decodeObjectList(ObjectType.CLIENT,
					companyClientReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyClients.size(), 1);
	}

	@Test(dependsOnMethods = { "getAllCompanyClients" })
	public void updateCompanyClient() {
		CompanyClientService companyClientService = AppContext.getInstance().getBean(
				CompanyClientService.class);
		String companyClientReturn = "";
		try {
			companyClientReturn = companyClientService.getCompanyClient(EncoderDecoderType.JSON,
					"dinuka");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyClientReturn.contains("api_key"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		CompanyClient companyClient = null;
		CompanyClient companyClient2 = null;
		try {
			companyClient = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT,
					companyClientReturn);
			companyClient2 = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT,
					companyClientReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the user name
		companyClient.setUser_name("malinda1");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
			finalString = companyClientService.updateCompanyClient(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		companyClient.setName("New_Name");
		companyClient.setEmail("test@malalanayake.com");
		companyClient.setUser_name("dinuka");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
			finalString = companyClientService.updateCompanyClient(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {

		}

		assertEquals(finalString.contains("New_Name"), true);
		assertEquals(finalString.contains("test@malalanayake.com"), true);

		companyClient2.setName("New_Name");
		companyClient2.setEmail("test@malalanayake.com");
		companyClient2.setUser_name("dinuka");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient2);
			companyClientService.updateCompanyClient(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());
	}

	@Test(dependsOnMethods = { "updateCompanyClient" })
	public void deleteCompanyClient() {
		CompanyClientService companyClientService = AppContext.getInstance().getBean(
				CompanyClientService.class);
		String companyClientReturn = "";
		try {
			companyClientReturn = companyClientService.getCompanyClient(EncoderDecoderType.JSON,
					"dinuka");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyClientReturn.contains("api_key"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		CompanyClient companyClient = null;
		CompanyClient companyClient2 = null;
		try {
			companyClient = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT,
					companyClientReturn);
			companyClient2 = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT,
					companyClientReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the user name
		companyClient.setUser_name("malinda1");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
			finalString = companyClientService.deleteCompanyClient(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		companyClient.setName("New_Name");
		companyClient.setEmail("test@malalanayake.com");
		companyClient.setUser_name("dinuka");
		encodedString = "";
		String updatedfinalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
			updatedfinalString = companyClientService.updateCompanyClient(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {

		}

		assertEquals(updatedfinalString.contains("New_Name"), true);
		assertEquals(updatedfinalString.contains("test@malalanayake.com"), true);

		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient2);
			companyClientService.deleteCompanyClient(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());

		encodedString = "";
		finalString = "";
		try {
			companyClientService.deleteCompanyClient(EncoderDecoderType.JSON, updatedfinalString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		companyClientReturn = "";
		try {
			companyClientReturn = companyClientService.getCompanyClient(EncoderDecoderType.JSON,
					"dinuka");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyClientReturn, "");
	}

}

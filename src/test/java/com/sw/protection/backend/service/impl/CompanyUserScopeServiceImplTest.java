package com.sw.protection.backend.service.impl;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.decoder.impl.JSONDecoder;
import com.sw.protection.backend.encoder.impl.JSONEncoder;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyUserScope;
import com.sw.protection.backend.service.CompanyService;
import com.sw.protection.backend.service.CompanyUserScopeService;
import com.sw.protection.backend.service.CompanyUserService;

@Test(groups = { "CompanyUserScopeServiceImplTest" }, dependsOnGroups = { "CompanyUserServiceImplTest" })
public class CompanyUserScopeServiceImplTest {

	public static String companyReturn = "";
	public static String companyUserReturn = "";

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

		CompanyUserService companyUserService = AppContext.getInstance().getBean(
				CompanyUserService.class);
		String companyUserEncoded = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		try {
			companyUserReturn = companyUserService.saveCompanyUser(EncoderDecoderType.JSON,
					companyUserEncoded);
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
	public void saveCompanyUserScope() {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);
		String companyUserEncoded = "{\"api_name\":\"software\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
				+ "\"companyUser\":" + companyUserReturn + "}";
		String companyUserScopeReturn = "";
		try {
			companyUserScopeReturn = companyUserScopeService.saveCompanyUserScope(
					EncoderDecoderType.JSON, companyUserEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String duplicateExcep = "";
		try {
			companyUserScopeReturn = companyUserScopeService.saveCompanyUserScope(
					EncoderDecoderType.JSON, companyUserEncoded);
		} catch (Exception e) {
			duplicateExcep = e.getClass().toString();
		}
		assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

		String decodingExcep = "";
		try {
			companyUserScopeReturn = companyUserScopeService.saveCompanyUserScope(
					EncoderDecoderType.JSON, companyUserEncoded + "}{");
		} catch (Exception e) {
			decodingExcep = e.getClass().toString();
		}
		assertEquals(decodingExcep, DecodingException.class.toString());

		String companyUserRequiredData = "{\"api_name\":\"software\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
				+ "\"companyUser\":{}}";
		String requiredDataNotFoundExcep = "";
		try {
			companyUserScopeReturn = companyUserScopeService.saveCompanyUserScope(
					EncoderDecoderType.JSON, companyUserRequiredData);
		} catch (Exception e) {
			decodingExcep = e.getClass().toString();
		}
		assertEquals(decodingExcep, RequiredDataNotFoundException.class.toString());

		String companyUserEncoded2 = "{\"api_name\":\"company-user\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
				+ "\"companyUser\":" + companyUserReturn + "}";
		String companyUserReturn2 = "";
		try {
			companyUserReturn2 = companyUserScopeService.saveCompanyUserScope(
					EncoderDecoderType.JSON, companyUserEncoded2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUserReturn2.contains("id"), true);
	}

	@Test(dependsOnMethods = { "saveCompanyUserScope" })
	public void getCompanyUserScope() {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);
		String companyUserScopeReturn = "";
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		try {
			String companyUserScopeEncoded = companyUserScopeService.getCompanyUserScope(
					EncoderDecoderType.JSON, "dinuka", "company-user");
			assertEquals(companyUserScopeEncoded.contains("\"get\":true"), true);
			assertEquals(companyUserScopeEncoded.contains("\"post\":true"), true);
			assertEquals(companyUserScopeEncoded.contains("\"put\":true"), true);
			assertEquals(companyUserScopeEncoded.contains("\"del\":true"), true);

			CompanyUserScope decodedCompanyUserScope = (CompanyUserScope) decoder.decodeObject(
					ObjectType.COMPANY_USER_SCOPE, companyUserScopeEncoded);
			decodedCompanyUserScope.setDel(false);
			decodedCompanyUserScope.setPost(false);
			decodedCompanyUserScope.setPut(false);
			decodedCompanyUserScope.setGet(false);
			String companyUserScopeFinalEncoded = encoder.encodeObject(
					ObjectType.COMPANY_USER_SCOPE, decodedCompanyUserScope);
			companyUserScopeReturn = companyUserScopeService.updateCompanyUserScope(
					EncoderDecoderType.JSON, companyUserScopeFinalEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUserScopeReturn.contains("id"), true);
		assertEquals(companyUserScopeReturn.contains("\"get\":true"), false);
		assertEquals(companyUserScopeReturn.contains("\"post\":true"), false);
		assertEquals(companyUserScopeReturn.contains("\"put\":true"), false);
		assertEquals(companyUserScopeReturn.contains("\"del\":true"), false);
	}

	@Test(dependsOnMethods = { "getCompanyUserScope" })
	public void getCompanyUserScopes() {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);
		String adminScopeReturn = "";
		try {
			adminScopeReturn = companyUserScopeService.getCompanyUserScopes(
					EncoderDecoderType.JSON, "dinuka");
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(adminScopeReturn.contains("\"api_name\":\"company-user\""), true);
		assertEquals(adminScopeReturn.contains("\"api_name\":\"software\""), true);

		String requiredDataNotfoundExcep = "";
		try {
			adminScopeReturn = companyUserScopeService.getCompanyUserScopes(
					EncoderDecoderType.JSON, "");
		} catch (Exception e) {
			requiredDataNotfoundExcep = e.getClass().toString();
		}
		assertEquals(requiredDataNotfoundExcep, RequiredDataNotFoundException.class.toString());
	}

	@Test(dependsOnMethods = { "getCompanyUserScopes" })
	public void isAccessGrantedFor() {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);
		String companyUserScopeReturn = "";
		try {
			companyUserScopeReturn = companyUserScopeService.isAccessGrantedFor("dinuka",
					"company-user", APIOperations.GET.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyUserScopeReturn, "false");

		try {
			companyUserScopeReturn = companyUserScopeService.isAccessGrantedFor("dinuka",
					"software", APIOperations.GET.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companyUserScopeReturn, "true");
	}

	@Test(dependsOnMethods = { "isAccessGrantedFor" })
	public void updateCompanyUserScope() {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);
		String companyUserScopeReturn = "";
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		try {
			String companyUserScopeEncoded = companyUserScopeService.getCompanyUserScope(
					EncoderDecoderType.JSON, "dinuka", "software");
			assertEquals(companyUserScopeEncoded.contains("\"get\":true"), true);
			assertEquals(companyUserScopeEncoded.contains("\"post\":true"), true);
			assertEquals(companyUserScopeEncoded.contains("\"put\":true"), true);
			assertEquals(companyUserScopeEncoded.contains("\"del\":true"), true);

			CompanyUserScope decodedAdminScope = (CompanyUserScope) decoder.decodeObject(
					ObjectType.COMPANY_USER_SCOPE, companyUserScopeEncoded);
			decodedAdminScope.setDel(false);
			decodedAdminScope.setPost(false);
			decodedAdminScope.setPut(false);
			decodedAdminScope.setGet(false);
			String companyUserScopeFinalEncoded = encoder.encodeObject(
					ObjectType.COMPANY_USER_SCOPE, decodedAdminScope);
			companyUserScopeReturn = companyUserScopeService.updateCompanyUserScope(
					EncoderDecoderType.JSON, companyUserScopeFinalEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companyUserScopeReturn.contains("id"), true);
		assertEquals(companyUserScopeReturn.contains("\"get\":true"), false);
		assertEquals(companyUserScopeReturn.contains("\"post\":true"), false);
		assertEquals(companyUserScopeReturn.contains("\"put\":true"), false);
		assertEquals(companyUserScopeReturn.contains("\"del\":true"), false);
	}

	@Test(dependsOnMethods = { "updateCompanyUserScope" })
	public void deleteCompanyUserScope() {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);
		String companyUserScopeReturn = "";
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		try {
			String companyUserScopeEncoded = companyUserScopeService.getCompanyUserScope(
					EncoderDecoderType.JSON, "dinuka", "software");
			assertEquals(companyUserScopeEncoded.contains("id"), true);
			assertEquals(companyUserScopeEncoded.contains("\"get\":true"), false);
			assertEquals(companyUserScopeEncoded.contains("\"post\":true"), false);
			assertEquals(companyUserScopeEncoded.contains("\"put\":true"), false);
			assertEquals(companyUserScopeEncoded.contains("\"del\":true"), false);

			CompanyUserScope decodedAdminScope = (CompanyUserScope) decoder.decodeObject(
					ObjectType.COMPANY_USER_SCOPE, companyUserScopeEncoded);

			String companyUserScopeFinalEncoded = encoder.encodeObject(
					ObjectType.COMPANY_USER_SCOPE, decodedAdminScope);
			companyUserScopeReturn = companyUserScopeService.deleteCompanyUserScope(
					EncoderDecoderType.JSON, companyUserScopeFinalEncoded);
			companyUserScopeEncoded = companyUserScopeService.getCompanyUserScope(
					EncoderDecoderType.JSON, "dinuka", "software");
			assertEquals(companyUserScopeEncoded, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

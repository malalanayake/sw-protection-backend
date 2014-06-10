package com.sw.protection.backend.service.impl;

import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
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
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.service.CompanyClientService;
import com.sw.protection.backend.service.CompanySWCopyService;
import com.sw.protection.backend.service.CompanySWService;
import com.sw.protection.backend.service.CompanyService;

@Test(groups = { "CompanySWCopyServiceImplTest" }, dependsOnGroups = { "CompanyClientServiceImplTest" })
public class CompanySWCopyServiceImplTest {

	public static String companyReturn = "";
	public static String companyClientReturn = "";
	public static String companySWReturn = "";

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

		CompanyClientService companyClientService = AppContext.getInstance().getBean(
				CompanyClientService.class);
		String companyClientEncoded = "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"email\":\"dinuka@gmail.com\","
				+ "\"company\":" + companyReturn + "}";
		companyClientReturn = "";
		try {
			companyClientReturn = companyClientService.saveCompanyClient(EncoderDecoderType.JSON,
					companyClientEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		CompanySWService companySwService = AppContext.getInstance()
				.getBean(CompanySWService.class);
		String companySWEncoded = "{\"name\":\"Application 1\",\"description\":\"Small accounting system\","
				+ "\"company\":" + companyReturn + "}";
		companySWReturn = "";
		try {
			companySWReturn = companySwService.saveCompanySW(EncoderDecoderType.JSON,
					companySWEncoded);
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
	public void saveCompanySWCopy() {
		CompanySWCopyService companySWCopyService = AppContext.getInstance().getBean(
				CompanySWCopyService.class);
		String expDate = Formatters.formatDate(new Date());
		String companySWCopyEncoded = "{\"mother_board\":\"M_BOARD_ID\",\"hd\":\"HD_ID\",\"mac\":\"MAC_ID\",\"expire_date\":\""
				+ expDate
				+ "\",\"company_sw\":"
				+ companySWReturn
				+ ","
				+ "\"company_client\":"
				+ companyClientReturn + "}";
		String companySWCopyReturn = "";
		try {
			companySWCopyReturn = companySWCopyService.saveCompanySWCopy(EncoderDecoderType.JSON,
					companySWCopyEncoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companySWCopyReturn.contains("id"), true);

		String duplicateExcep = "";
		try {
			companySWCopyReturn = companySWCopyService.saveCompanySWCopy(EncoderDecoderType.JSON,
					companySWCopyEncoded);
		} catch (Exception e) {
			duplicateExcep = e.getClass().toString();
		}
		assertEquals(duplicateExcep, DuplicateRecordException.class.toString());

		String decodingExcep = "";
		try {
			companySWCopyReturn = companySWCopyService.saveCompanySWCopy(EncoderDecoderType.JSON,
					companySWCopyEncoded + "}{");
		} catch (Exception e) {
			decodingExcep = e.getClass().toString();
		}
		assertEquals(decodingExcep, DecodingException.class.toString());

		String companyClientEncoded2 = "{\"mother_board\":\"M_BOARD_ID_2\",\"hd\":\"HD_ID_2\",\"mac\":\"MAC_ID_2\",\"expire_date\":\""
				+ expDate
				+ "\",\"company_sw\":"
				+ companySWReturn
				+ ","
				+ "\"company_client\":"
				+ companyClientReturn + "}";
		String companySWCopyReturn2 = "";
		try {
			companySWCopyReturn2 = companySWCopyService.saveCompanySWCopy(EncoderDecoderType.JSON,
					companyClientEncoded2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companySWCopyReturn2.contains("id"), true);
	}

	@Test(dependsOnMethods = { "saveCompanySWCopy" })
	public void getCompanySWCopy() {
		CompanySWCopyService companySwCopyService = AppContext.getInstance().getBean(
				CompanySWCopyService.class);
		String companySWCopyReturn = "";
		try {
			companySWCopyReturn = companySwCopyService.getCompanySWCopy(EncoderDecoderType.JSON,
					"dinuka", "Application 1", "M_BOARD_ID", "HD_ID", "MAC_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companySWCopyReturn.contains("id"), true);
		assertEquals(companySWCopyReturn.contains("company_sw"), true);
		assertEquals(companySWCopyReturn.contains("company_client"), true);
	}

	@Test(dependsOnMethods = { "getCompanySWCopy" })
	public void getAllCompanySWCopy() {
		CompanySWCopyService companySwCopyService = AppContext.getInstance().getBean(
				CompanySWCopyService.class);
		String companySWCopyReturn = "";
		JSONDecoder decoder = new JSONDecoder();
		List<CompanySWCopy> companySWCopies = null;
		try {
			companySWCopyReturn = companySwCopyService.getAllCompanySWCopy(EncoderDecoderType.JSON,
					1, 20);
			companySWCopies = (List<CompanySWCopy>) decoder.decodeObjectList(
					ObjectType.SOFTWARE_COPY, companySWCopyReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companySWCopies.size(), 2);

		try {
			companySWCopyReturn = companySwCopyService.getAllCompanySWCopy(EncoderDecoderType.JSON,
					2, 1);
			companySWCopies = (List<CompanySWCopy>) decoder.decodeObjectList(
					ObjectType.SOFTWARE_COPY, companySWCopyReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(companySWCopies.size(), 1);
	}

	@Test(dependsOnMethods = { "getAllCompanySWCopy" })
	public void updateCompanySWCopy() {
		CompanySWCopyService companySWCopyService = AppContext.getInstance().getBean(
				CompanySWCopyService.class);
		String expDate = Formatters.formatDate(new Date());
		String companySWReturn = "";
		try {
			companySWReturn = companySWCopyService.getCompanySWCopy(EncoderDecoderType.JSON,
					"dinuka", "Application 1", "M_BOARD_ID", "HD_ID", "MAC_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companySWReturn.contains("id"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		CompanySWCopy companySWCopy = null;
		CompanySWCopy companySWCopy2 = null;
		try {
			companySWCopy = (CompanySWCopy) decoder.decodeObject(ObjectType.SOFTWARE_COPY,
					companySWReturn);
			companySWCopy2 = (CompanySWCopy) decoder.decodeObject(ObjectType.SOFTWARE_COPY,
					companySWReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the mac address as empty string
		companySWCopy.setMac("");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
			finalString = companySWCopyService.updateCompanySWCopy(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		companySWCopy.setExpire_date(expDate);
		companySWCopy.setMac("MAC_ID");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
			finalString = companySWCopyService.updateCompanySWCopy(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {

		}

		assertEquals(finalString.contains("MAC_ID"), true);

		companySWCopy2.setExpire_date(expDate);
		companySWCopy2.setMac("MAC_ID");
		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy2);
			companySWCopyService.updateCompanySWCopy(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());
	}

	@Test(dependsOnMethods = { "updateCompanySWCopy" })
	public void deleteCompanySWCopy() {
		CompanySWCopyService companySWCopyService = AppContext.getInstance().getBean(
				CompanySWCopyService.class);
		String expDate = Formatters.formatDate(new Date());
		String companySWCopyReturn = "";
		try {
			companySWCopyReturn = companySWCopyService.getCompanySWCopy(EncoderDecoderType.JSON,
					"dinuka", "Application 1", "M_BOARD_ID", "HD_ID", "MAC_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companySWCopyReturn.contains("id"), true);
		JSONDecoder decoder = new JSONDecoder();
		JSONEncoder encoder = new JSONEncoder();
		CompanySWCopy companySwCopy = null;
		CompanySWCopy companySwCopy2 = null;
		try {
			companySwCopy = (CompanySWCopy) decoder.decodeObject(ObjectType.SOFTWARE_COPY,
					companySWCopyReturn);
			companySwCopy2 = (CompanySWCopy) decoder.decodeObject(ObjectType.SOFTWARE_COPY,
					companySWCopyReturn);
		} catch (DecodingException e) {
			e.printStackTrace();
		}

		String encodedString = "";
		String finalString = "";
		// check by changing the user name
		companySwCopy.setMac("");
		String requiredDataExp = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySwCopy);
			finalString = companySWCopyService.deleteCompanySWCopy(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {
			requiredDataExp = e.getClass().toString();
		}
		assertEquals(requiredDataExp, RequiredDataNotFoundException.class.toString());

		companySwCopy.setExpire_date(expDate);
		companySwCopy.setMac("MAC_ID");
		encodedString = "";
		String updatedfinalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySwCopy);
			updatedfinalString = companySWCopyService.updateCompanySWCopy(EncoderDecoderType.JSON,
					encodedString);
		} catch (Exception e) {

		}

		assertEquals(updatedfinalString.contains("id"), true);

		encodedString = "";
		finalString = "";
		try {
			encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySwCopy2);
			companySWCopyService.deleteCompanySWCopy(EncoderDecoderType.JSON, encodedString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		assertEquals(finalString, RecordAlreadyModifiedException.class.toString());

		encodedString = "";
		finalString = "";
		try {
			companySWCopyService.deleteCompanySWCopy(EncoderDecoderType.JSON, updatedfinalString);
		} catch (Exception e) {
			finalString = e.getClass().toString();
		}

		companySWCopyReturn = "";
		try {
			companySWCopyReturn = companySWCopyService.getCompanySWCopy(EncoderDecoderType.JSON,
					"dinuka", "Application 1", "M_BOARD_ID", "HD_ID", "MAC_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(companySWCopyReturn, "");
	}

}

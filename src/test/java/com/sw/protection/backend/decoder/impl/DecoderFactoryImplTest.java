package com.sw.protection.backend.decoder.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;
import com.sw.protection.backend.entity.SuperAdmin;
import com.sw.protection.backend.entity.Trace;
import com.sw.protection.backend.entity.UsageData;

@Test(groups = { "DecoderFactoryImplTest" }, dependsOnGroups = { "EncoderFactoryImplTest" })
public class DecoderFactoryImplTest {
    public static final Logger log = Logger.getLogger(DecoderFactoryImplTest.class.getName());
    DecoderFactory decoderFactory;
    Decoder decoder;

    @BeforeClass
    public void initialize() {
	decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	decoder = decoderFactory.getDecoder(EncoderDecoderType.JSON);
    }

    @Test
    public void testJsonDecoderSuperAdmin() {
	log.info("Test JSON Decoder for Super Admin");
	String encodedObj = "{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}";

	SuperAdmin admin2 = null;
	try {
	    admin2 = (SuperAdmin) decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(admin2);
	assertEquals(admin2.getId().toString(), "1");
	assertEquals(admin2.getName(), "super_dinuka");
	assertEquals(admin2.getUser_name(), "dinuka");
	assertEquals(admin2.getPass_word(), "test1");
	assertEquals(admin2.getEmail(), "dinuka@gmail.com");
	assertEquals(admin2.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"},"
		+ "{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}]";

	List<SuperAdmin> superAdminList = null;
	try {
	    superAdminList = (List<SuperAdmin>) decoder.decodeObjectList(ObjectType.SUPER_ADMIN, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (SuperAdmin admin3 : superAdminList) {
	    assertNotNull(admin3);
	    assertEquals(admin3.getId().toString(), "1");
	    assertEquals(admin3.getName(), "super_dinuka");
	    assertEquals(admin3.getUser_name(), "dinuka");
	    assertEquals(admin3.getPass_word(), "test1");
	    assertEquals(admin3.getEmail(), "dinuka@gmail.com");
	    assertEquals(admin3.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderSuperAdmin" })
    public void testJsonDecoderAdmin() {
	log.info("Test JSON Decoder for Admin");
	String encodedObj = "{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}";

	Admin admin = null;
	try {
	    admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(admin);
	assertEquals(admin.getId().toString(), "1");
	assertEquals(admin.getName(), "super_dinuka");
	assertEquals(admin.getUser_name(), "dinuka");
	assertEquals(admin.getPass_word(), "test1");
	assertEquals(admin.getEmail(), "dinuka@gmail.com");
	assertEquals(admin.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"},"
		+ "{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}]";

	List<Admin> adminList = null;
	try {
	    adminList = (List<Admin>) decoder.decodeObjectList(ObjectType.ADMIN, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (Admin admin1 : adminList) {
	    assertNotNull(admin1);
	    assertEquals(admin1.getId().toString(), "1");
	    assertEquals(admin1.getName(), "super_dinuka");
	    assertEquals(admin1.getUser_name(), "dinuka");
	    assertEquals(admin1.getPass_word(), "test1");
	    assertEquals(admin1.getEmail(), "dinuka@gmail.com");
	    assertEquals(admin1.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderAdmin" })
    public void testJsonDecoderCompany() {
	log.info("Test JSON Decoder for Company");
	String encodedObj = "{\"id\":\"1\",\"name\":\"Sysensor IT\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensorit@gmail.com\"}";

	Company company = null;
	try {
	    company = (Company) decoder.decodeObject(ObjectType.COMPANY, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(company);
	assertEquals(company.getId().toString(), "1");
	assertEquals(company.getName(), "Sysensor IT");
	assertEquals(company.getUser_name(), "dinuka");
	assertEquals(company.getPass_word(), "test2");
	assertEquals(company.getEmail(), "sysensorit@gmail.com");
	assertEquals(company.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"1\",\"name\":\"Sysensor IT\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensorit@gmail.com\"},"
		+ "{\"id\":\"1\",\"name\":\"Sysensor IT\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensorit@gmail.com\"}]";

	List<Company> companyList = null;
	try {
	    companyList = (List<Company>) decoder.decodeObjectList(ObjectType.COMPANY, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (Company company1 : companyList) {
	    assertNotNull(company1);
	    assertEquals(company1.getId().toString(), "1");
	    assertEquals(company1.getName(), "Sysensor IT");
	    assertEquals(company1.getUser_name(), "dinuka");
	    assertEquals(company1.getPass_word(), "test2");
	    assertEquals(company1.getEmail(), "sysensorit@gmail.com");
	    assertEquals(company1.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderCompany" })
    public void testJsonDecoderCompanyUser() {
	log.info("Test JSON Decoder for CompanyUser");
	String encodedObj = "{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}";

	CompanyUser companyUser = null;
	try {
	    companyUser = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(companyUser);
	assertEquals(companyUser.getId().toString(), "1");
	assertEquals(companyUser.getName(), "Dinuka Malalanayake");
	assertEquals(companyUser.getUser_name(), "dinuka");
	assertEquals(companyUser.getPass_word(), "test2");
	assertEquals(companyUser.getEmail(), "dinuka@gmail.com");
	assertEquals(companyUser.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	assertEquals(companyUser.getCompany().getId().toString(), "2");
	assertEquals(companyUser.getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyUser.getCompany().getUser_name(), "sysensor");
	assertEquals(companyUser.getCompany().getPass_word(), "test2");
	assertEquals(companyUser.getCompany().getEmail(), "sysensor@gmail.com");
	assertEquals(companyUser.getCompany().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}},"
		+ "{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}]";

	List<CompanyUser> companyList = null;
	try {
	    companyList = (List<CompanyUser>) decoder.decodeObjectList(ObjectType.COMPANY_USER, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (CompanyUser companyUser1 : companyList) {
	    assertNotNull(companyUser1);
	    assertEquals(companyUser1.getId().toString(), "1");
	    assertEquals(companyUser1.getName(), "Dinuka Malalanayake");
	    assertEquals(companyUser1.getUser_name(), "dinuka");
	    assertEquals(companyUser1.getPass_word(), "test2");
	    assertEquals(companyUser1.getEmail(), "dinuka@gmail.com");
	    assertEquals(companyUser1.getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	    assertEquals(companyUser1.getCompany().getId().toString(), "2");
	    assertEquals(companyUser1.getCompany().getName(), "Sysensor IT Solutions");
	    assertEquals(companyUser1.getCompany().getUser_name(), "sysensor");
	    assertEquals(companyUser1.getCompany().getPass_word(), "test2");
	    assertEquals(companyUser1.getCompany().getEmail(), "sysensor@gmail.com");
	    assertEquals(companyUser1.getCompany().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderCompanyUser" })
    public void testJsonDecoderCompanyClient() {
	log.info("Test JSON Decoder for CompanyClient");
	String encodedObj = "{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}";

	CompanyClient companyClient = null;
	try {
	    companyClient = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(companyClient);
	assertEquals(companyClient.getId().toString(), "1");
	assertEquals(companyClient.getName(), "Dinuka Malalanayake");
	assertEquals(companyClient.getUser_name(), "dinuka");
	assertEquals(companyClient.getPass_word(), "test2");
	assertEquals(companyClient.getEmail(), "dinuka@gmail.com");
	assertEquals(companyClient.getCompany().getId().toString(), "2");
	assertEquals(companyClient.getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyClient.getCompany().getUser_name(), "sysensor");
	assertEquals(companyClient.getCompany().getPass_word(), "test2");
	assertEquals(companyClient.getCompany().getEmail(), "sysensor@gmail.com");
	assertEquals(companyClient.getCompany().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}},"
		+ "{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}]";

	List<CompanyClient> companyList = null;
	try {
	    companyList = (List<CompanyClient>) decoder.decodeObjectList(ObjectType.CLIENT, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (CompanyClient companyClient1 : companyList) {
	    assertNotNull(companyClient1);
	    assertEquals(companyClient1.getId().toString(), "1");
	    assertEquals(companyClient1.getName(), "Dinuka Malalanayake");
	    assertEquals(companyClient1.getUser_name(), "dinuka");
	    assertEquals(companyClient1.getPass_word(), "test2");
	    assertEquals(companyClient1.getEmail(), "dinuka@gmail.com");
	    assertEquals(companyClient1.getCompany().getId().toString(), "2");
	    assertEquals(companyClient1.getCompany().getName(), "Sysensor IT Solutions");
	    assertEquals(companyClient1.getCompany().getUser_name(), "sysensor");
	    assertEquals(companyClient1.getCompany().getPass_word(), "test2");
	    assertEquals(companyClient1.getCompany().getEmail(), "sysensor@gmail.com");
	    assertEquals(companyClient1.getCompany().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderCompanyClient" })
    public void testJsonDecoderCompanySoftware() {
	log.info("Test JSON Decoder for CompanyClient");
	String encodedObj = "{\"id\":\"1\",\"name\":\"Application 1\",\"description\":\"Small accounting system\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}";

	CompanySW companyClient = null;
	try {
	    companyClient = (CompanySW) decoder.decodeObject(ObjectType.SOFTWARE, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(companyClient);
	assertEquals(companyClient.getId().toString(), "1");
	assertEquals(companyClient.getName(), "Application 1");
	assertEquals(companyClient.getDescription(), "Small accounting system");
	assertEquals(companyClient.getCompany().getId().toString(), "2");
	assertEquals(companyClient.getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyClient.getCompany().getUser_name(), "sysensor");
	assertEquals(companyClient.getCompany().getPass_word(), "test2");
	assertEquals(companyClient.getCompany().getEmail(), "sysensor@gmail.com");
	assertEquals(companyClient.getCompany().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"1\",\"name\":\"Application 1\",\"description\":\"Small accounting system\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}},"
		+ "{\"id\":\"1\",\"name\":\"Application 1\",\"description\":\"Small accounting system\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}]";

	List<CompanySW> companyList = null;
	try {
	    companyList = (List<CompanySW>) decoder.decodeObjectList(ObjectType.SOFTWARE, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (CompanySW companyClient1 : companyList) {
	    assertNotNull(companyClient1);
	    assertEquals(companyClient1.getName(), "Application 1");
	    assertEquals(companyClient1.getDescription(), "Small accounting system");
	    assertEquals(companyClient1.getCompany().getId().toString(), "2");
	    assertEquals(companyClient1.getCompany().getName(), "Sysensor IT Solutions");
	    assertEquals(companyClient1.getCompany().getUser_name(), "sysensor");
	    assertEquals(companyClient1.getCompany().getPass_word(), "test2");
	    assertEquals(companyClient1.getCompany().getEmail(), "sysensor@gmail.com");
	    assertEquals(companyClient1.getCompany().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderCompanySoftware" })
    public void testJsonDecoderCompanySoftwareCopy() {
	log.info("Test JSON Decoder for CompanyClient");
	String expDate = Formatters.formatDate(new Date());
	String encodedObj = "{\"id\":\"1\",\"mother_board\":\"M_BOARD_ID\",\"hd\":\"HD_ID\",\"mac\":\"MAC_ID\",\"expire_date\":\""
		+ expDate
		+ "\",\"company_sw\":{\"id\":\"2\",\"name\":\"Application 1\",\"description\":\"Small accounting system\"},"
		+ "\"company_client\":{\"id\":\"3\",\"name\":\"Client Dinuka\",\"user_name\":\"client1\",\"pass_word\":\"test2\",\"email\":\"client@gmail.com\"}}";

	CompanySWCopy companySWCopy = null;
	try {
	    companySWCopy = (CompanySWCopy) decoder.decodeObject(ObjectType.SOFTWARE_COPY, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(companySWCopy);
	assertEquals(companySWCopy.getId().toString(), "1");
	assertEquals(companySWCopy.getMother_board(), "M_BOARD_ID");
	assertEquals(companySWCopy.getHd(), "HD_ID");
	assertEquals(companySWCopy.getMac(), "MAC_ID");
	assertEquals(companySWCopy.getExpire_date(), expDate);
	assertEquals(companySWCopy.getCompany_sw().getId().toString(), "2");
	assertEquals(companySWCopy.getCompany_sw().getName(), "Application 1");
	assertEquals(companySWCopy.getCompany_sw().getDescription(), "Small accounting system");
	assertEquals(companySWCopy.getCompany_client().getId().toString(), "3");
	assertEquals(companySWCopy.getCompany_client().getName(), "Client Dinuka");
	assertEquals(companySWCopy.getCompany_client().getUser_name(), "client1");
	assertEquals(companySWCopy.getCompany_client().getPass_word(), "test2");
	assertEquals(companySWCopy.getCompany_client().getEmail(), "client@gmail.com");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"1\",\"mother_board\":\"M_BOARD_ID\",\"hd\":\"HD_ID\",\"mac\":\"MAC_ID\",\"expire_date\":\""
		+ expDate
		+ "\",\"company_sw\":{\"id\":\"2\",\"name\":\"Application 1\",\"description\":\"Small accounting system\"},"
		+ "\"company_client\":{\"id\":\"3\",\"name\":\"Client Dinuka\",\"user_name\":\"client1\",\"pass_word\":\"test2\",\"email\":\"client@gmail.com\"}},"
		+ "{\"id\":\"1\",\"mother_board\":\"M_BOARD_ID\",\"hd\":\"HD_ID\",\"mac\":\"MAC_ID\",\"expire_date\":\""
		+ expDate
		+ "\",\"company_sw\":{\"id\":\"2\",\"name\":\"Application 1\",\"description\":\"Small accounting system\"},"
		+ "\"company_client\":{\"id\":\"3\",\"name\":\"Client Dinuka\",\"user_name\":\"client1\",\"pass_word\":\"test2\",\"email\":\"client@gmail.com\"}}]";

	List<CompanySWCopy> companyList = null;
	try {
	    companyList = (List<CompanySWCopy>) decoder.decodeObjectList(ObjectType.SOFTWARE_COPY, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (CompanySWCopy companySWCopy1 : companyList) {
	    assertNotNull(companySWCopy1);
	    assertEquals(companySWCopy1.getId().toString(), "1");
	    assertEquals(companySWCopy1.getMother_board(), "M_BOARD_ID");
	    assertEquals(companySWCopy1.getHd(), "HD_ID");
	    assertEquals(companySWCopy1.getMac(), "MAC_ID");
	    assertEquals(companySWCopy1.getExpire_date(), expDate);
	    assertEquals(companySWCopy1.getCompany_sw().getId().toString(), "2");
	    assertEquals(companySWCopy1.getCompany_sw().getName(), "Application 1");
	    assertEquals(companySWCopy1.getCompany_sw().getDescription(), "Small accounting system");
	    assertEquals(companySWCopy1.getCompany_client().getId().toString(), "3");
	    assertEquals(companySWCopy1.getCompany_client().getName(), "Client Dinuka");
	    assertEquals(companySWCopy1.getCompany_client().getUser_name(), "client1");
	    assertEquals(companySWCopy1.getCompany_client().getPass_word(), "test2");
	    assertEquals(companySWCopy1.getCompany_client().getEmail(), "client@gmail.com");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderCompanySoftwareCopy" })
    public void testJsonDecoderAdminScope() {
	log.info("Test JSON Decoder for Admin Scope");
	String encodedObj = "{\"id\":\"2\",\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"admin\":{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}}";

	Admin admin = null;
	AdminScope adminScope = null;
	try {
	    adminScope = (AdminScope) decoder.decodeObject(ObjectType.ADMIN_SCOPE, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(adminScope);
	assertEquals(adminScope.getId().toString(), "2");
	assertEquals(adminScope.getApi_name(), "admin");
	assertEquals(adminScope.isGet(), true);
	assertEquals(adminScope.isPost(), true);
	assertEquals(adminScope.isPut(), true);
	assertEquals(adminScope.isDel(), true);
	assertEquals(adminScope.getAdmin().getId().toString(), "1");
	assertEquals(adminScope.getAdmin().getName(), "super_dinuka");
	assertEquals(adminScope.getAdmin().getUser_name(), "dinuka");
	assertEquals(adminScope.getAdmin().getPass_word(), "test1");
	assertEquals(adminScope.getAdmin().getEmail(), "dinuka@gmail.com");
	assertEquals(adminScope.getAdmin().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"2\",\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"admin\":{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}},"
		+ "{\"id\":\"2\",\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"admin\":{\"id\":\"1\",\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"pass_word\":\"test1\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}}]";

	List<AdminScope> adminList = null;
	try {
	    adminList = (List<AdminScope>) decoder.decodeObjectList(ObjectType.ADMIN_SCOPE, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (AdminScope adminScope1 : adminList) {
	    assertNotNull(adminScope1);
	    assertEquals(adminScope1.getId().toString(), "2");
	    assertEquals(adminScope1.getApi_name(), "admin");
	    assertEquals(adminScope1.isGet(), true);
	    assertEquals(adminScope1.isPost(), true);
	    assertEquals(adminScope1.isPut(), true);
	    assertEquals(adminScope1.isDel(), true);
	    assertEquals(adminScope1.getAdmin().getId().toString(), "1");
	    assertEquals(adminScope1.getAdmin().getName(), "super_dinuka");
	    assertEquals(adminScope1.getAdmin().getUser_name(), "dinuka");
	    assertEquals(adminScope1.getAdmin().getPass_word(), "test1");
	    assertEquals(adminScope1.getAdmin().getEmail(), "dinuka@gmail.com");
	    assertEquals(adminScope1.getAdmin().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderAdminScope" })
    public void testJsonDecoderCompanyUserScope() {
	log.info("Test JSON Decoder for CompanyUserScope");
	String encodedObj = "{\"id\":\"2\",\"api_name\":\"software\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"companyUser\":{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}}";

	CompanyUserScope companyUserScope = null;
	try {
	    companyUserScope = (CompanyUserScope) decoder.decodeObject(ObjectType.COMPANY_USER_SCOPE, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(companyUserScope);
	assertEquals(companyUserScope.getId().toString(), "2");
	assertEquals(companyUserScope.getApi_name(), "software");
	assertEquals(companyUserScope.isGet(), true);
	assertEquals(companyUserScope.isPost(), true);
	assertEquals(companyUserScope.isPut(), true);
	assertEquals(companyUserScope.isDel(), true);
	assertEquals(companyUserScope.getCompanyUser().getId().toString(), "1");
	assertEquals(companyUserScope.getCompanyUser().getName(), "Dinuka Malalanayake");
	assertEquals(companyUserScope.getCompanyUser().getUser_name(), "dinuka");
	assertEquals(companyUserScope.getCompanyUser().getPass_word(), "test2");
	assertEquals(companyUserScope.getCompanyUser().getEmail(), "dinuka@gmail.com");
	assertEquals(companyUserScope.getCompanyUser().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	assertEquals(companyUserScope.getCompanyUser().getCompany().getId().toString(), "2");
	assertEquals(companyUserScope.getCompanyUser().getCompany().getName(), "Sysensor IT Solutions");
	assertEquals(companyUserScope.getCompanyUser().getCompany().getUser_name(), "sysensor");
	assertEquals(companyUserScope.getCompanyUser().getCompany().getPass_word(), "test2");
	assertEquals(companyUserScope.getCompanyUser().getCompany().getEmail(), "sysensor@gmail.com");
	assertEquals(companyUserScope.getCompanyUser().getCompany().getApi_key(),
		"75e70eb1-7940-4234-b6c4-2870ab0bb794");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"id\":\"2\",\"api_name\":\"software\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"companyUser\":{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}},"
		+ "{\"id\":\"2\",\"api_name\":\"software\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
		+ "\"companyUser\":{\"id\":\"1\",\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
		+ "\"company\":{\"id\":\"2\",\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"pass_word\":\"test2\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}}]";

	List<CompanyUserScope> companyList = null;
	try {
	    companyList = (List<CompanyUserScope>) decoder.decodeObjectList(ObjectType.COMPANY_USER_SCOPE,
		    encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (CompanyUserScope companyUserScope1 : companyList) {
	    assertNotNull(companyUserScope1);
	    assertEquals(companyUserScope1.getId().toString(), "2");
	    assertEquals(companyUserScope1.getApi_name(), "software");
	    assertEquals(companyUserScope1.isGet(), true);
	    assertEquals(companyUserScope1.isPost(), true);
	    assertEquals(companyUserScope1.isPut(), true);
	    assertEquals(companyUserScope1.isDel(), true);
	    assertEquals(companyUserScope1.getCompanyUser().getId().toString(), "1");
	    assertEquals(companyUserScope1.getCompanyUser().getName(), "Dinuka Malalanayake");
	    assertEquals(companyUserScope1.getCompanyUser().getUser_name(), "dinuka");
	    assertEquals(companyUserScope1.getCompanyUser().getPass_word(), "test2");
	    assertEquals(companyUserScope1.getCompanyUser().getEmail(), "dinuka@gmail.com");
	    assertEquals(companyUserScope1.getCompanyUser().getApi_key(), "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	    assertEquals(companyUserScope1.getCompanyUser().getCompany().getId().toString(), "2");
	    assertEquals(companyUserScope1.getCompanyUser().getCompany().getName(), "Sysensor IT Solutions");
	    assertEquals(companyUserScope1.getCompanyUser().getCompany().getUser_name(), "sysensor");
	    assertEquals(companyUserScope1.getCompanyUser().getCompany().getPass_word(), "test2");
	    assertEquals(companyUserScope1.getCompanyUser().getCompany().getEmail(), "sysensor@gmail.com");
	    assertEquals(companyUserScope1.getCompanyUser().getCompany().getApi_key(),
		    "75e70eb1-7940-4234-b6c4-2870ab0bb794");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderCompanyUserScope" })
    public void testJsonDecoderTrace() {
	log.info("Test JSON Decoder for Trace");
	String date = Formatters.formatDate(new Date());
	String encodedObj = "{\"operation\":\"PUT\",\"api_name\":\"admin\",\"type_user_name\":\"dinuka\",\"type\":\"SUPER_ADMIN\",\"date_time\":\""
		+ date
		+ "\",\"affected_type\":\"ADMIN\",\"affected_user_name\":\"malinda\",\"before_data\":\"Admin operation GET,POST\",\"after_data\":\"Admin operation GET,POST,DELETE,PUT\"}";

	Trace trace = null;
	try {
	    trace = (Trace) decoder.decodeObject(ObjectType.TRACE, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(trace);
	assertEquals(trace.getOperation().toString(), "PUT");
	assertEquals(trace.getApi_name(), "admin");
	assertEquals(trace.getType_user_name(), "dinuka");
	assertEquals(trace.getType().toString(), "SUPER_ADMIN");
	assertEquals(trace.getDate_time(), date);
	assertEquals(trace.getAffected_type().toString(), "ADMIN");
	assertEquals(trace.getAffected_user_name(), "malinda");
	assertEquals(trace.getBefore_data(), "Admin operation GET,POST");
	assertEquals(trace.getAfter_data(), "Admin operation GET,POST,DELETE,PUT");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"operation\":\"PUT\",\"api_name\":\"admin\",\"type_user_name\":\"dinuka\",\"type\":\"SUPER_ADMIN\",\"date_time\":\""
		+ date
		+ "\",\"affected_type\":\"ADMIN\",\"affected_user_name\":\"malinda\",\"before_data\":\"Admin operation GET,POST\",\"after_data\":\"Admin operation GET,POST,DELETE,PUT\"},"
		+ "{\"operation\":\"PUT\",\"api_name\":\"admin\",\"type_user_name\":\"dinuka\",\"type\":\"SUPER_ADMIN\",\"date_time\":\""
		+ date
		+ "\",\"affected_type\":\"ADMIN\",\"affected_user_name\":\"malinda\",\"before_data\":\"Admin operation GET,POST\",\"after_data\":\"Admin operation GET,POST,DELETE,PUT\"}]";

	List<Trace> companyList = null;
	try {
	    companyList = (List<Trace>) decoder.decodeObjectList(ObjectType.TRACE, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (Trace trace1 : companyList) {
	    assertNotNull(trace1);
	    assertEquals(trace1.getOperation().toString(), "PUT");
	    assertEquals(trace1.getApi_name(), "admin");
	    assertEquals(trace1.getType_user_name(), "dinuka");
	    assertEquals(trace1.getType().toString(), "SUPER_ADMIN");
	    assertEquals(trace1.getDate_time(), date);
	    assertEquals(trace1.getAffected_type().toString(), "ADMIN");
	    assertEquals(trace1.getAffected_user_name(), "malinda");
	    assertEquals(trace1.getBefore_data(), "Admin operation GET,POST");
	    assertEquals(trace1.getAfter_data(), "Admin operation GET,POST,DELETE,PUT");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonDecoderTrace" })
    public void testJsonDecoderUsage() {
	log.info("Test JSON Decoder for UsageData");
	String date = Formatters.formatDate(new Date());
	String encodedObj = "{\"operation\":\"GET\",\"api_name\":\"admin\",\"type_id\":1,\"type\":\"ADMIN\",\"date_time\":\""
		+ date + "\",\"last_modified\":\"" + date + "\",\"access_count\":\"100\",\"decline_count\":\"0\"}";

	UsageData usage = null;
	try {
	    usage = (UsageData) decoder.decodeObject(ObjectType.USAGE, encodedObj);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	assertNotNull(usage);
	assertEquals(usage.getOperation().toString(), "GET");
	assertEquals(usage.getApi_name(), "admin");
	assertEquals(usage.getType_id().toString(), "1");
	assertEquals(usage.getType().toString(), "ADMIN");
	assertEquals(usage.getDate_time(), date);
	assertEquals(usage.getLast_modified(), date);
	assertEquals(usage.getAccess_count(), "100");
	assertEquals(usage.getDecline_count(), "0");

	// check the DecodingException exception
	String exceptionClass = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObj + "{");
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, DecodingException.class.toString());

	String encodedObjList = "[{\"operation\":\"GET\",\"api_name\":\"admin\",\"type_id\":1,\"type\":\"ADMIN\",\"date_time\":\""
		+ date
		+ "\",\"last_modified\":\""
		+ date
		+ "\",\"access_count\":\"100\",\"decline_count\":\"0\"},"
		+ "{\"operation\":\"GET\",\"api_name\":\"admin\",\"type_id\":1,\"type\":\"ADMIN\",\"date_time\":\""
		+ date + "\",\"last_modified\":\"" + date + "\",\"access_count\":\"100\",\"decline_count\":\"0\"}]";

	List<UsageData> companyList = null;
	try {
	    companyList = (List<UsageData>) decoder.decodeObjectList(ObjectType.USAGE, encodedObjList);
	} catch (DecodingException e) {
	    e.printStackTrace();
	}

	for (UsageData usage2 : companyList) {
	    assertNotNull(usage2);
	    assertEquals(usage2.getOperation().toString(), "GET");
	    assertEquals(usage2.getApi_name(), "admin");
	    assertEquals(usage2.getType_id().toString(), "1");
	    assertEquals(usage2.getType().toString(), "ADMIN");
	    assertEquals(usage2.getDate_time(), date);
	    assertEquals(usage2.getLast_modified(), date);
	    assertEquals(usage2.getAccess_count(), "100");
	    assertEquals(usage2.getDecline_count(), "0");
	}

	// check the DecodingException exception
	String exceptionClassList = "";
	try {
	    decoder.decodeObject(ObjectType.SUPER_ADMIN, encodedObjList + "{");
	} catch (Exception ex) {
	    exceptionClassList = ex.getClass().toString();
	}
	assertEquals(exceptionClassList, DecodingException.class.toString());
    }
}

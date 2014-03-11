package com.sw.protection.backend.encoder.impl;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.dao.impl.UsageDAOImplTest;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;
import com.sw.protection.backend.entity.SuperAdmin;

//@Test(groups = { "EncoderFactoryImplTest" }, dependsOnGroups = { "UsageDAOImplTest" })
public class EncoderFactoryImplTest {
    public static final Logger log = Logger.getLogger(UsageDAOImplTest.class.getName());
    EncoderFactory encoderFactory;
    Encoder encoder;

    @BeforeClass
    public void initialize() {
	encoderFactory = new EncoderFactoryImpl();
	encoder = encoderFactory.getEncoder(EncoderDecoderType.JSON);
    }

    @Test
    public void testJsonEncoderforSuperAdmin() {
	log.info("Test JSON Encoder for Super Admin");

	SuperAdmin superAdmin1 = new SuperAdmin();
	superAdmin1.setName("super_dinuka");
	superAdmin1.setUser_name("dinuka");
	superAdmin1.setPass_word("test1");
	superAdmin1.setEmail("dinuka@gmail.com");
	superAdmin1.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SUPER_ADMIN, superAdmin1);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedString,
		"{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.ADMIN, superAdmin1);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	SuperAdmin superAdmin2 = new SuperAdmin();
	superAdmin2.setName("super_dinuka");
	superAdmin2.setUser_name("dinuka");
	superAdmin2.setPass_word("test1");
	superAdmin2.setEmail("dinuka@gmail.com");
	superAdmin2.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	List<SuperAdmin> list = new ArrayList<SuperAdmin>();
	list.add(superAdmin1);
	list.add(superAdmin2);
	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"},"
			+ "{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforSuperAdmin" })
    public void testJsonEncoderforAdmin() {
	log.info("Test JSON Encoder for Admin");

	Admin admin = new Admin();
	admin.setName("super_dinuka");
	admin.setUser_name("dinuka");
	admin.setPass_word("test1");
	admin.setEmail("dinuka@gmail.com");
	admin.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedString,
		"{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, admin);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	Admin admin2 = new Admin();
	admin2.setName("super_dinuka");
	admin2.setUser_name("dinuka");
	admin2.setPass_word("test1");
	admin2.setEmail("dinuka@gmail.com");
	admin2.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	List<Admin> list = new ArrayList<Admin>();
	list.add(admin);
	list.add(admin2);

	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.ADMIN, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"},"
			+ "{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforAdmin" })
    public void testJsonEncoderforCompany() {
	log.info("Test JSON Encoder for Company");
	Company company = new Company();
	company.setName("Sysensor IT");
	company.setPass_word("test2");
	company.setEmail("sysensorit@gmail.com");
	company.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
	} catch (Exception ex) {

	}
	assertEquals(encodedString,
		"{\"name\":\"Sysensor IT\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensorit@gmail.com\"}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, company);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	Company company2 = new Company();
	company2.setName("Sysensor IT");
	company2.setPass_word("test2");
	company2.setEmail("sysensorit@gmail.com");
	company2.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	List<Company> list = new ArrayList<Company>();
	list.add(company);
	list.add(company2);

	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.COMPANY, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"name\":\"Sysensor IT\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensorit@gmail.com\"},"
			+ "{\"name\":\"Sysensor IT\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensorit@gmail.com\"}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforCompany" })
    public void testJsonEncoderforCompanyUser() {
	log.info("Test JSON Encoder for Company User");
	Company company = new Company();

	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	CompanyUser companyUser = new CompanyUser();
	companyUser.setUser_name("dinuka");
	companyUser.setName("Dinuka Malalanayake");
	companyUser.setPass_word("test1");
	companyUser.setEmail("dinuka@gmail.com");
	companyUser.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");
	companyUser.setCompany(company);

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
	} catch (Exception ex) {

	}

	assertEquals(
		encodedString,
		"{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, companyUser);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	CompanyUser companyUser2 = new CompanyUser();
	companyUser2.setUser_name("dinuka");
	companyUser2.setName("Dinuka Malalanayake");
	companyUser2.setPass_word("test1");
	companyUser2.setEmail("dinuka@gmail.com");
	companyUser2.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");
	companyUser2.setCompany(company);

	List<CompanyUser> list = new ArrayList<CompanyUser>();
	list.add(companyUser);
	list.add(companyUser2);
	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.COMPANY_USER, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}},"
			+ "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());

	// check the class cast exception
	String exceptionEncodingClass = "";
	companyUser.setCompany(null);
	try {
	    encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
	} catch (Exception ex) {
	    exceptionEncodingClass = ex.getClass().toString();
	}
	assertEquals(exceptionEncodingClass, EncodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforCompanyUser" })
    public void testJsonEncoderforCompanyClient() {
	log.info("Test JSON Encoder for Company Client");
	Company company = new Company();

	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	CompanyClient companyClient = new CompanyClient();
	companyClient.setUser_name("dinuka");
	companyClient.setName("Dinuka Malalanayake");
	companyClient.setPass_word("test1");
	companyClient.setEmail("dinuka@gmail.com");
	companyClient.setCompany(company);

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
	} catch (Exception ex) {

	}

	assertEquals(
		encodedString,
		"{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"email\":\"dinuka@gmail.com\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, companyClient);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	CompanyClient companyClient2 = new CompanyClient();
	companyClient2.setUser_name("dinuka");
	companyClient2.setName("Dinuka Malalanayake");
	companyClient2.setPass_word("test1");
	companyClient2.setEmail("dinuka@gmail.com");
	companyClient2.setCompany(company);

	List<CompanyClient> list = new ArrayList<CompanyClient>();
	list.add(companyClient);
	list.add(companyClient2);
	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.CLIENT, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"email\":\"dinuka@gmail.com\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}},"
			+ "{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"email\":\"dinuka@gmail.com\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());

	// check the class cast exception
	String exceptionEncodingClass = "";
	companyClient.setCompany(null);
	try {
	    encoder.encodeObject(ObjectType.CLIENT, companyClient);
	} catch (Exception ex) {
	    exceptionEncodingClass = ex.getClass().toString();
	}
	assertEquals(exceptionEncodingClass, EncodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforCompanyClient" })
    public void testJsonEncoderforCompanySoftware() {
	log.info("Test JSON Encoder for Company Software");
	Company company = new Company();

	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	CompanySW companySW = new CompanySW();
	companySW.setName("Application 1");
	companySW.setDescription("Small accounting system");
	companySW.setCompany(company);

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySW);
	} catch (Exception ex) {

	}

	assertEquals(
		encodedString,
		"{\"name\":\"Application 1\",\"description\":\"Small accounting system\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, companySW);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	CompanySW companySW2 = new CompanySW();
	companySW2.setName("Application 1");
	companySW2.setDescription("Small accounting system");
	companySW2.setCompany(company);

	List<CompanySW> list = new ArrayList<CompanySW>();
	list.add(companySW);
	list.add(companySW2);
	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.SOFTWARE, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"name\":\"Application 1\",\"description\":\"Small accounting system\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}},"
			+ "{\"name\":\"Application 1\",\"description\":\"Small accounting system\","
			+ "\"company\":{\"name\":\"Sysensor IT Solutions\",\"user_name\":\"sysensor\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"sysensor@gmail.com\"}}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());

	// check the class cast exception
	String exceptionEncodingClass = "";
	companySW.setCompany(null);
	try {
	    encoder.encodeObject(ObjectType.SOFTWARE, companySW);
	} catch (Exception ex) {
	    exceptionEncodingClass = ex.getClass().toString();
	}
	assertEquals(exceptionEncodingClass, EncodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforCompanySoftware" })
    public void testJsonEncoderforCompanySoftwareCopy() {
	log.info("Test JSON Encoder for Company Software Copy");
	Company company = new Company();

	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

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

	String expDate = Formatters.formatDate(new Date());
	CompanySWCopy companySWCopy = new CompanySWCopy();
	companySWCopy.setCompany_client(companyClient);
	companySWCopy.setCompany_sw(companySW);
	companySWCopy.setExpire_date(expDate);
	companySWCopy.setMother_board("M_BOARD_ID");
	companySWCopy.setHd("HD_ID");
	companySWCopy.setMac("MAC_ID");

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
	} catch (Exception ex) {

	}

	assertEquals(
		encodedString,
		"{\"mother_board\":\"M_BOARD_ID\",\"hd\":\"HD_ID\",\"mac\":\"MAC_ID\",\"expire_date\":\""
			+ expDate
			+ "\",\"company_sw\":{\"name\":\"Application 1\",\"description\":\"Small accounting system\"},"
			+ "\"company_client\":{\"name\":\"Client Dinuka\",\"user_name\":\"client1\",\"email\":\"client@gmail.com\"}}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, companySW);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	CompanySWCopy companySWCopy2 = new CompanySWCopy();
	companySWCopy2.setCompany_client(companyClient);
	companySWCopy2.setCompany_sw(companySW);
	companySWCopy2.setExpire_date(expDate);
	companySWCopy2.setMother_board("M_BOARD_ID");
	companySWCopy2.setHd("HD_ID");
	companySWCopy2.setMac("MAC_ID");

	List<CompanySWCopy> list = new ArrayList<CompanySWCopy>();
	list.add(companySWCopy);
	list.add(companySWCopy2);
	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.SOFTWARE_COPY, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"mother_board\":\"M_BOARD_ID\",\"hd\":\"HD_ID\",\"mac\":\"MAC_ID\",\"expire_date\":\""
			+ expDate
			+ "\",\"company_sw\":{\"name\":\"Application 1\",\"description\":\"Small accounting system\"},"
			+ "\"company_client\":{\"name\":\"Client Dinuka\",\"user_name\":\"client1\",\"email\":\"client@gmail.com\"}},"
			+ "{\"mother_board\":\"M_BOARD_ID\",\"hd\":\"HD_ID\",\"mac\":\"MAC_ID\",\"expire_date\":\""
			+ expDate
			+ "\",\"company_sw\":{\"name\":\"Application 1\",\"description\":\"Small accounting system\"},"
			+ "\"company_client\":{\"name\":\"Client Dinuka\",\"user_name\":\"client1\",\"email\":\"client@gmail.com\"}}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());

	// check the class cast exception
	String exceptionEncodingClass = "";
	companySWCopy.setCompany_client(null);
	try {
	    encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
	} catch (Exception ex) {
	    exceptionEncodingClass = ex.getClass().toString();
	}
	assertEquals(exceptionEncodingClass, EncodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforCompanySoftwareCopy" })
    public void testJsonEncoderforAdminScope() {
	log.info("Test JSON Encoder for Admin Scope");

	Admin admin = new Admin();
	admin.setName("super_dinuka");
	admin.setUser_name("dinuka");
	admin.setPass_word("test1");
	admin.setEmail("dinuka@gmail.com");
	admin.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	AdminScope adminScope1 = new AdminScope();
	adminScope1.setAdmin(admin);
	adminScope1.setApi_name(APINames.ADMIN);
	adminScope1.setDel(true);
	adminScope1.setGet(true);
	adminScope1.setPost(true);
	adminScope1.setPut(true);

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.ADMIN_SCOPE, adminScope1);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedString,
		"{\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
			+ "\"admin\":{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, adminScope1);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	AdminScope adminScope2 = new AdminScope();
	adminScope2.setAdmin(admin);
	adminScope2.setApi_name(APINames.ADMIN);
	adminScope2.setDel(true);
	adminScope2.setGet(true);
	adminScope2.setPost(true);
	adminScope2.setPut(true);

	List<AdminScope> list = new ArrayList<AdminScope>();
	list.add(adminScope1);
	list.add(adminScope2);

	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.ADMIN_SCOPE, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
			+ "\"admin\":{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}},"
			+ "{\"api_name\":\"admin\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
			+ "\"admin\":{\"name\":\"super_dinuka\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.SUPER_ADMIN, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());

	String listEncodingException = "";
	adminScope1.setAdmin(null);
	try {
	    encoder.encodeObject(ObjectType.ADMIN_SCOPE, adminScope1);
	} catch (Exception ex) {
	    listEncodingException = ex.getClass().toString();
	}
	assertEquals(listEncodingException, EncodingException.class.toString());
    }

    @Test(dependsOnMethods = { "testJsonEncoderforAdminScope" })
    public void testJsonEncoderforUserScope() {
	log.info("Test JSON Encoder for User Scope");

	Company company = new Company();
	company.setName("Sysensor IT Solutions");
	company.setUser_name("sysensor");
	company.setPass_word("test1");
	company.setEmail("sysensor@gmail.com");
	company.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");

	CompanyUser companyUser = new CompanyUser();
	companyUser.setUser_name("dinuka");
	companyUser.setName("Dinuka Malalanayake");
	companyUser.setPass_word("test1");
	companyUser.setEmail("dinuka@gmail.com");
	companyUser.setApi_key("75e70eb1-7940-4234-b6c4-2870ab0bb794");
	companyUser.setCompany(company);

	CompanyUserScope companyUserScope = new CompanyUserScope();
	companyUserScope.setApi_name(APINames.SOFTWARE);
	companyUserScope.setDel(true);
	companyUserScope.setGet(true);
	companyUserScope.setPost(true);
	companyUserScope.setPut(true);
	companyUserScope.setCompanyUser(companyUser);

	String encodedString = "";
	try {
	    encodedString = encoder.encodeObject(ObjectType.COMPANY_USER_SCOPE, companyUserScope);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedString,
		"{\"api_name\":\"software\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
			+ "\"companyUser\":{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}}");

	// check the class cast exception
	String exceptionClass = "";
	try {
	    encoder.encodeObject(ObjectType.SUPER_ADMIN, companyUserScope);
	} catch (Exception ex) {
	    exceptionClass = ex.getClass().toString();
	}
	assertEquals(exceptionClass, ClassCastException.class.toString());

	CompanyUserScope companyUserScope2 = new CompanyUserScope();
	companyUserScope2.setApi_name(APINames.COMPANY);
	companyUserScope2.setDel(true);
	companyUserScope2.setGet(true);
	companyUserScope2.setPost(true);
	companyUserScope2.setPut(true);
	companyUserScope2.setCompanyUser(companyUser);

	List<CompanyUserScope> list = new ArrayList<CompanyUserScope>();
	list.add(companyUserScope);
	list.add(companyUserScope2);

	String encodedListString = "";
	try {
	    encodedListString = encoder.encodeObjectList(ObjectType.COMPANY_USER_SCOPE, list);
	} catch (Exception ex) {

	}
	assertEquals(
		encodedListString,
		"[{\"api_name\":\"software\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
			+ "\"companyUser\":{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}},"
			+ "{\"api_name\":\"company\",\"get\":true,\"post\":true,\"put\":true,\"del\":true,"
			+ "\"companyUser\":{\"name\":\"Dinuka Malalanayake\",\"user_name\":\"dinuka\",\"api_key\":\"75e70eb1-7940-4234-b6c4-2870ab0bb794\",\"email\":\"dinuka@gmail.com\"}}]");

	String listClassCastException = "";
	try {
	    encoder.encodeObjectList(ObjectType.COMPANY, list);
	} catch (Exception ex) {
	    listClassCastException = ex.getClass().toString();
	}
	assertEquals(listClassCastException, ClassCastException.class.toString());

	String listEncodingException = "";
	companyUserScope.setCompanyUser(null);
	try {
	    encoder.encodeObject(ObjectType.COMPANY_USER_SCOPE, companyUserScope);
	} catch (Exception ex) {
	    listEncodingException = ex.getClass().toString();
	}
	assertEquals(listEncodingException, EncodingException.class.toString());
    }
}

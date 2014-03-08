package com.sw.protection.backend.encoder.impl;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.dao.impl.UsageDAOImplTest;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyUser;
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

    @Test
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

    @Test
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

    @Test
    public void testJsonEncoderforCompanyUser() {
	log.info("Test JSON Encoder for Company");
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
	System.out.println(encodedString);
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
    }
}

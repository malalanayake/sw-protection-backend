package com.sw.protection.backend.encoder.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.SuperAdmin;

/**
 * Implementation of JSON Encoder which is used to encode the any type of Entity
 * classes provided by ObjectType Enum
 * 
 * @author dinuka
 * 
 */
public class JSONEncoder implements Encoder {
    private String encodedString = null;
    private String encodedListString = null;
    public static final Logger log = Logger.getLogger(JSONEncoder.class.getName());

    @Override
    public String encodeObject(ObjectType objectType, Object object) throws ClassCastException, EncodingException {
	switch (objectType) {
	case SUPER_ADMIN:
	    try {
		this.encodeSuperAdmin((SuperAdmin) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case ADMIN:
	    try {
		this.encodeAdmin((Admin) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case COMPANY:
	    try {
		this.encodeCompany((Company) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case COMPANY_USER:
	    try {
		this.encodeCompanyUser((CompanyUser) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case CLIENT:
	    break;
	case SOFTWARE:
	    break;
	case SOFTWARE_COPY:
	    break;
	case ADMIN_SCOPE:
	    break;
	case COMPANY_USER_SCOPE:
	    break;
	}

	return encodedString;
    }

    @Override
    public String encodeObjectList(ObjectType objectType, List<?> objectList) throws ClassCastException,
	    EncodingException {
	switch (objectType) {
	case SUPER_ADMIN:
	    try {
		this.encodeSuperAdminList((List<SuperAdmin>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case ADMIN:
	    try {
		this.encodeAdminList((List<Admin>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case COMPANY:
	    try {
		this.encodeCompanyList((List<Company>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case COMPANY_USER:
	    try {
		this.encodeCompanyUserList((List<CompanyUser>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		throw ex;
	    }
	    break;
	case CLIENT:
	    break;
	case SOFTWARE:
	    break;
	case SOFTWARE_COPY:
	    break;
	case ADMIN_SCOPE:
	    break;
	case COMPANY_USER_SCOPE:
	    break;
	}
	return encodedListString;
    }

    /**
     * Encoding the Super Admin data to JSON but remove the password
     * 
     * @param object
     */
    private void encodeSuperAdmin(SuperAdmin object) {
	Gson gson = new Gson();
	object.setPass_word(null);
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Super Admin list of objects to JSON but remove all password
     * fields
     * 
     * @param objectList
     */
    private void encodeSuperAdminList(List<SuperAdmin> objectList) {
	Gson gson = new Gson();
	for (SuperAdmin sAdmin : objectList) {
	    sAdmin.setPass_word(null);
	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Admin data to JSON but remove the password
     * 
     * @param object
     */
    private void encodeAdmin(Admin object) {
	Gson gson = new Gson();
	object.setPass_word(null);
	object.setAdminScopeSet(null);
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Admin list of objects to JSON but remove all password fields
     * 
     * @param objectList
     */
    private void encodeAdminList(List<Admin> objectList) {
	Gson gson = new Gson();
	for (Admin sAdmin : objectList) {
	    sAdmin.setPass_word(null);
	    sAdmin.setAdminScopeSet(null);
	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Company data to JSON but remove all passwords
     * 
     * @param object
     */
    private void encodeCompany(Company object) {
	Gson gson = new Gson();
	object.setPass_word(null);
	object.setCompanyClientSet(null);
	object.setCompanySWSet(null);
	object.setCompanyUserSet(null);
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Company list of objects to JSON but remove all password
     * fields
     * 
     * @param objectList
     */
    private void encodeCompanyList(List<Company> objectList) {
	Gson gson = new Gson();
	for (Company company : objectList) {
	    company.setPass_word(null);
	    company.setCompanyClientSet(null);
	    company.setCompanySWSet(null);
	    company.setCompanyUserSet(null);
	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Company User data to JSON but remove all passwords
     * 
     * @param object
     */
    private void encodeCompanyUser(CompanyUser object) throws EncodingException {
	Gson gson = new Gson();
	object.setPass_word(null);
	object.setUserScopeSet(null);
	if (object.getCompany() != null) {
	    object.getCompany().setPass_word(null);
	    object.getCompany().setCompanyClientSet(null);
	    object.getCompany().setCompanySWSet(null);
	    object.getCompany().setCompanyUserSet(null);
	}
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Company User list of objects to JSON but remove all password
     * fields
     * 
     * @param objectList
     */
    private void encodeCompanyUserList(List<CompanyUser> objectList) throws EncodingException {
	Gson gson = new Gson();
	for (CompanyUser companyUser : objectList) {
	    companyUser.setPass_word(null);
	    companyUser.setUserScopeSet(null);
	    if (companyUser.getCompany() != null) {
		companyUser.getCompany().setPass_word(null);
		companyUser.getCompany().setCompanyClientSet(null);
		companyUser.getCompany().setCompanySWSet(null);
		companyUser.getCompany().setCompanyUserSet(null);
	    }
	}
	encodedListString = gson.toJson(objectList);
    }

}

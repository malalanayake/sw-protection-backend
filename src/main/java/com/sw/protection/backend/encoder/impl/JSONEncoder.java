package com.sw.protection.backend.encoder.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;
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
	    try {
		this.encodeCompanyClient((CompanyClient) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case SOFTWARE:
	    try {
		this.encodeCompanySoftware((CompanySW) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case SOFTWARE_COPY:
	    try {
		this.encodeCompanySoftwareCopy((CompanySWCopy) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case ADMIN_SCOPE:
	    try {
		this.encodeAdminScope((AdminScope) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case COMPANY_USER_SCOPE:
	    try {
		this.encodeCompanyUserScope((CompanyUserScope) object);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
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
		log.error(ex);
		throw ex;
	    }
	    break;
	case CLIENT:
	    try {
		this.encodeCompanyClientList((List<CompanyClient>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case SOFTWARE:
	    try {
		this.encodeCompanySoftwareList((List<CompanySW>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case SOFTWARE_COPY:
	    try {
		this.encodeCompanySoftwareCopyList((List<CompanySWCopy>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case ADMIN_SCOPE:
	    try {
		this.encodeAdminScopeList((List<AdminScope>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
	    break;
	case COMPANY_USER_SCOPE:
	    try {
		this.encodeCompanyUserScopeList((List<CompanyUserScope>) objectList);
	    } catch (ClassCastException ex) {
		log.error(ex);
		throw ex;
	    } catch (EncodingException ex) {
		log.error(ex);
		throw ex;
	    }
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
     * Encoding the Company User data to JSON but remove all passwords and
     * secondary data
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
	} else {
	    throw new EncodingException(
		    "Company details are not presented, Company details is required to encode the company user object");
	}
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Company User list of objects to JSON but remove all password
     * fields and secondary data
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
	    } else {
		throw new EncodingException(
			"Company details are not presented, Company details is required to encode the company user object");
	    }
	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Company Client data to JSON but remove all passwords and
     * secondary data
     * 
     * @param object
     */
    private void encodeCompanyClient(CompanyClient object) throws EncodingException {
	Gson gson = new Gson();
	object.setPass_word(null);
	object.setCompanySWCopySet(null);
	if (object.getCompany() != null) {
	    object.getCompany().setPass_word(null);
	    object.getCompany().setCompanyClientSet(null);
	    object.getCompany().setCompanySWSet(null);
	    object.getCompany().setCompanyUserSet(null);
	} else {
	    throw new EncodingException(
		    "Company details are not presented, Company details is required to encode the company client object");
	}
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Company Client list of objects to JSON but remove all
     * password fields and secondary data
     * 
     * @param objectList
     */
    private void encodeCompanyClientList(List<CompanyClient> objectList) throws EncodingException {
	Gson gson = new Gson();
	for (CompanyClient companyUser : objectList) {
	    companyUser.setPass_word(null);
	    companyUser.setCompanySWCopySet(null);
	    if (companyUser.getCompany() != null) {
		companyUser.getCompany().setPass_word(null);
		companyUser.getCompany().setCompanyClientSet(null);
		companyUser.getCompany().setCompanySWSet(null);
		companyUser.getCompany().setCompanyUserSet(null);
	    } else {
		throw new EncodingException(
			"Company details are not presented, Company details is required to encode the company client object");
	    }
	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Company Software data to JSON but remove all passwords and
     * secondary data
     * 
     * @param object
     */
    private void encodeCompanySoftware(CompanySW object) throws EncodingException {
	Gson gson = new Gson();
	object.setCompanySWCopySet(null);
	if (object.getCompany() != null) {
	    object.getCompany().setPass_word(null);
	    object.getCompany().setCompanyClientSet(null);
	    object.getCompany().setCompanySWSet(null);
	    object.getCompany().setCompanyUserSet(null);
	} else {
	    throw new EncodingException(
		    "Company details are not presented, Company details is required to encode the company software object");
	}
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Company User list of objects to JSON but remove all password
     * fields and secondary data
     * 
     * @param objectList
     */
    private void encodeCompanySoftwareList(List<CompanySW> objectList) throws EncodingException {
	Gson gson = new Gson();
	for (CompanySW companySw : objectList) {
	    companySw.setCompanySWCopySet(null);
	    if (companySw.getCompany() != null) {
		companySw.getCompany().setPass_word(null);
		companySw.getCompany().setCompanyClientSet(null);
		companySw.getCompany().setCompanySWSet(null);
		companySw.getCompany().setCompanyUserSet(null);
	    } else {
		throw new EncodingException(
			"Company details are not presented, Company details is required to encode the company software object");
	    }
	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Company Software Copy data to JSON but remove all passwords
     * and secondary data
     * 
     * @param object
     */
    private void encodeCompanySoftwareCopy(CompanySWCopy object) throws EncodingException {
	Gson gson = new Gson();
	if (object.getCompany_client() != null) {
	    object.getCompany_client().setPass_word(null);
	    object.getCompany_client().setCompanySWCopySet(null);
	    object.getCompany_client().setCompany(null);
	} else {
	    throw new EncodingException(
		    "Company Client details are not presented, Company Client details is required to encode the company software copy object");
	}

	if (object.getCompany_sw() != null) {
	    object.getCompany_sw().setCompanySWCopySet(null);
	    object.getCompany_sw().setCompany(null);
	} else {
	    throw new EncodingException(
		    "Company Software details are not presented, Company Software details is required to encode the company software copy object");
	}

	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Company User list of objects to JSON but remove all password
     * fields and secondary data
     * 
     * @param objectList
     */
    private void encodeCompanySoftwareCopyList(List<CompanySWCopy> objectList) throws EncodingException {
	Gson gson = new Gson();
	for (CompanySWCopy companySwCopy : objectList) {
	    if (companySwCopy.getCompany_client() != null) {
		companySwCopy.getCompany_client().setPass_word(null);
		companySwCopy.getCompany_client().setCompanySWCopySet(null);
		companySwCopy.getCompany_client().setCompany(null);
	    } else {
		throw new EncodingException(
			"Company Client details are not presented, Company Client details is required to encode the company software copy object");
	    }

	    if (companySwCopy.getCompany_sw() != null) {
		companySwCopy.getCompany_sw().setCompanySWCopySet(null);
		companySwCopy.getCompany_sw().setCompany(null);
	    } else {
		throw new EncodingException(
			"Company Software details are not presented, Company Software details is required to encode the company software copy object");
	    }

	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Admin Scope data to JSON but remove the password
     * 
     * @param object
     */
    private void encodeAdminScope(AdminScope object) throws EncodingException {
	Gson gson = new Gson();
	if (object.getAdmin() != null) {
	    object.getAdmin().setPass_word(null);
	    object.getAdmin().setAdminScopeSet(null);
	} else {
	    throw new EncodingException(
		    "Admin details are not presented, Admin details is required to encode the admin scope object");

	}
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Admin Scope list of objects to JSON but remove all password
     * fields
     * 
     * @param objectList
     */
    private void encodeAdminScopeList(List<AdminScope> objectList) throws EncodingException {
	Gson gson = new Gson();
	for (AdminScope sAdminScope : objectList) {
	    if (sAdminScope.getAdmin() != null) {
		sAdminScope.getAdmin().setPass_word(null);
		sAdminScope.getAdmin().setAdminScopeSet(null);
	    } else {
		throw new EncodingException(
			"Admin details are not presented, Admin details is required to encode the admin scope object");

	    }
	}
	encodedListString = gson.toJson(objectList);
    }

    /**
     * Encoding the Admin Scope data to JSON but remove the password
     * 
     * @param object
     */
    private void encodeCompanyUserScope(CompanyUserScope object) throws EncodingException {
	Gson gson = new Gson();
	if (object.getCompanyUser() != null) {
	    object.getCompanyUser().setPass_word(null);
	    object.getCompanyUser().setUserScopeSet(null);
	    object.getCompanyUser().setCompany(null);
	} else {
	    throw new EncodingException(
		    "Company user details are not presented, Company user details is required to encode the user scope object");

	}
	encodedString = gson.toJson(object);
    }

    /**
     * Encoding the Admin Scope list of objects to JSON but remove all password
     * fields
     * 
     * @param objectList
     */
    private void encodeCompanyUserScopeList(List<CompanyUserScope> objectList) throws EncodingException {
	Gson gson = new Gson();
	for (CompanyUserScope companyUserScope : objectList) {
	    if (companyUserScope.getCompanyUser() != null) {
		companyUserScope.getCompanyUser().setPass_word(null);
		companyUserScope.getCompanyUser().setUserScopeSet(null);
		companyUserScope.getCompanyUser().setCompany(null);
	    } else {
		throw new EncodingException(
			"Company user details are not presented, Company user details is required to encode the user scope object");

	    }
	}
	encodedListString = gson.toJson(objectList);
    }

}

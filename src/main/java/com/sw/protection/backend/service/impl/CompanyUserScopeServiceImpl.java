package com.sw.protection.backend.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.dao.CompanyUserScopeDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.CompanyUserScope;
import com.sw.protection.backend.service.CompanyUserScopeService;

/**
 * CompanyUserScope service operation implementation.
 * 
 * @author dinuka
 * 
 */
@Service
public class CompanyUserScopeServiceImpl implements CompanyUserScopeService {
    public static final Logger log = Logger.getLogger(CompanyUserScopeServiceImpl.class.getName());

    @Override
    public String saveCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(CompanyUserScopeDAO.class);
	String encodedString = "";

	CompanyUserScope companyUserScope = (CompanyUserScope) decoder.decodeObject(ObjectType.COMPANY_USER_SCOPE,
		companyUserScopeString);
	if (companyUserScope != null) {
	    if (companyUserScopeDAO.validateCompanyUserScopeforSave(companyUserScope)) {
		companyUserScope = companyUserScopeDAO.saveNewCompanyUserScope(companyUserScope);
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER_SCOPE, companyUserScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given CompanyUserScope object doesn't contain expected data:");
		}
		throw new RequiredDataNotFoundException();
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given CompanyUserScope object is null");
	    }
	    throw new RequiredDataNotFoundException();
	}

	return encodedString;
    }

    @Override
    public String getCompanyUserScope(EncoderDecoderType encoderDecoderType, String userName, String api_name)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(CompanyUserScopeDAO.class);
	String encodedString = "";
	if (userName != "" && api_name != "") {
	    CompanyUserScope companyUserScope = companyUserScopeDAO.getCompanyUserScope(userName, api_name);
	    if (companyUserScope != null) {
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER_SCOPE, companyUserScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company User scopes is empty so return empty string as encoded user scope string");
		}
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("User name or Api name is empty string");
	    }
	    throw new RequiredDataNotFoundException("User name or Api name is empty");
	}

	return encodedString;
    }

    @Override
    public String getCompanyUserScopes(EncoderDecoderType encoderDecoderType, String userName)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(CompanyUserScopeDAO.class);
	String encodedString = "";
	if (userName != "") {
	    List<CompanyUserScope> companyUserScopes = companyUserScopeDAO.getAllCompanyUserScopes(userName);
	    if (companyUserScopes != null) {
		encodedString = encoder.encodeObjectList(ObjectType.COMPANY_USER_SCOPE, companyUserScopes);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company user scopes list is empty so return empty string as encoded user scope list string");
		}
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("User name is empty string");
	    }
	    throw new RequiredDataNotFoundException("Username is empty");
	}

	return encodedString;
    }

    @Override
    public String isAccessGrantedFor(String userName, String api_name, String api_opertaion)
	    throws RequiredDataNotFoundException {
	CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(CompanyUserScopeDAO.class);
	String encodedString = "";
	if (userName != "" && api_name != "" && api_opertaion != "") {
	    boolean apiOptEnumExist = false;
	    for (APIOperations a : APIOperations.values()) {
		if (a.toString().equals(api_opertaion)) {
		    apiOptEnumExist = true;
		    break;
		}
	    }

	    // check the api operation is match with the enum
	    if (apiOptEnumExist) {
		boolean result = companyUserScopeDAO.isAccessGrantedFor(userName, api_name,
			APIOperations.valueOf(api_opertaion));
		encodedString = String.valueOf(result);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Api operation is invalid:" + api_opertaion);
		}
		throw new RequiredDataNotFoundException("Api operation is invalid :" + api_opertaion);
	    }

	} else {
	    if (log.isDebugEnabled()) {
		log.debug("User name, Api name or Api operation is empty string");
	    }
	    throw new RequiredDataNotFoundException("User name, Api name or Api operation is empty string");
	}

	return encodedString;
    }

    @Override
    public String deleteCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(CompanyUserScopeDAO.class);
	String encodedString = "";

	CompanyUserScope companyUserScope = (CompanyUserScope) decoder.decodeObject(ObjectType.COMPANY_USER_SCOPE,
		companyUserScopeString);
	if (companyUserScope != null) {
	    if (companyUserScopeDAO.validateCompanyUserScopeforUpdateandDelete(companyUserScope)) {
		companyUserScope = companyUserScopeDAO.deleteCompanyUserScope(companyUserScope);
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER_SCOPE, companyUserScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given CompanyUserScope object doesn't contain expected data:");
		}
		throw new RequiredDataNotFoundException(
			"Cannot delete the CompanyUserScope - Given CompanyUserScope object doesn't contain expected data");
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given CompanyUserScope object is null");
	    }
	    throw new RequiredDataNotFoundException(
		    "Cannot delete the CompanyUserScope - Given CompanyUserScope object is null");
	}

	return encodedString;
    }

    @Override
    public String updateCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyUserScopeDAO companyUserScopeDAO = AppContext.getInstance().getBean(CompanyUserScopeDAO.class);
	String encodedString = "";

	CompanyUserScope companyUserScope = (CompanyUserScope) decoder.decodeObject(ObjectType.COMPANY_USER_SCOPE,
		companyUserScopeString);
	if (companyUserScope != null) {
	    if (companyUserScopeDAO.validateCompanyUserScopeforUpdateandDelete(companyUserScope)) {
		companyUserScope = companyUserScopeDAO.updateCompanyUserScope(companyUserScope);
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER_SCOPE, companyUserScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given CompanyUserScope object doesn't contain expected data:");
		}
		throw new RequiredDataNotFoundException(
			"Cannot update the CompanyUserScope - Given CompanyUserScope object doesn't contain expected data");
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given CompanyUserScope object is null");
	    }
	    throw new RequiredDataNotFoundException(
		    "Cannot update the CompanyUserScope - Given CompanyUserScope object is null");
	}

	return encodedString;
    }

}

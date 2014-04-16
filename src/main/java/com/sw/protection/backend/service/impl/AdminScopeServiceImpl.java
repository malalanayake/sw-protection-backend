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
import com.sw.protection.backend.dao.AdminScopeDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.AdminScope;
import com.sw.protection.backend.service.AdminScopeService;

/**
 * AdminScope service operation implementation.
 * 
 * @author dinuka
 * 
 */
@Service
public class AdminScopeServiceImpl implements AdminScopeService {
    public static final Logger log = Logger.getLogger(AdminScopeServiceImpl.class.getName());

    @Override
    public String saveAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	String encodedString = "";

	AdminScope adminScope = (AdminScope) decoder.decodeObject(ObjectType.ADMIN_SCOPE, adminScopeString);
	if (adminScope != null) {
	    if (adminScopeDAO.validateAdminScopeforSave(adminScope)) {
		adminScope = adminScopeDAO.saveNewAdminScope(adminScope);
		encodedString = encoder.encodeObject(ObjectType.ADMIN_SCOPE, adminScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given AdminScope object doesn't contain expected data:");
		}
		throw new RequiredDataNotFoundException();
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given AdminScope object is null");
	    }
	    throw new RequiredDataNotFoundException();
	}

	return encodedString;
    }

    @Override
    public String getAdminScopes(EncoderDecoderType encoderDecoderType, String userName) throws EncodingException,
	    RequiredDataNotFoundException {

	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	String encodedString = "";
	if (userName != "") {
	    List<AdminScope> adminScopes = adminScopeDAO.getAllAdminScopes(userName);
	    if (adminScopes != null) {
		encodedString = encoder.encodeObjectList(ObjectType.ADMIN_SCOPE, adminScopes);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin scopes list is empty so return empty string as encoded admin scope list string");
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
    public String deleteAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	String encodedString = "";

	AdminScope adminScope = (AdminScope) decoder.decodeObject(ObjectType.ADMIN_SCOPE, adminScopeString);
	if (adminScope != null) {
	    if (adminScopeDAO.validateAdminScopeforUpdateandDelete(adminScope)) {
		adminScope = adminScopeDAO.deleteAdminScope(adminScope);
		encodedString = encoder.encodeObject(ObjectType.ADMIN_SCOPE, adminScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given AdminScope object doesn't contain expected data:");
		}
		throw new RequiredDataNotFoundException(
			"Cannot delete the AdminScope - Given AdminScope object doesn't contain expected data");
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given AdminScope object is null");
	    }
	    throw new RequiredDataNotFoundException("Cannot delete the AdminScope - Given AdminScope object is null");
	}

	return encodedString;
    }

    @Override
    public String updateAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	String encodedString = "";

	AdminScope adminScope = (AdminScope) decoder.decodeObject(ObjectType.ADMIN_SCOPE, adminScopeString);
	if (adminScope != null) {
	    if (adminScopeDAO.validateAdminScopeforUpdateandDelete(adminScope)) {
		adminScope = adminScopeDAO.updateAdminScope(adminScope);
		encodedString = encoder.encodeObject(ObjectType.ADMIN_SCOPE, adminScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given AdminScope object doesn't contain expected data:");
		}
		throw new RequiredDataNotFoundException(
			"Cannot update the AdminScope - Given AdminScope object doesn't contain expected data");
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given AdminScope object is null");
	    }
	    throw new RequiredDataNotFoundException("Cannot update the AdminScope - Given AdminScope object is null");
	}

	return encodedString;
    }

    @Override
    public String getAdminScope(EncoderDecoderType encoderDecoderType, String userName, String api_name)
	    throws EncodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
	String encodedString = "";
	if (userName != "" && api_name != "") {
	    AdminScope adminScope = adminScopeDAO.getAdminScope(userName, api_name);
	    if (adminScope != null) {
		encodedString = encoder.encodeObject(ObjectType.ADMIN_SCOPE, adminScope);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin scopes is empty so return empty string as encoded admin scope string");
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
    public String isAccessGrantedFor(String userName, String api_name, String api_opertaion)
	    throws RequiredDataNotFoundException {
	AdminScopeDAO adminScopeDAO = AppContext.getInstance().getBean(AdminScopeDAO.class);
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
		boolean result = adminScopeDAO.isAccessGrantedFor(userName, api_name,
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
}

package com.sw.protection.backend.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
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
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getCompanyUserScopes(EncoderDecoderType encoderDecoderType, String userName)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String isAccessGrantedFor(String userName, String api_name, String api_opertaion)
	    throws RequiredDataNotFoundException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String deleteCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String updateCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	// TODO Auto-generated method stub
	return null;
    }

}

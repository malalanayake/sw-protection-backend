package com.sw.protection.backend.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APIKeyGenerator;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.dao.AdminScopeDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.Admin;
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
		    log.debug("Given AdminScope object doesn't contain expected data:" + adminScope.toString());
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
    public String getAdminScopes(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String deleteAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String updateAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	// TODO Auto-generated method stub
	return null;
    }

}

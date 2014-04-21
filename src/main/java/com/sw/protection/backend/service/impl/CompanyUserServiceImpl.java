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
import com.sw.protection.backend.config.APIKeyGenerator;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.dao.CompanyUserDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.service.CompanyUserService;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {
    public static final Logger log = Logger.getLogger(CompanyUserServiceImpl.class.getName());

    @Override
    public String saveCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	String encodedString = "";

	CompanyUser companyUser = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER, companyUserString);
	if (companyUser != null) {
	    if (companyUserDAO.validateCompanyUserforSave(companyUser)) {
		companyUser.setApi_key(APIKeyGenerator.generateAPIKey());
		companyUser = companyUserDAO.saveUser(companyUser);
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + companyUser.toString());
		}
		throw new RequiredDataNotFoundException();
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given object is null");
	    }
	    throw new RequiredDataNotFoundException();
	}

	return encodedString;
    }

    @Override
    public String getCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	String encodedString = "";
	CompanyUser companyUser = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER, companyUserString);
	if (companyUser.getUser_name() != null && companyUser.getUser_name() != "") {
	    companyUser = companyUserDAO.getUser(companyUser.getUser_name());
	    if (companyUser != null) {
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
	    } else {
		// Recode not found according to the user name
		encodedString = "";
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given object doesn't contain expected data:" + companyUser.toString());
	    }
	    throw new RequiredDataNotFoundException("Given object doesn't contain expected data");
	}
	return encodedString;
    }

    @Override
    public String deleteCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	String encodedString = "";
	CompanyUser companyUser = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER, companyUserString);
	if (companyUser != null) {
	    if (companyUserDAO.validateCompanyUserforUpdateandDelete(companyUser)) {
		if (companyUser.getPass_word() == null || companyUser.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    CompanyUser companyUserData = companyUserDAO.getUser(companyUser.getUser_name());
		    if (companyUserData.getPass_word() != null) {
			companyUser.setPass_word(companyUserData.getPass_word());
		    }
		}

		companyUser = companyUserDAO.deleteUser(companyUser);
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + companyUser.toString());
		}
		throw new RequiredDataNotFoundException("Given object doesn't contain expected data");
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given object is null");
	    }
	    throw new RequiredDataNotFoundException("Given object is null");
	}

	return encodedString;
    }

    @Override
    public String updateCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	String encodedString = "";
	CompanyUser companyUser = (CompanyUser) decoder.decodeObject(ObjectType.COMPANY_USER, companyUserString);
	if (companyUser != null) {
	    if (companyUserDAO.validateCompanyUserforUpdateandDelete(companyUser)) {
		if (companyUser.getPass_word() == null || companyUser.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    CompanyUser companyUserData = companyUserDAO.getUser(companyUser.getUser_name());
		    if (companyUserData.getPass_word() != null) {
			companyUser.setPass_word(companyUserData.getPass_word());
		    }
		}
		// generate new API-KEY
		companyUser.setApi_key(APIKeyGenerator.generateAPIKey());
		companyUser = companyUserDAO.updateUser(companyUser);
		encodedString = encoder.encodeObject(ObjectType.COMPANY_USER, companyUser);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + companyUser.toString());
		}
		throw new RequiredDataNotFoundException("Given object doesn't contain expected data");
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given object is null");
	    }
	    throw new RequiredDataNotFoundException("Given object is null");
	}

	return encodedString;
    }

    @Override
    public String getAllCompanyUsers(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	String encodedString = "";
	// TODO : set the max recode limitation on this by configurations
	if (page > 0 && recordePerPage > 0) {
	    EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	    Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);

	    CompanyUserDAO companyUserDAO = AppContext.getInstance().getBean(CompanyUserDAO.class);
	    List<CompanyUser> companyUsers = companyUserDAO.getAllCompanyUsersWithPagination(page, recordePerPage);
	    if (companyUsers != null) {
		encodedString = encoder.encodeObjectList(ObjectType.COMPANY_USER, companyUsers);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("CompanyUser list is empty so return empty string as encoded CompanyUser list string");
		}
	    }

	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Page number and Max recode count per page should be grater than zero but found :(Page number:"
			+ page + ") (Max Recode:" + recordePerPage + ")");
	    }

	    throw new RequiredDataNotFoundException(
		    "Page number and Max recode count per page should be grater than zero but found :(Page number:"
			    + page + ") (Max Recode:" + recordePerPage + ")");
	}

	return encodedString;
    }

}

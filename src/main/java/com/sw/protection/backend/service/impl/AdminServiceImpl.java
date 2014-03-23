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
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.service.AdminService;

/**
 * Admin service operation implementation.
 * 
 * @author dinuka
 * 
 */

@Service
public class AdminServiceImpl implements AdminService {
    public static final Logger log = Logger.getLogger(AdminServiceImpl.class.getName());

    @Override
    public String saveAdmin(EncoderDecoderType encoderDecoderType, String adminString) throws DuplicateRecordException,
	    OperationRollBackException, EncodingException, DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	String encodedString = "";

	Admin admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminString);
	if (admin != null) {
	    if (adminDAO.validateAdminforSave(admin)) {
		admin.setApi_key(APIKeyGenerator.generateAPIKey());
		admin = adminDAO.saveAdmin(admin);
		encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + admin.toString());
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
    public String getAdmin(EncoderDecoderType encoderDecoderType, String adminString) throws EncodingException,
	    DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	String encodedString = "";
	Admin admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminString);
	if (admin.getUser_name() != null && admin.getUser_name() != "") {
	    admin = adminDAO.getAdmin(admin.getUser_name());
	    if (admin != null) {
		encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    } else {
		// Recode not found according to the user name
		encodedString = "";
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given object doesn't contain expected data:" + admin.toString());
	    }
	    throw new RequiredDataNotFoundException();
	}
	return encodedString;
    }

    @Override
    public String deleteAdmin(EncoderDecoderType encoderDecoderType, String adminString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	String encodedString = "";
	Admin admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminString);
	if (admin != null) {
	    if (adminDAO.validateAdminforUpdateandDelete(admin)) {
		if (admin.getPass_word() == null || admin.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    Admin adminData = adminDAO.getAdmin(admin.getUser_name());
		    if (adminData.getPass_word() != null) {
			admin.setPass_word(adminData.getPass_word());
		    }
		}
		admin = adminDAO.deleteAdmin(admin);
		encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + admin.toString());
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
    public String updateAdmin(EncoderDecoderType encoderDecoderType, String adminString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	String encodedString = "";
	Admin admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminString);
	if (admin != null) {
	    if (adminDAO.validateAdminforUpdateandDelete(admin)) {
		if (admin.getPass_word() == null || admin.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    Admin adminData = adminDAO.getAdmin(admin.getUser_name());
		    if (adminData.getPass_word() != null) {
			admin.setPass_word(adminData.getPass_word());
		    }
		}
		admin = adminDAO.updateAdmin(admin);
		encodedString = encoder.encodeObject(ObjectType.ADMIN, admin);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + admin.toString());
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
    public String getAllAdmins(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	String encodedString = "";
	// TODO : set the max recode limitation on this by configurations
	if (page > 0 && recordePerPage > 0) {
	    EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	    Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);

	    AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	    List<Admin> admins = adminDAO.getAllAdminsWithPagination(page, recordePerPage);
	    if (admins != null) {
		encodedString = encoder.encodeObjectList(ObjectType.ADMIN, admins);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Admin user list is empty so return empty string as encoded admin user list string");
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

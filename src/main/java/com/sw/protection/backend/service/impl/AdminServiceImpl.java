package com.sw.protection.backend.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APIKeyGenerator;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.dao.impl.AdminDAOImpl;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.decoder.impl.DecoderFactoryImpl;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.encoder.impl.EncoderFactoryImpl;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.service.AdminService;

/**
 * Admin service operation implementation.
 * 
 * @author dinuka
 * 
 */
public class AdminServiceImpl implements AdminService {
    public static final Logger log = Logger.getLogger(AdminServiceImpl.class.getName());
    private static AdminServiceImpl adminServiceImpl;

    private AdminServiceImpl() {

    }

    public static AdminServiceImpl getInstance() {
	if (adminServiceImpl == null) {
	    synchronized (AdminServiceImpl.class) {
		if (adminServiceImpl == null) {
		    adminServiceImpl = new AdminServiceImpl();
		}
	    }
	}
	return adminServiceImpl;
    }

    @Override
    public String saveAdmin(EncoderDecoderType encoderDecoderType, String adminString) throws DuplicateRecordException,
	    OperationRollBackException, EncodingException, DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = new EncoderFactoryImpl();
	DecoderFactory decoderFactory = new DecoderFactoryImpl();
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = new AdminDAOImpl();
	String encodedString = "";

	Admin admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminString);
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

	return encodedString;

    }

    @Override
    public String getAdmin(EncoderDecoderType encoderDecoderType, String adminString) throws EncodingException,
	    DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = new EncoderFactoryImpl();
	DecoderFactory decoderFactory = new DecoderFactoryImpl();
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = new AdminDAOImpl();
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
	EncoderFactory encoderFactory = new EncoderFactoryImpl();
	DecoderFactory decoderFactory = new DecoderFactoryImpl();
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = new AdminDAOImpl();
	String encodedString = "";
	Admin admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminString);
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

	return encodedString;
    }

    @Override
    public String updateAdmin(EncoderDecoderType encoderDecoderType, String adminString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = new EncoderFactoryImpl();
	DecoderFactory decoderFactory = new DecoderFactoryImpl();
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	AdminDAO adminDAO = new AdminDAOImpl();
	String encodedString = "";
	Admin admin = (Admin) decoder.decodeObject(ObjectType.ADMIN, adminString);
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

	return encodedString;
    }

    @Override
    public String getAllAdmins(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, DecodingException {
	EncoderFactory encoderFactory = new EncoderFactoryImpl();
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	AdminDAO adminDAO = new AdminDAOImpl();
	String encodedString = "";
	List<Admin> admins = adminDAO.getAllAdminsWithPagination(page, recordePerPage);
	encodedString = encoder.encodeObjectList(ObjectType.ADMIN, admins);
	return encodedString;
    }

}

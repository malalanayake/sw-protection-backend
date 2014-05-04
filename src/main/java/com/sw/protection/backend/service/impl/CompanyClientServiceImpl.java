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
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.dao.CompanyClientDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.service.CompanyClientService;

@Service
public class CompanyClientServiceImpl implements CompanyClientService {
    public static final Logger log = Logger.getLogger(CompanyClientServiceImpl.class.getName());

    @Override
    public String saveCompanyClient(EncoderDecoderType encoderDecoderType, String companyClientString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyClientDAO companyClientDAO = AppContext.getInstance().getBean(CompanyClientDAO.class);
	String encodedString = "";

	CompanyClient companyClient = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT, companyClientString);
	if (companyClient != null) {
	    if (companyClientDAO.validateCompanyClientforSave(companyClient)) {
		companyClient = companyClientDAO.saveCompanyClient(companyClient);
		encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + companyClient.toString());
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
    public String getCompanyClient(EncoderDecoderType encoderDecoderType, String companyClientUserName)
	    throws EncodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	CompanyClientDAO companyClientDAO = AppContext.getInstance().getBean(CompanyClientDAO.class);
	String encodedString = "";
	CompanyClient companyClient = null;
	if (companyClientUserName != null && companyClientUserName != "") {
	    companyClient = companyClientDAO.getCompanyClient(companyClientUserName);
	    if (companyClient != null) {
		encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
	    } else {
		// Recode not found according to the user name
		encodedString = "";
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given object doesn't contain expected data");
	    }
	    throw new RequiredDataNotFoundException("Given object doesn't contain expected data");
	}
	return encodedString;
    }

    @Override
    public String deleteCompanyClient(EncoderDecoderType encoderDecoderType, String companyClientString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyClientDAO companyClientDAO = AppContext.getInstance().getBean(CompanyClientDAO.class);
	String encodedString = "";
	CompanyClient companyClient = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT, companyClientString);
	if (companyClient != null) {
	    if (companyClientDAO.validateCompanyClientforUpdateandDelete(companyClient)) {
		if (companyClient.getPass_word() == null || companyClient.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    CompanyClient companyClientData = companyClientDAO.getCompanyClient(companyClient.getUser_name());
		    if (companyClientData.getPass_word() != null) {
			companyClient.setPass_word(companyClientData.getPass_word());
		    }
		}

		companyClient = companyClientDAO.deleteCompanyClient(companyClient);
		encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + companyClient.toString());
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
    public String updateCompanyClient(EncoderDecoderType encoderDecoderType, String companyClientString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyClientDAO companyClientDAO = AppContext.getInstance().getBean(CompanyClientDAO.class);
	String encodedString = "";
	CompanyClient companyClient = (CompanyClient) decoder.decodeObject(ObjectType.CLIENT, companyClientString);
	if (companyClient != null) {
	    if (companyClientDAO.validateCompanyClientforUpdateandDelete(companyClient)) {
		if (companyClient.getPass_word() == null || companyClient.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    CompanyClient companyClientData = companyClientDAO.getCompanyClient(companyClient.getUser_name());
		    if (companyClientData.getPass_word() != null) {
			companyClient.setPass_word(companyClientData.getPass_word());
		    }
		}

		companyClient = companyClientDAO.updateCompanyClient(companyClient);
		encodedString = encoder.encodeObject(ObjectType.CLIENT, companyClient);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + companyClient.toString());
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
    public String getAllCompanyClients(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, RequiredDataNotFoundException {
	String encodedString = "";
	// TODO : set the max recode limitation on this by configurations
	if (page > 0 && recordePerPage > 0) {
	    EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	    Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);

	    CompanyClientDAO companyClientDAO = AppContext.getInstance().getBean(CompanyClientDAO.class);
	    List<CompanyClient> companyClients = companyClientDAO.getAllCompanyClientsWithPagination(page,
		    recordePerPage);
	    if (companyClients != null) {
		encodedString = encoder.encodeObjectList(ObjectType.CLIENT, companyClients);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("CompanyClient list is empty so return empty string as encoded CompanyClient list string");
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

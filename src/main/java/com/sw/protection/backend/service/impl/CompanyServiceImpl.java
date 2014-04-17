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
import com.sw.protection.backend.dao.CompanyDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
    public static final Logger log = Logger.getLogger(CompanyServiceImpl.class.getName());

    @Override
    public String saveCompany(EncoderDecoderType encoderDecoderType, String companyString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	String encodedString = "";

	Company company = (Company) decoder.decodeObject(ObjectType.COMPANY, companyString);
	if (company != null) {
	    if (companyDAO.validateCompanyforSave(company)) {
		company.setApi_key(APIKeyGenerator.generateAPIKey());
		company = companyDAO.saveCompany(company);
		encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + company.toString());
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
    public String getCompany(EncoderDecoderType encoderDecoderType, String companyString) throws EncodingException,
	    DecodingException, RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	String encodedString = "";
	Company company = (Company) decoder.decodeObject(ObjectType.COMPANY, companyString);
	if (company.getUser_name() != null && company.getUser_name() != "") {
	    company = companyDAO.getCompany(company.getUser_name());
	    if (company != null) {
		encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
	    } else {
		// Recode not found according to the user name
		encodedString = "";
	    }
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Given object doesn't contain expected data:" + company.toString());
	    }
	    throw new RequiredDataNotFoundException("Given object doesn't contain expected data");
	}
	return encodedString;
    }

    @Override
    public String deleteCompany(EncoderDecoderType encoderDecoderType, String companyString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	String encodedString = "";
	Company company = (Company) decoder.decodeObject(ObjectType.COMPANY, companyString);
	if (company != null) {
	    if (companyDAO.validateCompanyforUpdateandDelete(company)) {
		if (company.getPass_word() == null || company.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    Company companyData = companyDAO.getCompany(company.getUser_name());
		    if (companyData.getPass_word() != null) {
			company.setPass_word(companyData.getPass_word());
		    }
		}

		company = companyDAO.deleteCompany(company);
		encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + company.toString());
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
    public String updateCompany(EncoderDecoderType encoderDecoderType, String companyString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException {
	EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
	Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
	Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
	CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	String encodedString = "";
	Company company = (Company) decoder.decodeObject(ObjectType.COMPANY, companyString);
	if (company != null) {
	    if (companyDAO.validateCompanyforUpdateandDelete(company)) {
		if (company.getPass_word() == null || company.getPass_word() == "") {
		    // If password is not set then it will automatically get the
		    // existing one and save it
		    Company companyData = companyDAO.getCompany(company.getUser_name());
		    if (companyData.getPass_word() != null) {
			company.setPass_word(companyData.getPass_word());
		    }
		}
		// generate new API-KEY
		company.setApi_key(APIKeyGenerator.generateAPIKey());
		company = companyDAO.updateCompany(company);
		encodedString = encoder.encodeObject(ObjectType.COMPANY, company);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Given object doesn't contain expected data:" + company.toString());
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
    public String getAllCompanies(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException {
	String encodedString = "";
	// TODO : set the max recode limitation on this by configurations
	if (page > 0 && recordePerPage > 0) {
	    EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
	    Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);

	    CompanyDAO companyDAO = AppContext.getInstance().getBean(CompanyDAO.class);
	    List<Company> companies = companyDAO.getAllCompaniesWithPagination(page, recordePerPage);
	    if (companies != null) {
		encodedString = encoder.encodeObjectList(ObjectType.COMPANY, companies);
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Company list is empty so return empty string as encoded Company list string");
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

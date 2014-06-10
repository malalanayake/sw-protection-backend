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
import com.sw.protection.backend.dao.CompanySWDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.service.CompanySWService;

@Service
public class CompanySWServiceImpl implements CompanySWService {

	public static final Logger log = Logger.getLogger(CompanySWServiceImpl.class.getName());

	@Override
	public String saveCompanySW(EncoderDecoderType encoderDecoderType, String companySWString)
			throws DuplicateRecordException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
		CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
		String encodedString = "";

		CompanySW companysw = (CompanySW) decoder
				.decodeObject(ObjectType.SOFTWARE, companySWString);
		if (companysw != null) {
			if (companySWDAO.validateCompanySWforSave(companysw)) {
				companysw = companySWDAO.saveCompanySW(companysw);
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companysw);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Given object doesn't contain expected data:" + companysw.toString());
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
	public String getCompanySW(EncoderDecoderType encoderDecoderType, String companyUserName,
			String softwareName) throws EncodingException, RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
		String encodedString = "";
		CompanySW companySW = null;
		if (companyUserName != null && softwareName != null) {
			companySW = companySWDAO.getCompanySW(companyUserName, softwareName);
			if (companySW != null) {
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySW);
			} else {
				// Recode not found according to the user name
				encodedString = "";
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Company user name or Software name is null");
			}
			throw new RequiredDataNotFoundException("Company user name or Software name is null");
		}
		return encodedString;
	}

	@Override
	public String deleteCompanySW(EncoderDecoderType encoderDecoderType, String companySWString)
			throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
		CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
		String encodedString = "";
		CompanySW companySW = (CompanySW) decoder
				.decodeObject(ObjectType.SOFTWARE, companySWString);
		if (companySW != null) {
			if (companySWDAO.validateCompanySWforUpdateandDelete(companySW)) {
				companySW = companySWDAO.deleteCompanySW(companySW);
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySW);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Given object doesn't contain expected data:" + companySW.toString());
				}
				throw new RequiredDataNotFoundException(
						"Given object doesn't contain expected data");
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
	public String updateCompanySW(EncoderDecoderType encoderDecoderType, String companySWString)
			throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
		CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
		String encodedString = "";
		CompanySW companySW = (CompanySW) decoder
				.decodeObject(ObjectType.SOFTWARE, companySWString);
		if (companySW != null) {
			if (companySWDAO.validateCompanySWforUpdateandDelete(companySW)) {
				companySW = companySWDAO.updateCompanySW(companySW);
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE, companySW);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Given object doesn't contain expected data:" + companySW.toString());
				}
				throw new RequiredDataNotFoundException(
						"Given object doesn't contain expected data");
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
	public String getAllCompanySW(EncoderDecoderType encoderDecoderType, int page,
			int recordePerPage) throws EncodingException, RequiredDataNotFoundException {
		String encodedString = "";
		// TODO : set the max recode limitation on this by configurations
		if (page > 0 && recordePerPage > 0) {
			EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
			Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);

			CompanySWDAO companySWDAO = AppContext.getInstance().getBean(CompanySWDAO.class);
			List<CompanySW> companySW = companySWDAO.getAllCompanySWWithPagination(page,
					recordePerPage);
			if (companySW != null) {
				encodedString = encoder.encodeObjectList(ObjectType.SOFTWARE, companySW);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("CompanySW list is empty so return empty string as encoded CompanySW list string");
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

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
import com.sw.protection.backend.dao.CompanySWCopyDAO;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.service.CompanySWCopyService;

@Service
public class CompanySWCopyServiceImpl implements CompanySWCopyService {
	public static final Logger log = Logger.getLogger(CompanySWCopyServiceImpl.class.getName());

	@Override
	public String saveCompanySWCopy(EncoderDecoderType encoderDecoderType,
			String companySWCopyString) throws DuplicateRecordException,
			OperationRollBackException, EncodingException, DecodingException,
			RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
		CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance()
				.getBean(CompanySWCopyDAO.class);
		String encodedString = "";

		CompanySWCopy companySWCopy = (CompanySWCopy) decoder.decodeObject(
				ObjectType.SOFTWARE_COPY, companySWCopyString);
		if (companySWCopy != null) {
			if (companySWCopyDAO.validateCompanySWCopyforSave(companySWCopy)) {
				companySWCopy = companySWCopyDAO.saveCompanySWCopy(companySWCopy);
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Given object doesn't contain expected data:"
							+ companySWCopy.toString());
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
	public String getCompanySWCopy(EncoderDecoderType encoderDecoderType, String clientUserName,
			String softwareName, String motherBoard, String hd, String mac)
			throws EncodingException, RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance()
				.getBean(CompanySWCopyDAO.class);
		String encodedString = "";
		CompanySWCopy companySWCopy = null;
		if (clientUserName != null && softwareName != null) {
			companySWCopy = companySWCopyDAO.getCompanySWCopy(clientUserName, softwareName,
					motherBoard, hd, mac);
			if (companySWCopy != null) {
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
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
	public String deleteCompanySWCopy(EncoderDecoderType encoderDecoderType,
			String companySWCopyString) throws RecordAlreadyModifiedException,
			OperationRollBackException, EncodingException, DecodingException,
			RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
		CompanySWCopyDAO companySWDAO = AppContext.getInstance().getBean(CompanySWCopyDAO.class);
		String encodedString = "";
		CompanySWCopy companySWCopy = (CompanySWCopy) decoder.decodeObject(
				ObjectType.SOFTWARE_COPY, companySWCopyString);
		if (companySWCopy != null) {
			if (companySWDAO.validateCompanySWCopyforUpdateandDelete(companySWCopy)) {
				companySWCopy = companySWDAO.deleteCompanySWCopy(companySWCopy);
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Given object doesn't contain expected data:"
							+ companySWCopy.toString());
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
	public String updateCompanySWCopy(EncoderDecoderType encoderDecoderType, String companySWString)
			throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException {
		EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
		DecoderFactory decoderFactory = AppContext.getInstance().getBean(DecoderFactory.class);
		Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);
		Decoder decoder = decoderFactory.getDecoder(encoderDecoderType);
		CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance()
				.getBean(CompanySWCopyDAO.class);
		String encodedString = "";
		CompanySWCopy companySWCopy = (CompanySWCopy) decoder.decodeObject(
				ObjectType.SOFTWARE_COPY, companySWString);
		if (companySWCopy != null) {
			if (companySWCopyDAO.validateCompanySWCopyforUpdateandDelete(companySWCopy)) {
				companySWCopy = companySWCopyDAO.updateCompanySWCopy(companySWCopy);
				encodedString = encoder.encodeObject(ObjectType.SOFTWARE_COPY, companySWCopy);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Given object doesn't contain expected data:"
							+ companySWCopy.toString());
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
	public String getAllCompanySWCopy(EncoderDecoderType encoderDecoderType, int page,
			int recordePerPage) throws EncodingException, RequiredDataNotFoundException {
		String encodedString = "";
		// TODO : set the max recode limitation on this by configurations
		if (page > 0 && recordePerPage > 0) {
			EncoderFactory encoderFactory = AppContext.getInstance().getBean(EncoderFactory.class);
			Encoder encoder = encoderFactory.getEncoder(encoderDecoderType);

			CompanySWCopyDAO companySWCopyDAO = AppContext.getInstance().getBean(
					CompanySWCopyDAO.class);
			List<CompanySWCopy> companySWCopy = companySWCopyDAO.getAllCompanySWCopyWithPagination(
					page, recordePerPage);
			if (companySWCopy != null) {
				encodedString = encoder.encodeObjectList(ObjectType.SOFTWARE_COPY, companySWCopy);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("CompanySWCopy list is empty so return empty string as encoded CompanySWCopy list string");
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

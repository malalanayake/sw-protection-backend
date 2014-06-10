package com.sw.protection.backend.service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * This interface is used to define the all CompanyUser service operations
 * 
 * @author dinuka
 * 
 */
public interface CompanyClientService {

	public String saveCompanyClient(EncoderDecoderType encoderDecoderType,
			String companyClientString) throws DuplicateRecordException,
			OperationRollBackException, EncodingException, DecodingException,
			RequiredDataNotFoundException;

	public String getCompanyClient(EncoderDecoderType encoderDecoderType,
			String companyClientUserName) throws EncodingException, RequiredDataNotFoundException;

	public String deleteCompanyClient(EncoderDecoderType encoderDecoderType,
			String companyClientString) throws RecordAlreadyModifiedException,
			OperationRollBackException, EncodingException, DecodingException,
			RequiredDataNotFoundException;

	public String updateCompanyClient(EncoderDecoderType encoderDecoderType,
			String companyClientString) throws RecordAlreadyModifiedException,
			OperationRollBackException, EncodingException, DecodingException,
			RequiredDataNotFoundException;

	public String getAllCompanyClients(EncoderDecoderType encoderDecoderType, int page,
			int recordePerPage) throws EncodingException, RequiredDataNotFoundException;
}

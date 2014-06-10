package com.sw.protection.backend.service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * This interface is used to define the all CompanySW service operations
 * 
 * @author dinuka
 * 
 */
public interface CompanySWService {

	public String saveCompanySW(EncoderDecoderType encoderDecoderType, String companySWString)
			throws DuplicateRecordException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException;

	public String getCompanySW(EncoderDecoderType encoderDecoderType, String companyUserName,
			String softwareName) throws EncodingException, RequiredDataNotFoundException;

	public String deleteCompanySW(EncoderDecoderType encoderDecoderType, String companySWString)
			throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException;

	public String updateCompanySW(EncoderDecoderType encoderDecoderType, String companySWString)
			throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException;

	public String getAllCompanySW(EncoderDecoderType encoderDecoderType, int page,
			int recordePerPage) throws EncodingException, RequiredDataNotFoundException;
}

package com.sw.protection.backend.service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;

public interface CompanySWCopyService {

	public String saveCompanySWCopy(EncoderDecoderType encoderDecoderType,
			String companySWCopyString) throws DuplicateRecordException,
			OperationRollBackException, EncodingException, DecodingException,
			RequiredDataNotFoundException;

	public String getCompanySWCopy(EncoderDecoderType encoderDecoderType, String clientUserName,
			String softwareName, String motherBoard, String hd, String mac)
			throws EncodingException, RequiredDataNotFoundException;

	public String deleteCompanySWCopy(EncoderDecoderType encoderDecoderType,
			String companySWCopyString) throws RecordAlreadyModifiedException,
			OperationRollBackException, EncodingException, DecodingException,
			RequiredDataNotFoundException;

	public String updateCompanySWCopy(EncoderDecoderType encoderDecoderType, String companySWString)
			throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException,
			DecodingException, RequiredDataNotFoundException;

	public String getAllCompanySWCopy(EncoderDecoderType encoderDecoderType, int page,
			int recordePerPage) throws EncodingException, RequiredDataNotFoundException;

}

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
public interface CompanyUserService {

    public String saveCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String getCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException;

    public String deleteCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String updateCompanyUser(EncoderDecoderType encoderDecoderType, String companyUserString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String getAllCompanyUsers(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException;
}

package com.sw.protection.backend.service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * This interface is used to define the all Admin service operations
 * 
 * @author dinuka
 * 
 */
public interface CompanyService {

    public String saveCompany(EncoderDecoderType encoderDecoderType, String companyString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String getCompany(EncoderDecoderType encoderDecoderType, String companyString) throws EncodingException,
	    DecodingException, RequiredDataNotFoundException;

    public String deleteCompany(EncoderDecoderType encoderDecoderType, String companyString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String updateCompany(EncoderDecoderType encoderDecoderType, String companyString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String getAllCompanies(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException;
}

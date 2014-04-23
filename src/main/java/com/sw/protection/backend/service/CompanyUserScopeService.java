package com.sw.protection.backend.service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * This interface is used to define the all CompanyUserScope service operations
 * 
 * @author dinuka
 * 
 */
public interface CompanyUserScopeService {
    public String saveCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String getCompanyUserScope(EncoderDecoderType encoderDecoderType, String userName, String api_name)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException;

    public String getCompanyUserScopes(EncoderDecoderType encoderDecoderType, String userName)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException;

    public String isAccessGrantedFor(String userName, String api_name, String api_opertaion)
	    throws RequiredDataNotFoundException;

    public String deleteCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String updateCompanyUserScope(EncoderDecoderType encoderDecoderType, String companyUserScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;
}

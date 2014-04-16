package com.sw.protection.backend.service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * This interface is used to define the all AdminScope service operations
 * 
 * @author dinuka
 * 
 */
public interface AdminScopeService {
    public String saveAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws DuplicateRecordException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String getAdminScopes(EncoderDecoderType encoderDecoderType, String userName) throws EncodingException,
	    RequiredDataNotFoundException;

    public String getAdminScope(EncoderDecoderType encoderDecoderType, String userName, String api_name)
	    throws EncodingException, RequiredDataNotFoundException;

    public String isAccessGrantedFor(String userName, String api_name, String api_opertaion)
	    throws RequiredDataNotFoundException;

    public String deleteAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String updateAdminScope(EncoderDecoderType encoderDecoderType, String adminScopeString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;
}

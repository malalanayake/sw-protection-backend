package com.sw.protection.backend.service;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * This interface is used to define the all Super Admin service operations
 * 
 * @author dinuka
 * 
 */
public interface AdminService {

    public String saveAdmin(EncoderDecoderType encoderDecoderType, String adminString) throws DuplicateRecordException,
	    OperationRollBackException, EncodingException, DecodingException, RequiredDataNotFoundException;

    public String getAdmin(EncoderDecoderType encoderDecoderType, String adminString) throws EncodingException,
	    DecodingException, RequiredDataNotFoundException;

    public String deleteAdmin(EncoderDecoderType encoderDecoderType, String adminString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String updateAdmin(EncoderDecoderType encoderDecoderType, String adminString)
	    throws RecordAlreadyModifiedException, OperationRollBackException, EncodingException, DecodingException,
	    RequiredDataNotFoundException;

    public String getAllAdmins(EncoderDecoderType encoderDecoderType, int page, int recordePerPage)
	    throws EncodingException, DecodingException, RequiredDataNotFoundException;
}

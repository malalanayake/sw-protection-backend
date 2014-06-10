package com.sw.protection.backend.common.exception;

/**
 * Exception class for throw when the recode is going to be duplicated in the
 * database
 * 
 * @author dinuka
 * 
 */
public class DuplicateRecordException extends Exception {

	public DuplicateRecordException() {
	}

	public DuplicateRecordException(String message) {
		super(message);
	}
}

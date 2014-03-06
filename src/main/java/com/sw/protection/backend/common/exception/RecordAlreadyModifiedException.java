package com.sw.protection.backend.common.exception;

/**
 * Custom exception class for throw when the record already modified in the
 * database
 * 
 * @author dinuka
 * 
 */
public class RecordAlreadyModifiedException extends Exception {

    public RecordAlreadyModifiedException() {
    }

    public RecordAlreadyModifiedException(String message) {
	super(message);
    }
}

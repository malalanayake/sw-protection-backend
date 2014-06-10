package com.sw.protection.backend.common.exception;

/**
 * This exception will throw when the mandatory data is not found.
 * 
 * @author dinuka
 * 
 */
public class RequiredDataNotFoundException extends Exception {
	public RequiredDataNotFoundException() {
	}

	public RequiredDataNotFoundException(String message) {
		super(message);
	}
}

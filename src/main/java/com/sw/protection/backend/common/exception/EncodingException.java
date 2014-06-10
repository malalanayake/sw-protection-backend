package com.sw.protection.backend.common.exception;

/**
 * Custom exception class for throw when the problem is occur in encoding
 * 
 * @author dinuka
 * 
 */
public class EncodingException extends Exception {
	public EncodingException() {
	}

	public EncodingException(String message) {
		super(message);
	}
}

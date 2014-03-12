package com.sw.protection.backend.common.exception;

/**
 * Custom exception class for throw when the problem is occur in decoding
 * 
 * @author dinuka
 * 
 */
public class DecodingException extends Exception {
    public DecodingException() {
    }

    public DecodingException(String message) {
	super(message);
    }
}

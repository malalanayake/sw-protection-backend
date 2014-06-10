package com.sw.protection.backend.common.exception;

/**
 * Custom exception class for throw when the problem is occur in runtime and
 * roll back the transaction
 * 
 * @author dinuka
 * 
 */
public class OperationRollBackException extends Exception {
	public OperationRollBackException() {
	}

	public OperationRollBackException(String message) {
		super(message);
	}

	public OperationRollBackException(Exception ex) {
		super(ex);
	}
}

package com.sw.protection.backend.common.exception;

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

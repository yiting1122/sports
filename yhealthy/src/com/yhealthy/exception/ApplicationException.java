package com.yhealthy.exception;

public class ApplicationException extends Exception {
	/**
	 * �Զ����쳣
	 */
	private static final long serialVersionUID = 1L;

	public ApplicationException() {
		// TODO Auto-generated constructor stub
		super();
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}
}

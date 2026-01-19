package com.kh.jde.exception;

public class S3ServiceFailureException extends RuntimeException {

	public S3ServiceFailureException(String message) {
		super(message);
	}
}

package com.glimps.glimpsserver.common.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
	private final String message;

	public CustomException(ErrorCode errorCode, String message) {
		super(message);
		this.message = message;
		this.errorCode = errorCode;
	}
}

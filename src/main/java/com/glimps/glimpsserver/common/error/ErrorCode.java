package com.glimps.glimpsserver.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E-001", "예외가 발생했습니다.");


	private final HttpStatus status;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus status, String errorCode, String message) {
		this.status = status;
		this.code = errorCode;
		this.message = message;
	}
}

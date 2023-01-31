package com.glimps.glimpsserver.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E-001"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "E-002");



	private final HttpStatus status;
	private final String code;

	ErrorCode(HttpStatus status, String errorCode) {
		this.status = status;
		this.code = errorCode;
	}
}

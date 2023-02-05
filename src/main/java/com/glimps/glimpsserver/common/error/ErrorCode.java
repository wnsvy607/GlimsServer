package com.glimps.glimpsserver.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// E-XXX
	EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E-001", "서버 내부 오류입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E-002", "사용자를 찾을 수 없습니다."),

	// A-XXX - Authentication Error
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-001", "유효하지 않은 토큰입니다."),

	NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-002", "Authorization Header 가 빈값입니다."),
	NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-003", "인증 타입이 Bearer 타입이 아닙니다."),

	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-004", "토큰이 만료되었습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus status, String errorCode, String message) {
		this.status = status;
		this.code = errorCode;
		this.message = message;
	}
}

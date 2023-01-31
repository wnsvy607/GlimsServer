package com.glimps.glimpsserver.common.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.glimps.glimpsserver.common.dto.ErrorResponse;
import com.glimps.glimpsserver.common.error.CustomException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerErrorAdvice {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Exception occurs: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		log.error("Exception occurs: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode().getStatus(), e.getMessage());
		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.error("Exception occurs: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getBindingResult());
		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}
}

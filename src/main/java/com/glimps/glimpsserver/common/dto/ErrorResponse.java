package com.glimps.glimpsserver.common.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.glimps.glimpsserver.common.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private String message;
	private HttpStatus status;

	public static ErrorResponse of(HttpStatus status, String message) {
		return new ErrorResponse(message, ErrorCode.valueOf(status.name()).getStatus());
	}

	public static ErrorResponse of(HttpStatus status, BindingResult bindingResult) {
		return new ErrorResponse(createErrorMessage(bindingResult), ErrorCode.valueOf(status.name()).getStatus());
	}

	private static String createErrorMessage(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			if (!isFirst) {
				sb.append(",");
			} else {
				isFirst = false;
			}
			sb.append("[");
			sb.append(fieldError.getField());
			sb.append("]");
			sb.append(fieldError.getDefaultMessage());
		}
		return sb.toString();
	}

}

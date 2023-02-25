package com.glimps.glimpsserver.common.jwt;

import lombok.Getter;

@Getter
public enum TokenType {

	ACCESS_TOKEN("access_token"),
	REFRESH_TOKEN("refresh_token");

	private String type;

	TokenType(String type) {
		this.type = type;
	}
}

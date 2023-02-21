package com.glimps.glimpsserver.common.oauth.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenDto {

	private String grantType;
	private String accessToken;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
	private Date accessTokenExpireTime;
	private String refreshToken;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
	private Date refreshTokenExpireTime;

	@Builder
	public JwtTokenDto(String grantType, String accessToken, Date accessTokenExpireTime, String refreshToken,
		Date refreshTokenExpireTime) {
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.accessTokenExpireTime = accessTokenExpireTime;
		this.refreshToken = refreshToken;
		this.refreshTokenExpireTime = refreshTokenExpireTime;
	}
}

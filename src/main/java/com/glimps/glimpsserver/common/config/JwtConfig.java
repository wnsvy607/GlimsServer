package com.glimps.glimpsserver.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.glimps.glimpsserver.common.util.JwtUtil;

@Configuration
public class JwtConfig {
	@Value("${token.access-token-expiration-time}")
	private String accessTokenExpirationTime;
	@Value("${token.refresh-token-expiration-time}")
	private String refreshTokenExpirationTime;
	@Value("${token.secret}")
	private String tokenSecret;

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil(accessTokenExpirationTime, refreshTokenExpirationTime, tokenSecret);
	}
}

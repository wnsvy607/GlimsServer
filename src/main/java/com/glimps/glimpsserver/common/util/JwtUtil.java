package com.glimps.glimpsserver.common.util;

import java.security.Key;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.glimps.glimpsserver.common.error.CustomException;
import com.glimps.glimpsserver.common.error.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {
	private final Key key;

	public JwtUtil() {
		byte[] arr = new byte[32];
		new Random().nextBytes(arr);
		this.key = Keys.hmacShaKeyFor(arr);
	}

	public String encode(String email) {
		return Jwts.builder()
			.signWith(key)
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
			.setHeaderParam("type", "jwt")
			.claim("email", email)
			.compact();
	}

	public Claims decode(String token) {
		if (token == null || token.isBlank()) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SignatureException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}
}

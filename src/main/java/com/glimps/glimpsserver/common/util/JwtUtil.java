package com.glimps.glimpsserver.common.util;

import java.security.Key;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.glimps.glimpsserver.common.error.CustomException;
import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.user.domain.RoleType;

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

	public String createAccessToken(String email, RoleType role, Date expirationTime) {
		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(new Date())
			.signWith(key)
			.setExpiration(expirationTime)
			.setHeaderParam("type", "jwt")
			.claim("role", role)
			.claim("email", email)
			.compact();
	}

	public String createRefreshToken(String email, Date expirationTime) {
		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(new Date())
			.signWith(key)
			.setExpiration(expirationTime)
			.setHeaderParam("type", "jwt")
			.claim("email", email)
			.compact();
	}

	public Date createAccessTokenExpireTime() {
		return new Date(System.currentTimeMillis() + Long.parseLong("9000000"));
	}

	public Date createRefreshTokenExpireTime() {
		return new Date(System.currentTimeMillis() + Long.parseLong("1209600000"));
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

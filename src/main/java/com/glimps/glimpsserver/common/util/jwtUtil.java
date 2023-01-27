package com.glimps.glimpsserver.common.util;

import java.security.Key;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class jwtUtil {
	private final Key key;

	public jwtUtil() {
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
			// TODO: 2023/01/27 throw exception
		}
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SignatureException e) {
			// TODO: 2023/01/27 throw exception
		}
	}
}

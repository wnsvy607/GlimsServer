package com.glimps.glimpsserver.session.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.glimps.glimpsserver.common.util.JwtUtil;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.infra.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public String parseToken(String accessToken) {
		Claims claims = jwtUtil.decode(accessToken);
		return claims.get("email", String.class);
	}

	public List<User> getRoles(String email) {
		return userRepository.findAllByEmail(email);
	}
}

package com.glimps.glimpsserver.session.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.glimps.glimpsserver.common.oauth.dto.JwtTokenDto;
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

	public JwtTokenDto createJwt(User user) {
		Date accessTokenExpireTime = jwtUtil.createAccessTokenExpireTime();
		String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole(), accessTokenExpireTime);

		Date refreshTokenExpireTime = jwtUtil.createRefreshTokenExpireTime();
		String refreshToken = jwtUtil.createRefreshToken(user.getEmail(), refreshTokenExpireTime);

		return JwtTokenDto.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.accessTokenExpireTime(accessTokenExpireTime)
			.refreshTokenExpireTime(refreshTokenExpireTime)
			.build();
	}
}

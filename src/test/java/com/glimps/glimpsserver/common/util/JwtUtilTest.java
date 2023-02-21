package com.glimps.glimpsserver.common.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.glimps.glimpsserver.common.oauth.dto.JwtTokenDto;
import com.glimps.glimpsserver.user.domain.RoleType;

import io.jsonwebtoken.Claims;

// @ExtendWith(MockitoExtension.class)
class JwtUtilTest {

	private final JwtUtil jwtUtil = new JwtUtil();

	private static final String EMAIL = "wnsvy607@naver.com";
	private static final RoleType ROLE = RoleType.USER;

	@Test
	@DisplayName("JwtToken 발급")
	void issueJWT() throws Exception {
		JwtTokenDto jwtTokenDto = jwtUtil.createJwtTokenDto(EMAIL, ROLE);

		assertThat(jwtTokenDto.getAccessToken()).isNotNull().isNotBlank();
		assertThat(jwtTokenDto.getRefreshToken()).isNotNull().isNotBlank();
		assertThat(jwtTokenDto.getAccessTokenExpireTime()).isNotNull().isAfter(new Date());
		assertThat(jwtTokenDto.getRefreshTokenExpireTime()).isNotNull().isAfter(new Date());
		assertThat(jwtTokenDto.getGrantType()).isNotNull();
	}

	@Test
	@DisplayName("발급된 Jwt Decode")
	void decodeAccessToken() throws Exception {
		//given
		JwtTokenDto jwtTokenDto = jwtUtil.createJwtTokenDto(EMAIL, ROLE);

		//when
		Claims accessTokenClaims = jwtUtil.decode(jwtTokenDto.getAccessToken());
		Claims refreshTokenClaims = jwtUtil.decode(jwtTokenDto.getRefreshToken());

		//then
		assertThat(accessTokenClaims.get("email")).isEqualTo(EMAIL);
		assertThat(accessTokenClaims.getSubject()).isEqualTo(EMAIL);
		assertThat(refreshTokenClaims.get("email")).isEqualTo(EMAIL);
		assertThat(refreshTokenClaims.getSubject()).isEqualTo(EMAIL);
		assertThat(accessTokenClaims.get("role")).isEqualTo(ROLE.toString());
	}

	/**
	 * TODO 만료 테스트도 추가할 필요가 있음
	 */

}

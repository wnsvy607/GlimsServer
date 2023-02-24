package com.glimps.glimpsserver.common.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.glimps.glimpsserver.common.oauth.dto.JwtDto;
import com.glimps.glimpsserver.user.domain.RoleType;

import io.jsonwebtoken.Claims;

// @ExtendWith(MockitoExtension.class)
class JwtUtilTest {

	private static final String SECRET = "ugKv3L25LnW1ndJvnUpTZQ77MxkVxqhpexelDc5mR5MmWMFyTnm8h12J8q3wn7dAE";
	private static final String ACCESS_TOKEN_EXP_TIME = "300000";
	private static final String REFRESH_TOKEN_EXP_TIME = "7200000";
	private final JwtUtil jwtUtil = new JwtUtil(ACCESS_TOKEN_EXP_TIME, REFRESH_TOKEN_EXP_TIME, SECRET);

	private static final String EMAIL = "wnsvy607@naver.com";
	private static final RoleType ROLE = RoleType.USER;

	@Test
	@DisplayName("JwtToken 발급")
	void issueJWT() throws Exception {
		JwtDto jwtDto = jwtUtil.createJwtDto(EMAIL, ROLE);

		assertThat(jwtDto.getAccessToken()).isNotNull().isNotBlank();
		assertThat(jwtDto.getRefreshToken()).isNotNull().isNotBlank();
		assertThat(jwtDto.getAccessTokenExpireTime()).isNotNull().isAfter(new Date());
		assertThat(jwtDto.getRefreshTokenExpireTime()).isNotNull().isAfter(new Date());
		assertThat(jwtDto.getGrantType()).isNotNull();
	}

	@Test
	@DisplayName("발급된 Jwt Decode")
	void decodeAccessToken() throws Exception {
		//given
		JwtDto jwtDto = jwtUtil.createJwtDto(EMAIL, ROLE);

		//when
		Claims accessTokenClaims = jwtUtil.decode(jwtDto.getAccessToken());
		Claims refreshTokenClaims = jwtUtil.decode(jwtDto.getRefreshToken());

		//then
		assertThat(accessTokenClaims.getSubject()).isEqualTo(EMAIL);
		assertThat(refreshTokenClaims.getSubject()).isEqualTo(EMAIL);
		assertThat(accessTokenClaims.get("role")).isEqualTo(ROLE.toString());
	}

	/**
	 * TODO 만료 테스트도 추가할 필요가 있음
	 */

}

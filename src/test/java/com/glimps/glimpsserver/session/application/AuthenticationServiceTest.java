package com.glimps.glimpsserver.session.application;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.glimps.glimpsserver.common.oauth.dto.JwtTokenDto;
import com.glimps.glimpsserver.common.util.DateTimeUtils;
import com.glimps.glimpsserver.common.util.JwtUtil;
import com.glimps.glimpsserver.user.application.UserService;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.domain.UserType;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
	private static final Long ID = 3L;
	private static final String EMAIL = "user12345@naver.com";
	private static final String NAME = "장광남";
	private static final UserType KAKAO = UserType.KAKAO;
	private static final RoleType ROLE = RoleType.USER;
	private static final User USER = User.builder()
		.id(ID)
		.email(EMAIL)
		.nickname(NAME)
		.role(ROLE)
		.build();
	private OAuth2User oAuth2User = new DefaultOAuth2User(null, new HashMap<>() {{
		put("name", NAME);
		put("email", EMAIL);
		put("userType", KAKAO.toString().toLowerCase());
	}}, "email");
	@Mock
	private UserService userService;

	@Spy
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthenticationService authenticationService;

	@Test
	@DisplayName("신규 유저 OAuthLogin")
	void whenNotSignedUserLogin() throws Exception {
		//given
		given(userService.getOptionalUserByEmail(EMAIL)).willReturn(Optional.empty());
		given(userService.registerUser(any())).willReturn(ID);
		given(userService.findById(ID)).willReturn(USER);

		//when
		JwtTokenDto jwtTokenDto = authenticationService.oauthLogin(oAuth2User);
		LocalDateTime convertedExpTime = DateTimeUtils.convertToLocalDateTime(
			jwtTokenDto.getRefreshTokenExpireTime());

		//then
		verify(userService, times(1)).registerUser(any());
		verify(jwtUtil, times(1)).createJwtTokenDto(EMAIL, ROLE);
		verify(userService, times(1)).updateRefreshToken(ID, jwtTokenDto.getRefreshToken(),
			jwtTokenDto.getRefreshTokenExpireTime());
	}

	@Test
	@DisplayName("기존 유저 OAuthLogin")
	void whenSignedUserLogin() throws Exception {
		//given
		given(userService.getOptionalUserByEmail(EMAIL)).willReturn(Optional.of(USER));

		//when
		JwtTokenDto jwtTokenDto = authenticationService.oauthLogin(oAuth2User);

		//then
		verify(userService, times(0)).registerUser(any());
		verify(jwtUtil, times(1)).createJwtTokenDto(EMAIL, ROLE);
		verify(userService, times(1)).updateRefreshToken(ID, jwtTokenDto.getRefreshToken(),
			jwtTokenDto.getRefreshTokenExpireTime());
	}

}

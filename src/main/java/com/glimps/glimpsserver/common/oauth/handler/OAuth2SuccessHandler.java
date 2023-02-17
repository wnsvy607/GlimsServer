package com.glimps.glimpsserver.common.oauth.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimps.glimpsserver.common.oauth.dto.JwtTokenDto;
import com.glimps.glimpsserver.common.oauth.dto.OAuthUserInfo;
import com.glimps.glimpsserver.session.application.AuthenticationService;
import com.glimps.glimpsserver.user.application.UserService;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final AuthenticationService authenticationService;
	private final UserService userService;
	private final ObjectMapper mapper;

	/**
	 * OAuth 로그인 성공 시 JWT 발급 및 회원 가입
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		OAuthUserInfo oauthUserInfo = OAuthUserInfo.from(oAuth2User);

		JwtTokenDto jwtTokenDto;

		Optional<User> optionalUser = userService.findUserByEmail(oauthUserInfo.getEmail());
		if (optionalUser.isEmpty()) {
			// 회원 가입
			User oauthUser = User.createUser(oauthUserInfo.toRequest(RoleType.USER));
			userService.registerUser(oauthUser);
			jwtTokenDto = authenticationService.createJwt(oauthUser);

		} else {
			jwtTokenDto = authenticationService.createJwt(optionalUser.get());
		}

		responseJwtDto(response, jwtTokenDto);

	}

	private void responseJwtDto(HttpServletResponse response, JwtTokenDto jwtTokenDto) throws IOException {
		String convertedDto = mapper.writeValueAsString(jwtTokenDto);
		PrintWriter writer = response.getWriter();

		response.setStatus(HttpStatus.ACCEPTED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

		writer.write(convertedDto);
	}
}

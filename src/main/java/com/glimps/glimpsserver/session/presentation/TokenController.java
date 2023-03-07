package com.glimps.glimpsserver.session.presentation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glimps.glimpsserver.session.application.AuthenticationService;
import com.glimps.glimpsserver.session.dto.AccessTokenDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication", description = "로그아웃/토큰재발급 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TokenController {

	private final AuthenticationService authenticationService;

	@Tag(name = "Authentication")
	@Operation(summary = "Access Token 재발급 API", description = "Access Token 재발급 API")
	@PostMapping("/access-token/issue")
	public AccessTokenDto logout(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");

		AccessTokenDto accessTokenDto = authenticationService.issueAccessToken(authorizationHeader);

		return accessTokenDto;
	}
}

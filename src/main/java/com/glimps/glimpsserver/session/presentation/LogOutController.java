package com.glimps.glimpsserver.session.presentation;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glimps.glimpsserver.common.authentication.UserAuthentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "authentication", description = "로그아웃/토큰재발급 API")
@RestController
@RequestMapping("/api/v1")
public class LogOutController {

	@Tag(name = "authentication")
	@Operation(summary = "로그아웃 API", description = "로그아웃시 서버 DB 내부 refresh token 만료 처리")
	@PostMapping("/logout")
	public Map<String, String> logout(UserAuthentication userAuthentication) {

		return Map.of("result", "Logout succeeded");
	}
}

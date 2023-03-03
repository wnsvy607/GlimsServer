package com.glimps.glimpsserver.user.presentation;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glimps.glimpsserver.common.authentication.UserAuthentication;
import com.glimps.glimpsserver.session.dto.UserInfoDto;
import com.glimps.glimpsserver.user.application.UserInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "유저 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserInfoService userInfoService;


	@Tag(name = "User")
	@Operation(summary = "자기 정보 조회 API", description = "자신의 계정 조회 - 인증 필요")
	@GetMapping("/users")
	public UserInfoDto getSignedUserInfo(UserAuthentication userAuthentication) {
		String email = userAuthentication.getEmail();
		return userInfoService.getUserInfo(email);
	}

	@Tag(name = "User")
	@Operation(summary = "타유저 단건 조회 API", description = "타인 계정 조회")
	@GetMapping("/users/{email}")
	public UserInfoDto getSignedUserInfo(@PathVariable String email) {
		return userInfoService.getUserInfo(email);
	}
}

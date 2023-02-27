package com.glimps.glimpsserver.session.presentation;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glimps.glimpsserver.common.authentication.UserAuthentication;

@RestController
@RequestMapping("/api/v1")
public class LogOutController {


	@PostMapping("/logout")
	public Map<String, String> logout(UserAuthentication userAuthentication) {

		return Map.of("result", "Logout succeeded");
	}
}

package com.glimps.glimpsserver.common.presentation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glimps.glimpsserver.common.authentication.UserAuthentication;

@RestController
public class TestController {


	@GetMapping("/test")
	public String test(UserAuthentication userAuthentication) {
		if (userAuthentication == null) {
			return "userAuthentication is null";
		}
		return userAuthentication.getEmail() + " is returned";
	}
}

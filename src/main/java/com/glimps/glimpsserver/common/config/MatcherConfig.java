package com.glimps.glimpsserver.common.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class MatcherConfig {

	private static final List<String> authURL = List.of("/api/v1/user/**", "/api/v1/logout", "/test");
	private static final List<String> adminURL = List.of("/admin/**");

	@Bean
	public List<RequestMatcher> matcher() {

		List<RequestMatcher> result = authURL.stream()
			.map(a -> new AntPathRequestMatcher(a))
			.collect(Collectors.toList());

		result.addAll(adminURL.stream()
			.map(a -> new AntPathRequestMatcher(a))
			.collect(Collectors.toList()));

		return result;
	}

	public static List<String> getAuthURL() {
		return authURL;
	}

	public static List<String> getAdminURL() {
		return adminURL;
	}

}

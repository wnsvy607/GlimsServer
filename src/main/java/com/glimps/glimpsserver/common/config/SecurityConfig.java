package com.glimps.glimpsserver.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import com.glimps.glimpsserver.common.filter.CustomAccessDeniedHandler;
import com.glimps.glimpsserver.common.filter.CustomAuthenticationEntryPoint;
import com.glimps.glimpsserver.common.filter.JwtAuthenticationFilter;
import com.glimps.glimpsserver.common.oauth.handler.OAuth2SuccessHandler;
import com.glimps.glimpsserver.common.oauth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final OAuth2SuccessHandler successHandler;
	private final CustomOAuth2UserService oAuth2UserService;

	private final JwtAuthenticationFilter jwtFilter;

	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf()
			.disable();

		http.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler);

		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.formLogin()
			.disable();

		http.headers()
			.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

		http.oauth2Login()
			.successHandler(successHandler)
			.userInfoEndpoint()
			.userService(oAuth2UserService);

		http.authorizeRequests()
			.antMatchers(MatcherConfig.getAuthURL().toArray(new String[0])).authenticated()
			.antMatchers(MatcherConfig.getAdminURL().toArray(new String[0])).hasRole("ADMIN")
			.anyRequest().permitAll();

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}

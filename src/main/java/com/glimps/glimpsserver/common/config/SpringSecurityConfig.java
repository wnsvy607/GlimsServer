package com.glimps.glimpsserver.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.glimps.glimpsserver.common.oauth.handler.OAuth2SuccessHandler;
import com.glimps.glimpsserver.common.oauth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig {
	// @Autowired
	// private AuthenticationService authenticationService;
	//
	// @Bean
	// public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
	// 	return configuration.getAuthenticationManager();
	// }

	private final CustomOAuth2UserService oAuth2UserService;
	private final OAuth2SuccessHandler successHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf()
			.disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/token/**", "/health", "/v3/api-docs", "/swagger*/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.oauth2Login()
			.successHandler(successHandler)
			.userInfoEndpoint()
			.userService(oAuth2UserService);

		// AuthenticationManager authenticationManager = authenticationManager(
		// 	http.getSharedObject(AuthenticationConfiguration.class));
		// Filter authenticationFilter = new JwtAuthenticationFilter(authenticationManager, authenticationService);
		// Filter authenticationErrorFilter = new AuthenticationErrorFilter();
		// http.exceptionHandling()
		// 	.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		// http.formLogin().disable();

		// http.addFilter(authenticationFilter);

		// http.addFilterAfter(authenticationErrorFilter, JwtAuthenticationFilter.class);
		return http.build();
	}

}

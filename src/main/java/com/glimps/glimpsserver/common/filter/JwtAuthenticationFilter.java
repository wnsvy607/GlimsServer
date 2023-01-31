package com.glimps.glimpsserver.common.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.glimps.glimpsserver.common.security.UserAuthentication;
import com.glimps.glimpsserver.session.application.AuthenticationService;
import com.glimps.glimpsserver.user.domain.User;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
	private final AuthenticationService authenticationService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
		AuthenticationService authenticationService) {
		super(authenticationManager);
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		String authorization = request.getHeader("Authorization");

		if (authorization != null) {
			//TODO refresh token에 대한 처리 필요
			String accessToken = authorization.substring("Bearer ".length());
			String email = authenticationService.parseToken(accessToken);
			List<User> users = authenticationService.getRoles(email);
			Authentication authentication = new UserAuthentication(email, users);

			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
}

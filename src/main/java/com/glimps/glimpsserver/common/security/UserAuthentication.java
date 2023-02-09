package com.glimps.glimpsserver.common.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.glimps.glimpsserver.user.domain.User;

import lombok.Getter;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {
	private final String email;

	public UserAuthentication(String email, List<User> users) {
		super(authorities(users));
		this.email = email;
		super.setAuthenticated(true);
	}

	private static List<GrantedAuthority> authorities(List<User> users) {
		return users.stream()
			.map(user -> new SimpleGrantedAuthority(user.getRole().toString()))
			.collect(Collectors.toList());
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public String getPrincipal() {
		return this.email;
	}

	@Override
	public boolean isAuthenticated() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return false;
		}
		return authentication.isAuthenticated();
	}

	@Override
	public String toString() {
		return "Authentication(" + email + ")";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}

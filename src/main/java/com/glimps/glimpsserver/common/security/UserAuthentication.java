package com.glimps.glimpsserver.common.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.glimps.glimpsserver.user.domain.Role;

import lombok.Getter;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {
	private final String email;

	public UserAuthentication(String email, List<Role> roles) {
		super(authorities(roles));
		this.email = email;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public String toString() {
		return "Authentication(" + email + ")";
	}

	private static List<GrantedAuthority> authorities(List<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getType().toString()))
				.collect(Collectors.toList());
	}
}

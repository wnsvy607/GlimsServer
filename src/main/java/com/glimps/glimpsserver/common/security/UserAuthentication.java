package com.glimps.glimpsserver.common.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.glimps.glimpsserver.user.domain.User;

import lombok.Getter;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {
	private final String email;


	public UserAuthentication(String email, List<User> users) {
		super(authorities(users));
		this.email = email;
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

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}

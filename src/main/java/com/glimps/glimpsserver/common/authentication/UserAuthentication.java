package com.glimps.glimpsserver.common.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {

	private String email;

	private UserAuthentication(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(false);
	}

	public UserAuthentication(
		String email,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.email = email;
		super.setAuthenticated(true);
	}

	// public static UserAuthentication getAnonymous() {
	// 	return new UserAuthentication(List.of(new SimpleGrantedAuthority("ROLE_UNKNOWN")));
	// }

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return email;
	}
}

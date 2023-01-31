package com.glimps.glimpsserver.session.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.glimps.glimpsserver.common.util.JwtUtil;
import com.glimps.glimpsserver.user.domain.Role;
import com.glimps.glimpsserver.user.infra.RoleRepository;
import com.glimps.glimpsserver.user.infra.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final RoleRepository roleRepository;

	public String parseToken(String accessToken) {
		Claims claims = jwtUtil.decode(accessToken);
		return claims.get("email", String.class);
	}

	public List<Role> getRoles(String email) {
		return roleRepository.findAllByEmail(email);
	}
}

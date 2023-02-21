package com.glimps.glimpsserver.session.dto;

import com.glimps.glimpsserver.user.domain.UserType;

public interface SignUpRequest {

	public String getName();

	public String getEmail();

	public UserType getUserType();
}

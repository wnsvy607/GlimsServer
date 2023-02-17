package com.glimps.glimpsserver.user.dto;

import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserCreateRequest {

	private String nickName;
	private String email;
	private RoleType role;
	private UserType userType;

}

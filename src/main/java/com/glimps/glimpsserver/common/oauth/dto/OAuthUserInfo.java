package com.glimps.glimpsserver.common.oauth.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.UserType;
import com.glimps.glimpsserver.user.dto.UserCreateRequest;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthUserInfo {

	private String name;
	private String email;
	private UserType userType;

	public static OAuthUserInfo from(OAuth2User oAuth2User) {
		String type = oAuth2User.getAttribute("userType");
		UserType of = UserType.valueOf(type.toUpperCase());
		return OAuthUserInfo.builder()
			.email(oAuth2User.getAttribute("name"))
			.name(oAuth2User.getAttribute("email"))
			.userType(of)
			.build();
	}

	public UserCreateRequest toRequest(RoleType role) {
		return UserCreateRequest.builder()
			.nickName(name)
			.email(email)
			.role(role)
			.userType(userType)
			.build();
	}

	@Builder
	private OAuthUserInfo(String name, String email, UserType userType) {
		this.name = name;
		this.email = email;
		this.userType = userType;
	}
}

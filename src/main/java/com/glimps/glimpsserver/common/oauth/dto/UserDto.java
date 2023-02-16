package com.glimps.glimpsserver.common.oauth.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.domain.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDto {

	private String name;
	private String email;
	private UserType userType;

	public static UserDto from(OAuth2User oAuth2User) {
		String type = oAuth2User.getAttribute("userType");
		UserType of = UserType.valueOf(type.toUpperCase());
		return new UserDto(oAuth2User.getAttribute("name"), oAuth2User.getAttribute("email"), of);
	}

	public User toEntity(RoleType roleType) {
		return User.builder()
			.nickName(name)
			.email(email)
			.role(roleType)
			.userType(userType)
			.build();
	}
}

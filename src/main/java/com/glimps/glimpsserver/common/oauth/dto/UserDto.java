package com.glimps.glimpsserver.common.oauth.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

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

	public static UserDto from(OAuth2User oAuth2User) {
		return new UserDto(oAuth2User.getAttribute("name"), oAuth2User.getAttribute("email"));
	}
}

package com.glimps.glimpsserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.glimps.glimpsserver.common.oauth.dto.OAuthUserVo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nick_name")
	private String nickname;

	@Column(name = "email")
	private String email;

	@Column(name = "review_cnt")
	private int reviewCnt;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private RoleType role;

	@Column
	@Enumerated(EnumType.STRING)
	private UserType userType;

	public static User createUser(OAuthUserVo oAuthUserVo, RoleType role) {
		return User.builder()
			.nickname(oAuthUserVo.getName())
			.email(oAuthUserVo.getEmail())
			.userType(oAuthUserVo.getUserType())
			.role(role)
			.reviewCnt(0)
			.build();
	}
}

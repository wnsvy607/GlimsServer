package com.glimps.glimpsserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.glimps.glimpsserver.user.dto.UserCreateRequest;

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
	private String nickName;

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

	public static User createUser(UserCreateRequest userCreateRequest) {
		return User.builder()
			.nickName(userCreateRequest.getNickName())
			.email(userCreateRequest.getEmail())
			.userType(userCreateRequest.getUserType())
			.role(userCreateRequest.getRole())
			.reviewCnt(0)
			.build();
	}
}

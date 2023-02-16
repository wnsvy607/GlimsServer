package com.glimps.glimpsserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
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

	@Builder
	public User(String nickName, String email, RoleType role, UserType userType) {
		this.nickName = nickName;
		this.email = email;
		this.role = role;
		this.userType = userType;
		this.reviewCnt = 0;
	}
}

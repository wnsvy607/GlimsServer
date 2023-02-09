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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "email")
	private String email;

	@Column(name = "review_cnt")
	private int reviewCnt;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private RoleType role;

	@Builder
	public User(Long id, String nickname, String email, int reviewCnt, RoleType role) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.reviewCnt = reviewCnt;
		this.role = role;
	}
}

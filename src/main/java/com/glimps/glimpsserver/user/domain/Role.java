package com.glimps.glimpsserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "name")
	@Enumerated(EnumType.STRING)
	private RoleType type;

	public Role(String userEmail, RoleType type) {
		this.userEmail = userEmail;
		this.type = type;
	}

	public Role(Long id, String userEmail, RoleType type) {
		this.id = id;
		this.userEmail = userEmail;
		this.type = type;
	}

	public Role(RoleType type) {
		this(null, type);
	}

}

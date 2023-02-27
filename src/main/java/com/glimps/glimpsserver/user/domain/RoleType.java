package com.glimps.glimpsserver.user.domain;

public enum RoleType {
	USER, ADMIN;

	public String toString() {
		return "ROLE_"+ this.name();
	}

}

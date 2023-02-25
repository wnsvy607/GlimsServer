package com.glimps.glimpsserver.user.domain;

public class RoleTypeConverter {

	public static RoleType RoleTypeConverter(String name) {
		String of = name.substring(5, name.length());
		return RoleType.valueOf(of);
	}

}

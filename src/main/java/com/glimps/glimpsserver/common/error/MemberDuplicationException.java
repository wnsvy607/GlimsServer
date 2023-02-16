package com.glimps.glimpsserver.common.error;

import com.glimps.glimpsserver.user.domain.UserType;

import lombok.Getter;

@Getter
public class MemberDuplicationException extends CustomException {
	private UserType userType;

	public MemberDuplicationException(ErrorCode errorCode, UserType userType) {
		super(errorCode);
		this.userType = userType;
	}
}

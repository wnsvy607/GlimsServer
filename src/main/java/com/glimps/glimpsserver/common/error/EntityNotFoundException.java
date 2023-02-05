package com.glimps.glimpsserver.common.error;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends CustomException {
	// TODO Entity에 따라 보여줄 정보를 다르게 해야함
	private Long id;
	private String email;

	public EntityNotFoundException(ErrorCode errorCode, Long id, String email) {
		super(errorCode);
		this.id = id;
		this.email = email;
	}
}

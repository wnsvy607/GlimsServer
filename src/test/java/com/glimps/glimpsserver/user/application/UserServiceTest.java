package com.glimps.glimpsserver.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.common.error.UserDuplicationException;
import com.glimps.glimpsserver.session.dto.SignUpInfo;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.domain.UserType;
import com.glimps.glimpsserver.user.infra.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	private static final Long ID = 3L;
	private static final String EMAIL = "wnsvy607@naver.com";
	private static final String NAME = "이준표";
	private static final UserType KAKAO = UserType.KAKAO;
	private static final User EXIST_USER = User.builder()
		.id(ID)
		.email(EMAIL)
		.nickname(NAME)
		.userType(KAKAO)
		.role(RoleType.USER)
		.reviewCnt(0)
		.build();

	@Mock
	private UserRepository userRepository;

	@Mock
	private SignUpInfo signUpInfo;

	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("가입된 유저의 이메일로 가입 요청이 오면 예외를 던진다.")
	public void validateDuplicationWhenSignUp() {
		//given
		given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(EXIST_USER));
		given(signUpInfo.getEmail()).willReturn(EMAIL);
		given(signUpInfo.getName()).willReturn(NAME);
		given(signUpInfo.getUserType()).willReturn(UserType.GOOGLE);

		assertThatExceptionOfType(UserDuplicationException.class).isThrownBy(() -> {
				//when
				userService.registerUser(signUpInfo);
			})
			//then
			.withMessage(ErrorCode.ALREADY_REGISTERED_USER.getMessage());
	}

}

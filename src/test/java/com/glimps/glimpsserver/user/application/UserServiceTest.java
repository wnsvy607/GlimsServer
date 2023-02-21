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
import com.glimps.glimpsserver.session.dto.SignUpRequest;
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
	private SignUpRequest signUpRequest;

	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("회원가입 중 이메일 중복 => 예외 발생")
	public void validateDuplicationWhenSignUp() {
		//given
		given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(EXIST_USER));
		given(signUpRequest.getEmail()).willReturn(EMAIL);
		given(signUpRequest.getName()).willReturn(NAME);
		given(signUpRequest.getUserType()).willReturn(UserType.GOOGLE);

		assertThatExceptionOfType(UserDuplicationException.class).isThrownBy(() -> {
				//when
				userService.registerUser(signUpRequest);
			})
			//then
			.withMessage(ErrorCode.ALREADY_REGISTERED_USER.getMessage());
	}

}

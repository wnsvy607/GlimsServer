package com.glimps.glimpsserver.user.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import com.glimps.glimpsserver.common.config.SecurityConfig;
import com.glimps.glimpsserver.session.dto.UserInfoDto;
import com.glimps.glimpsserver.testconfig.WithMockCustomUser;
import com.glimps.glimpsserver.user.application.UserInfoService;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.UserType;

@WebMvcTest(controllers = UserController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
	})
class UserControllerTest {

	private static final String EMAIL = "wnsvy607@naver.com";
	private static final String nickname = "이준표";
	private static final String ROLE = RoleType.USER.toString();
	private static final String USER_TYPE = UserType.KAKAO.name();
	private static final int REVIEW_CNT = 10;
	private static final LocalDateTime CREATED_AT = LocalDateTime.now().minusMonths(1);
	private static final UserInfoDto USER_INFO_DTO =
		UserInfoDto.builder()
			.email(EMAIL)
			.nickname(nickname)
			.role(ROLE)
			.userType(USER_TYPE)
			.reviewCnt(REVIEW_CNT)
			.createdAt(CREATED_AT)
			.build();

	@MockBean
	private UserInfoService userInfoService;

	@Autowired
	private MockMvc mockMvc;

	@WithMockCustomUser(userName = EMAIL)
	@Test
	void given_Authenticated_When_User_Then_Return() throws Exception {
		//given
		given(userInfoService.getUserInfo(EMAIL)).willReturn(USER_INFO_DTO);

		//when
		mockMvc.perform(get("/api/v1/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value(EMAIL))
			.andExpect(jsonPath("$.nickname").value(nickname))
			.andExpect(jsonPath("$.reviewCnt").value(REVIEW_CNT))
			.andExpect(jsonPath("$.role").value(ROLE))
			.andExpect(jsonPath("$.userType").value(USER_TYPE))
			.andExpect(jsonPath("$.createdAt").exists())
			.andDo(print());

		//then
		verify(userInfoService).getUserInfo(EMAIL);

	}

	@WithMockCustomUser
	@Test
	void given_No_When_FindByEmail_Then_Return() throws Exception {
		//given
		given(userInfoService.getUserInfo(EMAIL)).willReturn(USER_INFO_DTO);

		//when
		mockMvc.perform(get("/api/v1/users/" + EMAIL))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value(EMAIL))
			.andExpect(jsonPath("$.nickname").value(nickname))
			.andExpect(jsonPath("$.reviewCnt").value(REVIEW_CNT))
			.andExpect(jsonPath("$.role").value(ROLE))
			.andExpect(jsonPath("$.userType").value(USER_TYPE))
			.andExpect(jsonPath("$.createdAt").exists())
			.andDo(print());

		//then
		verify(userInfoService).getUserInfo(EMAIL);

	}

}

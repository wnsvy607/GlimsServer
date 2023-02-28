package com.glimps.glimpsserver.common.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.glimps.glimpsserver.common.config.SecurityConfig;
import com.glimps.glimpsserver.testconfig.WithMockCustomUser;
import com.glimps.glimpsserver.user.domain.RoleType;

@WebMvcTest(controllers = TestController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
	})
class TestControllerTest {
	private static final String EMAIL = "user12345@naver.com";
	private static final String ROLE = "ROLE_USER";
	@Autowired
	private MockMvc mockMvc;

	@WithMockCustomUser(userName = EMAIL, role = ROLE)
	@Test
	void AuthenticationTest() throws Exception {

		mockMvc.perform(get("/test")).andExpect(status().isOk())
			.andExpect(status().isOk())
			.andExpect(content().string(EMAIL + "has been authenticated."));

	}


}

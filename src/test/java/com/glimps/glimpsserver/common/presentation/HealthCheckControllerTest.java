package com.glimps.glimpsserver.common.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.glimps.glimpsserver.common.config.SecurityConfig;
import com.glimps.glimpsserver.common.filter.JwtAuthenticationFilter;

@WebMvcTest(controllers = HealthCheckController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
	})
// @ImportAutoConfiguration(exclude = {OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class})
@WithMockUser(username = "이준표", password = "",authorities = {"ROLE_USER"})
class HealthCheckControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void HealthCheckTest() throws Exception {
		//given
		mockMvc.perform(get("/health"))
			.andExpect(MockMvcResultMatchers.status().isOk());

		//when

		//then
	}

}

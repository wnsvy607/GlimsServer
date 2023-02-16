package com.glimps.glimpsserver.common.oauth.handler;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.glimps.glimpsserver.common.oauth.dto.UserDto;
import com.glimps.glimpsserver.session.application.AuthenticationService;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final AuthenticationService authenticationService;
	private final UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		UserDto userDto = UserDto.from(oAuth2User);

		Optional<User> optionalUser = userService.findUserByEmail(userDto.getEmail());
		if (optionalUser.isEmpty()) {
			User oauthUser = userDto.toEntity(RoleType.USER);
			userService.registerUser(oauthUser);
			log.info("User: {}", oauthUser);

		} else {
			log.info("Already registered");

		}

		log.info("UserDto: {}", userDto);

		getRedirectStrategy().sendRedirect(request, response, "/health");
	}
}

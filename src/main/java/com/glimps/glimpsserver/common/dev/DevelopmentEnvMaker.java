package com.glimps.glimpsserver.common.dev;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.glimps.glimpsserver.common.oauth.dto.OAuthUserVo;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.domain.UserType;
import com.glimps.glimpsserver.user.infra.UserRepository;

import lombok.RequiredArgsConstructor;

@Profile({"dev", "local"})
@RequiredArgsConstructor
@Component
public class DevelopmentEnvMaker {

	private final UserRepository userRepository;

	@PostConstruct
	private void initDB() {
		User user1 = getUser("이준표", "wnsvy607@naver.com", RoleType.USER);
		User user2 = getUser("강시후", "gyeong0308@gmail.com", RoleType.USER);
		userRepository.saveAll(List.of(user1,user2));
	}

	private User getUser(String name, String email, RoleType role) {
		OAuthUserVo userVo = OAuthUserVo.builder()
			.name(name)
			.email(email)
			.userType(UserType.KAKAO)
			.build();
		User user = User.createUser(userVo, role);
		return user;
	}

}

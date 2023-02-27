package com.glimps.glimpsserver.user.application;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glimps.glimpsserver.common.error.CustomException;
import com.glimps.glimpsserver.common.error.EntityNotFoundException;
import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.common.error.UserDuplicationException;
import com.glimps.glimpsserver.common.util.DateTimeUtils;
import com.glimps.glimpsserver.session.dto.SignUpRequest;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.infra.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
	private final UserRepository userRepository;

	public Optional<User> getOptionalUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional
	public Long registerUser(SignUpRequest signUpRequest) {
		User user = User.createUser(signUpRequest, RoleType.USER);
		validateDuplicateUser(user);
		User savedUser = userRepository.save(user);

		log.info("The new user registered email={}	role={}	provider={}", savedUser.getEmail(),
			savedUser.getRole(),
			savedUser.getUserType());

		return savedUser.getId();
	}

	private void validateDuplicateUser(User user) {
		userRepository.findByEmail(user.getEmail())
			.ifPresent(m -> {
				throw new UserDuplicationException(ErrorCode.ALREADY_REGISTERED_USER, m.getUserType());
			});
	}

	public List<User> getAllByEmail(String email) {
		return userRepository.findAllByEmail(email);
	}

	public User findById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND, id, "UNKNOWN"));
	}

	@Transactional
	public Long updateRefreshToken(Long id, String refreshToken, Date refreshTokenExpireTime) {
		User user = findById(id);
		LocalDateTime convertedExpTime = DateTimeUtils.convertToLocalDateTime(
			refreshTokenExpireTime);

		user.updateRefreshToken(refreshToken, convertedExpTime);

		return user.getId();
	}

	public User getByRefreshToken(String refreshToken) {
		User user = userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

		LocalDateTime tokenExpirationTime = user.getTokenExpirationTime();
		if(tokenExpirationTime.isBefore(LocalDateTime.now())){
			throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		}
		return user;
	}
}

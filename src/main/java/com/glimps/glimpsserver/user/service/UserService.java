package com.glimps.glimpsserver.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.common.error.MemberDuplicationException;
import com.glimps.glimpsserver.user.domain.User;
import com.glimps.glimpsserver.user.infra.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
	private final UserRepository userRepository;

	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Long registerUser(User user) {
		validateDuplicateMember(user);
		return userRepository.save(user).getId();
	}

	private void validateDuplicateMember(User user) {
		Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
		if (optionalUser.isPresent()) {
			throw new MemberDuplicationException(ErrorCode.ALREADY_REGISTERED_MEMBER, optionalUser.get().getUserType());
		}
	}
}

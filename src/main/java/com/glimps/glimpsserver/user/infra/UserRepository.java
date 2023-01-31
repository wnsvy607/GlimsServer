package com.glimps.glimpsserver.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glimps.glimpsserver.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

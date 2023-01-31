package com.glimps.glimpsserver.user.infra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.glimps.glimpsserver.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {


	@Query("select u from User u where u.email = ?1")
	List<User> findAllByEmail(String email);
}

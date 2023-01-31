package com.glimps.glimpsserver.user.infra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.glimps.glimpsserver.user.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query("select r from Role r where r.userEmail = :email")
	List<Role> findAllByEmail(@Param("email") String email);
}

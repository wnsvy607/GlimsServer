package com.glimps.glimpsserver.review.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.glimps.glimpsserver.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@Query("select r from Review r "
		+ "left join fetch User u "
		+ "on r.user.id = u.id "
		+ "left join fetch ReviewPhoto rp "
		+ "on rp.review.id = r.id "
		+ "where u.id = :id")
	Page<Review> findAllByUser(Long id, Pageable pageable);

	@Query("select distinct r from Review r "
		+ "left join fetch ReviewPhoto rp "
		+ "on r.id = rp.review.id "
		+ "where r.id = :id")
	Optional<Review> findByUuid(@Param("id") UUID id);
}

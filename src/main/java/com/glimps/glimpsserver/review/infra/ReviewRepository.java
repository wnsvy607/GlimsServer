package com.glimps.glimpsserver.review.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.glimps.glimpsserver.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@Query("select r from Review r left join fetch User u on r.user.id = u.id where u.id = :id")
	Page<Review> findAllByUser(Long id, Pageable pageable);
}

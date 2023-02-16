package com.glimps.glimpsserver.review.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.glimps.glimpsserver.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {
	@Query("select r from Review r "
		+ "left join fetch User u "
		+ "on r.user.id = u.id "
		+ "left join fetch ReviewPhoto rp "
		+ "on rp.review.id = r.id "
		+ "left join fetch Perfume p "
		+ "on p.id = r.perfume.id "
		+ "where u.id = :id "
		+ "order by r.createdAt desc")
	Page<Review> findAllByUser(Long id, Pageable pageable);
}

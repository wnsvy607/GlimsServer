package com.glimps.glimpsserver.review.infra;

import static com.glimps.glimpsserver.perfume.domain.QPerfume.*;
import static com.glimps.glimpsserver.review.domain.QReview.*;
import static com.glimps.glimpsserver.review.domain.QReviewPhoto.*;
import static com.glimps.glimpsserver.user.domain.QUser.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.glimps.glimpsserver.review.domain.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Review> findTop10ByOrderByCreatedAtDesc() {
		return jpaQueryFactory.selectFrom(review)
			.fetchJoin().leftJoin(reviewPhoto)
			.on(reviewPhoto.review.id.eq(review.id))
			.fetchJoin().leftJoin(user)
			.on(user.id.eq(review.user.id))
			.fetchJoin().leftJoin(perfume)
			.on(perfume.id.eq(review.perfume.id))
			.orderBy(review.createdAt.desc())
			.limit(10)
			.stream().collect(Collectors.toList());
	}
	
	@Override
	public Optional<Review> findByUuid(UUID uuid) {
		return jpaQueryFactory.selectFrom(review)
			.fetchJoin().leftJoin(reviewPhoto)
			.on(reviewPhoto.review.id.eq(review.id))
			.fetchJoin().leftJoin(user)
			.on(user.id.eq(review.user.id))
			.fetchJoin().leftJoin(perfume)
			.on(perfume.id.eq(review.perfume.id))
			.where(review.uuid.eq(uuid))
			.stream()
			.findFirst();
	}
}

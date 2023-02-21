package com.glimps.glimpsserver.review.infra;

import static com.glimps.glimpsserver.perfume.domain.QPerfume.*;
import static com.glimps.glimpsserver.review.domain.QReview.*;
import static com.glimps.glimpsserver.review.domain.QReviewPhoto.*;
import static com.glimps.glimpsserver.user.domain.QUser.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.glimps.glimpsserver.common.domain.CustomPage;
import com.glimps.glimpsserver.common.domain.CustomPageImpl;
import com.glimps.glimpsserver.review.domain.Review;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
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
	public CustomPage<Review> findAllByOrder(Pageable pageRequest) {
		JPAQuery<Review> joinQuery = jpaQueryFactory.selectFrom(review)
			.fetchJoin().leftJoin(reviewPhoto)
			.on(reviewPhoto.review.id.eq(review.id))
			.fetchJoin().leftJoin(user)
			.on(user.id.eq(review.user.id))
			.fetchJoin().leftJoin(perfume)
			.on(perfume.id.eq(review.perfume.id));

		long totalElements = joinQuery.stream().count();

		List<Review> result = joinQuery
			.offset(pageRequest.getOffset())
			.limit(pageRequest.getPageSize())
			.orderBy(getSort(pageRequest))
			.stream().collect(Collectors.toList());

		return new CustomPageImpl<>(result, pageRequest.getOffset(), pageRequest.getPageSize(), totalElements);

	}

	@Override
	public CustomPage<Review> findAllByUser(Long userId, Pageable pageRequest) {
		JPAQuery<Review> joinQuery = jpaQueryFactory.selectFrom(review)
			.fetchJoin().leftJoin(reviewPhoto)
			.on(reviewPhoto.review.id.eq(review.id))
			.fetchJoin().leftJoin(user)
			.on(user.id.eq(review.user.id))
			.fetchJoin().leftJoin(perfume)
			.on(perfume.id.eq(review.perfume.id))
			.where(review.user.id.eq(userId));

		long totalElements = joinQuery.stream().count();

		List<Review> result = joinQuery
			.offset(pageRequest.getOffset())
			.limit(pageRequest.getPageSize())
			.orderBy(getSort(pageRequest))
			.stream().collect(Collectors.toList());

		return new CustomPageImpl<>(result, pageRequest.getOffset(), pageRequest.getPageSize(), totalElements);
	}

	private OrderSpecifier<?> getSort(Pageable pageRequest) {
		for (Sort.Order order : pageRequest.getSort()) {
			Order direction = order.isAscending() ? Order.ASC : Order.DESC;
			switch (order.getProperty()) {
				case "heartsCnt":
					return new OrderSpecifier<>(direction, review.heartsCnt);
				default:
					return new OrderSpecifier<>(direction, review.createdAt);
			}
		}
		return null;
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

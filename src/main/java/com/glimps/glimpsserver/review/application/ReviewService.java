package com.glimps.glimpsserver.review.application;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glimps.glimpsserver.common.domain.CustomPage;
import com.glimps.glimpsserver.common.error.EntityNotFoundException;
import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.perfume.application.PerfumeService;
import com.glimps.glimpsserver.perfume.domain.Perfume;
import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;
import com.glimps.glimpsserver.review.dto.ReviewPageParam;
import com.glimps.glimpsserver.review.infra.ReviewCustomRepository;
import com.glimps.glimpsserver.review.infra.ReviewRepository;
import com.glimps.glimpsserver.user.application.UserService;
import com.glimps.glimpsserver.user.domain.User;

@Service
@Transactional(readOnly = true)
public class ReviewService {
	private final ReviewCustomRepository reviewCustomRepository;
	private final ReviewRepository reviewRepository;
	private final UserService userService;
	private final ReviewPhotoService reviewPhotoService;
	private final PerfumeService perfumeService;
	private final ReviewHeartService reviewHeartService;

	public ReviewService(ReviewCustomRepository reviewCustomRepository, ReviewRepository reviewRepository,
		UserService userService,
		ReviewPhotoService reviewPhotoService, PerfumeService perfumeService, ReviewHeartService reviewHeartService) {
		this.reviewCustomRepository = reviewCustomRepository;
		this.reviewRepository = reviewRepository;
		this.userService = userService;
		this.reviewPhotoService = reviewPhotoService;
		this.perfumeService = perfumeService;
		this.reviewHeartService = reviewHeartService;
	}

	@Transactional
	public Review createReview(ReviewCreateRequest reviewCreateRequest, String email) {
		User user = userService.getUserByEmail(email);
		Long perfumeId = reviewCreateRequest.getPerfumeId();
		Perfume perfume = perfumeService.getPerfumeById(perfumeId);

		Review review = Review.createReview(reviewCreateRequest, user, perfume);

		perfumeService.updateRatings(perfume, reviewCreateRequest);
		reviewPhotoService.createReviewPhotos(review,
			reviewCreateRequest.getPhotoUrls());

		return reviewRepository.save(review);
	}

	public Review getReviewById(UUID uuid) {
		return reviewCustomRepository.findByUuid(uuid)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND, uuid));
	}

	public CustomPage<Review> getMyReviews(ReviewPageParam reviewPageParam, String email) {
		Integer offset = reviewPageParam.getOffset();
		if (offset == null) {
			offset = 0;
		}

		Pageable pageRequest = PageRequest.of(offset, reviewPageParam.getLimit(),
			reviewPageParam.getSortType().getDirection(), reviewPageParam.getOrderStandard().getProperty());

		User user = userService.getUserByEmail(email);

		return reviewCustomRepository.findAllByUser(user.getId(), pageRequest);
	}

	public List<Review> getRecentReviews() {
		return reviewCustomRepository.findTop10ByOrderByCreatedAtDesc();
	}

	public CustomPage<Review> getReviews(ReviewPageParam reviewPageParam) {
		Integer offset = reviewPageParam.getOffset();

		if (offset == null) {
			offset = 0;
		}

		Pageable pageRequest = PageRequest.of(offset, reviewPageParam.getLimit(),
			reviewPageParam.getSortType().getDirection(),
			reviewPageParam.getOrderStandard().getProperty());
		return reviewCustomRepository.findAllByOrder(pageRequest);
	}

	@Transactional
	public Review createHeart(UUID reviewId, String existsEmail) {
		Review review = getReviewById(reviewId);
		User user = userService.getUserByEmail(existsEmail);
		reviewHeartService.createReviewHeart(review, user);

		return review;
	}

	@Transactional
	public Review cancelHeart(UUID uuid, String email) {
		Review review = getReviewById(uuid);
		User user = userService.getUserByEmail(email);
		reviewHeartService.cancelReviewHeart(review, user);
		return review;
	}

	public List<Review> getPerfumeReviews(UUID perfumeUuid) {
		return reviewCustomRepository.findAllByPerfumeId(perfumeUuid);
	}

	public List<Review> getBestReviews(int amountOfBestReview) {
		return reviewCustomRepository.findBestReviewByAmount(amountOfBestReview);
	}
}

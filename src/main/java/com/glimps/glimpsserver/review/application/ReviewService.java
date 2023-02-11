package com.glimps.glimpsserver.review.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glimps.glimpsserver.common.error.CustomException;
import com.glimps.glimpsserver.common.error.EntityNotFoundException;
import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.perfume.application.PerfumeService;
import com.glimps.glimpsserver.perfume.domain.Perfume;
import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;
import com.glimps.glimpsserver.review.infra.ReviewRepository;
import com.glimps.glimpsserver.user.application.UserService;
import com.glimps.glimpsserver.user.domain.User;

@Service
@Transactional(readOnly = true)
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final UserService userService;
	private final ReviewPhotoService reviewPhotoService;
	private final PerfumeService perfumeService;

	public ReviewService(ReviewRepository reviewRepository, UserService userService,
		ReviewPhotoService reviewPhotoService, PerfumeService perfumeService) {
		this.reviewRepository = reviewRepository;
		this.userService = userService;
		this.reviewPhotoService = reviewPhotoService;
		this.perfumeService = perfumeService;
	}

	@Transactional
	public Review createReview(ReviewCreateRequest reviewCreateRequest, String email) {
		User user = userService.getUser(email);
		Long perfumeId = reviewCreateRequest.getPerfumeId();
		Perfume perfume = perfumeService.getPerfume(perfumeId);
		Review review = Review.createReview(reviewCreateRequest, user, perfume);

		perfumeService.updateRatings(perfume, reviewCreateRequest);
		reviewPhotoService.createReviewPhotos(review,
			reviewCreateRequest.getPhotoUrls());
		return reviewRepository.save(review);
	}

	public Review getReview(UUID id) {
		return reviewRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND, id));
	}

	public Page<Review> getMyReviews(Integer offset, Integer limit, String email) {
		if (offset == null) {
			offset = 0;
		}
		Pageable pageRequest = PageRequest.of(offset, limit);
		User user = userService.getUser(email);
		return reviewRepository.findAllByUser(user.getId(), pageRequest);
	}
}

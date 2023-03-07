package com.glimps.glimpsserver.review.presentation;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.glimps.glimpsserver.common.authentication.UserAuthentication;
import com.glimps.glimpsserver.common.domain.CustomPage;
import com.glimps.glimpsserver.review.application.ReviewService;
import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;
import com.glimps.glimpsserver.review.dto.ReviewPageParam;
import com.glimps.glimpsserver.review.dto.ReviewPageResponse;
import com.glimps.glimpsserver.review.dto.ReviewResponse;
import com.glimps.glimpsserver.review.dto.ReviewUpdateRequest;

@RestController
@CrossOrigin
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
	public ReviewResponse create(@RequestBody @Valid ReviewCreateRequest reviewCreateRequest,
		UserAuthentication userAuthentication) {
		String email = userAuthentication.getEmail();
		Review review = reviewService.createReview(reviewCreateRequest, email);
		return ReviewResponse.of(review);
	}

	@GetMapping("/{uuid}")
	public ReviewResponse detail(@PathVariable UUID uuid) {
		Review review = reviewService.getReviewById(uuid);
		return ReviewResponse.of(review);
	}

	@GetMapping("/myReviews")
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
	public List<ReviewPageResponse> myReviews(ReviewPageParam reviewPageParam, UserAuthentication userAuthentication) {
		String email = userAuthentication.getEmail();
		CustomPage<Review> reviews = reviewService.getMyReviews(reviewPageParam, email);
		return ReviewPageResponse.of(reviews);
	}

	@GetMapping("/recentReviews")
	public List<ReviewResponse> recentReviews() {
		List<Review> reviews = reviewService.getRecentReviews();
		return ReviewResponse.of(reviews);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ReviewPageResponse> list(ReviewPageParam reviewPageParam) {
		CustomPage<Review> reviews = reviewService.getReviews(reviewPageParam);
		return ReviewPageResponse.of(reviews);
	}

	@PostMapping("/{id}/heart")
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
	public ReviewResponse createHeart(@PathVariable UUID id, UserAuthentication userAuthentication) {
		String email = userAuthentication.getEmail();
		Review review = reviewService.createHeart(id, email);
		return ReviewResponse.of(review);
	}

	@DeleteMapping("/{id}/heart")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
	public ReviewResponse cancelHeart(@PathVariable UUID id, UserAuthentication userAuthentication) {
		String email = userAuthentication.getEmail();
		Review review = reviewService.cancelHeart(id, email);
		return ReviewResponse.of(review);
	}

	@GetMapping("/perfumeReviews")
	public List<ReviewResponse> perfumeReviews(@RequestParam UUID perfumeUuid) {
		List<Review> reviews = reviewService.getPerfumeReviews(perfumeUuid);
		return ReviewResponse.of(reviews);
	}

	@GetMapping("/bestReviews")
	public List<ReviewResponse> bestReviews(@RequestParam int amountOfBestReview) {
		List<Review> reviews = reviewService.getBestReviews(amountOfBestReview);
		return ReviewResponse.of(reviews);
	}

	@PatchMapping("/{uuid}")
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
	public ReviewResponse update(@PathVariable UUID uuid, @RequestBody @Valid ReviewUpdateRequest reviewUpdateRequest,
		UserAuthentication userAuthentication) {
		String email = userAuthentication.getEmail();
		Review review = reviewService.updateReview(uuid, reviewUpdateRequest, email);
		return ReviewResponse.of(review);
	}

	@DeleteMapping("/{uuid}")
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
	public void delete(@PathVariable UUID uuid, UserAuthentication userAuthentication) {
		String email = userAuthentication.getEmail();
		reviewService.deleteReview(uuid, email);
	}

}

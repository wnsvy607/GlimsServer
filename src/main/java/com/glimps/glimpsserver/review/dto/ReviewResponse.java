package com.glimps.glimpsserver.review.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.domain.ReviewPhoto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class ReviewResponse {
	private String title;
	private String body;
	private String nickname;
	@Builder.Default
	private List<String> photoUrls = new ArrayList<>();
	private String perfumeName;
	private String perfumeBrand;
	private double overallRating;
	private double longevityRating;
	private double sillageRating;

	public static ReviewResponse of(Review review) {
		return ReviewResponse.builder()
			.title(review.getTitle())
			.body(review.getBody())
			.nickname(review.getUser().getNickname())
			.photoUrls(review.getReviewPhotos().stream()
				.map(ReviewPhoto::getUrl)
				.collect(Collectors.toList()))
			.perfumeName(review.getPerfume().getPerfumeName())
			.perfumeBrand(review.getPerfume().getBrand())
			.overallRating(review.getOverallRating())
			.longevityRating(review.getLongevityRating())
			.sillageRating(review.getSillageRating())
			.build();
	}

	public static List<ReviewResponse> of(List<Review> reviews) {
		return reviews.stream()
			.map(ReviewResponse::of)
			.collect(Collectors.toList());
	}
}

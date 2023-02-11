package com.glimps.glimpsserver.review.dto;

import com.glimps.glimpsserver.review.domain.Review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Builder
public class ReviewResponse {
	private String title;
	private String body;
	private String perfumeName;
	private String perfumeBrand;
	private double overallRating;
	private double longevityRating;
	private double sillageRating;

	public static ReviewResponse of(Review review) {
		return ReviewResponse.builder()
			.title(review.getTitle())
			.body(review.getBody())
			.perfumeName(review.getPerfume().getPerfumeName())
			.perfumeBrand(review.getPerfume().getBrand())
			.overallRating(review.getOverallRating())
			.longevityRating(review.getLongevityRating())
			.sillageRating(review.getSillageRating())
			.build();
	}
}

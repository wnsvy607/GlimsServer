package com.glimps.glimpsserver.review.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.glimps.glimpsserver.review.domain.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReviewPageResponse {
	private String title;
	private String body;
	private String perfumeName;
	private String perfumeBrand;
	private double overallRating;
	private double longevityRating;
	private double sillageRating;
	private long totalElements;
	private int totalPages;

	public static List<ReviewPageResponse> of(Page<Review> reviews) {
		return reviews.stream()
			.map(review -> ReviewPageResponse.builder()
				.title(review.getTitle())
				.body(review.getBody())
				.perfumeName(review.getPerfume().getPerfumeName())
				.perfumeBrand(review.getPerfume().getBrand())
				.overallRating(review.getOverallRating())
				.longevityRating(review.getLongevityRating())
				.sillageRating(review.getSillageRating())
				.totalElements(reviews.getTotalElements())
				.totalPages(reviews.getTotalPages())
				.build())
			.collect(Collectors.toList());
	}
}

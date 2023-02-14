package com.glimps.glimpsserver.review.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReviewCreateRequest {
	@NotEmpty
	@Size(max = 15)
	private String title;

	@NotEmpty
	private String body;

	@Positive
	private double overallRatings;
	@Positive
	private double longevityRatings;
	@Positive
	private double sillageRatings;
	@NotNull
	private Long perfumeId;

	@Builder.Default
	@NotNull
	private List<String> photoUrls = new ArrayList<>();

	@Builder.Default
	@NotNull
	private List<Long> hashTagsIds = new ArrayList<>();
}

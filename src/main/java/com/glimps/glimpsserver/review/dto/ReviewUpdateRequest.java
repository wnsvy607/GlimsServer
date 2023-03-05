package com.glimps.glimpsserver.review.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewUpdateRequest {
	@NotEmpty
	@Size(max = 15)
	private String title;

	@NotEmpty
	private String body;
	@Positive
	private Double overallRatings;
	@Positive
	private Double longevityRatings;
	@Positive
	private Double sillageRatings;
	@Builder.Default
	@NotNull
	private List<String> photoUrls = Lists.newArrayList();
}

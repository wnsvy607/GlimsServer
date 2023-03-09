package com.glimps.glimpsserver.review.dto;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.glimps.glimpsserver.review.domain.Uuid;
import com.google.common.collect.Lists;

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
	@Uuid
	private UUID perfumeUuid;

	@Builder.Default
	@NotNull
	private List<String> photoUrls = Lists.newArrayList();

}

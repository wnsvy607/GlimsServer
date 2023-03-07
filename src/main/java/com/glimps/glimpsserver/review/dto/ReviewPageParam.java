package com.glimps.glimpsserver.review.dto;

import java.util.Objects;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.glimps.glimpsserver.common.domain.ReviewOrderStandard;
import com.glimps.glimpsserver.common.domain.SortType;

import lombok.Getter;

@Getter
public class ReviewPageParam {
	@NotNull
	private final int limit;
	@Enumerated(value = EnumType.STRING)
	private final ReviewOrderStandard orderStandard;
	@Enumerated(value = EnumType.STRING)
	private final SortType sortType;
	private final Integer offset;

	public ReviewPageParam(Integer offset, int limit, ReviewOrderStandard orderStandard, SortType sortType) {
		this.offset = offset;
		this.orderStandard = Objects.requireNonNullElse(orderStandard, ReviewOrderStandard.DATE);
		this.limit = limit;
		this.sortType = Objects.requireNonNullElse(sortType, SortType.DESC);

	}
}

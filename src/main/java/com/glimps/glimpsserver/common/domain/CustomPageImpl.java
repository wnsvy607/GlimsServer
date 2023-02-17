package com.glimps.glimpsserver.common.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class CustomPageImpl<T> implements CustomPage<T> {
	private final List<T> content;
	private final long totalPages;
	private final long totalElements;

	public CustomPageImpl(List<T> content, long offset, long limit, long totalElements) {
		this.content = content;
		if (content.isEmpty()) {
			this.totalPages = 0;
			this.totalElements = 0;
		} else {
			long modulo = content.size() % (limit - offset);
			this.totalPages = modulo == 0 ? content.size() / (limit - offset) : content.size() / (limit - offset) + 1;
			this.totalElements = totalElements;
		}
	}
}

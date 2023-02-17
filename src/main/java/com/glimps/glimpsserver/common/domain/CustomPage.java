package com.glimps.glimpsserver.common.domain;

import java.util.List;

public interface CustomPage<T> {

	static <T> CustomPage<T> empty() {
		return new CustomPageImpl<>(List.of(), 0, 0, 0);
	}

	List<T> getContent();

	long getTotalPages();

	long getTotalElements();
}

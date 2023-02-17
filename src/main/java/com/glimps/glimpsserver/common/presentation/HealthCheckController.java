package com.glimps.glimpsserver.common.presentation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

	@RequestMapping("/health")
	public String healthCheck() {
		return "Health is GOOD!";
	}

}

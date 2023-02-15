package com.glimps.glimpsserver.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

	@RequestMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("Health is GOOD!");
	}

}

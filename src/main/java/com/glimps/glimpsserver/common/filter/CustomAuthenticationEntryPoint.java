package com.glimps.glimpsserver.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimps.glimpsserver.common.dto.ErrorResponse;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper mapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		// response.setStatus(401);
		// PrintWriter writer = response.getWriter();
		//
		// response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		// ErrorResponse of = ErrorResponse.of("401", authException.getMessage());
		// String convertedError = mapper.writeValueAsString(of);
		//
		// writer.write(convertedError);
	}
}

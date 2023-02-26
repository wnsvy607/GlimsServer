package com.glimps.glimpsserver.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Profile({"local", "dev"})

public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")      //패턴
			.allowedOrigins("*")    //URL
			.allowedOrigins("*", "*") //URL
			.allowedHeaders("Authorization", "header2")  //header
			.allowedMethods("GET", "POST");        //method
	}


}

package com.alexstrasza.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	private static String VOICE_URI = "http://test-core-web-app:8081";

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder)
	{
		return builder.routes()
			.route(p -> p
				.path("/testing/**")
				.filters(f -> f.addRequestHeader("test", "Hello World"))
				.uri(VOICE_URI))
			.route(p -> p
				.path("/something/**")
				.filters(f -> f.addRequestHeader("something", "Hello something"))
				.uri(VOICE_URI))
			.build();
	}
}

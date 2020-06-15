package com.smilei.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	private static String AUCTIONING = "http://localhost:8081";
	private static String WEBSOCKET_URI = "ws://localhost:8082";

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder)
	{
		return builder.routes()
			.route(p -> p
			    .path("/**")
				.uri(AUCTIONING))
			.route(p -> p
				.path("/websocket/**")
				.uri(WEBSOCKET_URI))
		    .build();
	}
}

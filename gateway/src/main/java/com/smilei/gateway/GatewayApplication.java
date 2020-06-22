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

//	private static String WEBSOCKET_URI = "ws://localhost:8081";
//	private static String WEBAPI = "http://localhost:8081";
//	private static String AUTH = "http://localhost:8082";
//	private static String INVENTORY = "http://localhost:8083";
//	private static String CURRENCY = "http://localhost:8084";
//	private static String AUCTION = "http://localhost:8085";

	private static String WEBSOCKET_URI = "ws://service-webapi:8081";
	private static String WEBAPI = "http://service-webapi:8081";
	private static String AUTH = "http://service-authentication:8082";
	private static String INVENTORY = "http://service-inventory:8083";
	private static String CURRENCY = "http://service-currency:8084";
	private static String AUCTION = "http://service-auctioning:8085";


	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder)
	{
		return builder.routes()
			.route(p -> p
				.path("/users/**")
				.uri(AUTH))
			.route(p -> p
				.path("/inventory/**")
				.uri(INVENTORY))
			.route(p -> p
				.path("/currency/**")
				.uri(CURRENCY))
			.route(p -> p
				.path("/auction/**")
				.uri(AUCTION))
			.route(p -> p
				.path("/websocket/**")
				.uri(WEBSOCKET_URI))
			.route(p -> p
			    .path("/**")
				.uri(WEBAPI))
		    .build();
	}
}

package com.alexstrasza.auctioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuctioningApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctioningApplication.class, args);
	}

}

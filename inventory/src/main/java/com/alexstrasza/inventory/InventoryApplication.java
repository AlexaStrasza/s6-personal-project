package com.alexstrasza.inventory;

import com.alexstrasza.inventory.components.InventoryHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
//		InventoryHolder.GetInstance().CreatePlayerForTesting();
	}

}

package com.nexora.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Enables Spring Cloud Config Server.
 *
 * Responsibilities:
 * - Acts as a centralized configuration repository.
 * - Provides configuration properties to all microservices.
 * - Eliminates duplicate configuration across services.
 * - Supports externalized configuration management.
 *
 * Example:
 * accounts-service --> Config Server --> accounts.yml
 */

@EnableConfigServer

@SpringBootApplication
public class ConfigserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigserverApplication.class, args);
	}

}

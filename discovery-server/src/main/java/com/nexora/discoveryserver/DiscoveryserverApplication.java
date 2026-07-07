package com.nexora.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * =====================================================
 * NEXORA BANKING PLATFORM
 * =====================================================
 *
 * Service Name : Discovery Server
 * Port         : 8071
 * Layer        : Infrastructure
 *
 * Purpose:
 * --------
 * Acts as a Service Registry and Discovery mechanism
 * for all microservices within the Nexora Banking Platform.
 *
 * Why is it needed?
 * -----------------
 * In a microservices architecture, service instances may
 * start, stop, scale, or change IP addresses dynamically.
 *
 * Instead of hardcoding service URLs, services register
 * themselves with Eureka Server and discover each other
 * using logical service names.
 *
 * Responsibilities:
 * -----------------
 * - Service Registration
 * - Service Discovery
 * - Health Monitoring
 * - Instance Tracking
 * - Load Balancer Integration
 *
 * Registration Flow:
 * ------------------
 * Auth Service
 * Customer Service
 * Account Service
 * Transaction Service
 * Payment Service
 *
 *        |
 *        v
 *
 *  Eureka Discovery Server
 *
 * Service Discovery Example:
 * --------------------------
 * API Gateway -> auth-service
 *
 * Instead of:
 * http://localhost:8081
 *
 * Gateway uses:
 * lb://auth-service
 *
 * Eureka resolves the actual service instance.
 *
 * Benefits:
 * ---------
 * - Loose Coupling
 * - Dynamic Service Discovery
 * - Easier Scaling
 * - Improved Fault Tolerance
 * - Cloud Native Architecture
 *
 * =====================================================
 */

@EnableEurekaServer

@SpringBootApplication
public class DiscoveryserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryserverApplication.class, args);
	}

}

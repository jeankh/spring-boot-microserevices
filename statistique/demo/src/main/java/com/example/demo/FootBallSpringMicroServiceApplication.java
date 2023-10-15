package com.example.demo;

import com.example.demo.configuration.RibbonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableDiscoveryClient
@RibbonClient(name = "server", configuration = RibbonConfiguration.class)
public class FootBallSpringMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootBallSpringMicroServiceApplication.class, args);
	}

}

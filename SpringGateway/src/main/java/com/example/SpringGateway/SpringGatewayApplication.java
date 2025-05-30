package com.example.SpringGateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class SpringGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGatewayApplication.class, args);

		Properties properties = System.getProperties();
		for (Object key : properties.keySet()) {
			log.info("Property: {} = {}", key, System.getProperties().get(key));
		}

		String url = System.getProperty("url");
		String username = System.getProperty("username");
		String password = System.getProperty("password");

		log.info("url: {}", url);
		log.info("username: {}", username);
		log.info("password: {}", password);
	}

}

// 2025-06-04: Application 시작 시 System Property와 Active Profile 출력 추가

package com.example.SpringGateway;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

import java.util.Properties;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class SpringGatewayApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(SpringGatewayApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Properties properties = System.getProperties();
		for (Object key : properties.keySet()) {
			log.info("Property: {} = {}", key, properties.get(key));
		}

		String url = System.getProperty("url");
		String username = System.getProperty("username");
		String password = System.getProperty("password");

		log.info("url: {}", url);
		log.info("username: {}", username);
		log.info("password: {}", password);

		log.info("✅ Active Profiles: {}", String.join(", ", env.getActiveProfiles()));
	}
}

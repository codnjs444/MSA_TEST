package com.example.SpringGateway.global.security.cors;

import com.example.SpringGateway.global.security.matcher.MatcherRequestManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(MatcherRequestManager.ALLOWED_ORIGIN));
        configuration.setAllowedMethods(Arrays.asList(MatcherRequestManager.ALLOWED_METHOD));
        configuration.setAllowedHeaders(Arrays.asList(MatcherRequestManager.ALLOWED_HEADER));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh", "Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
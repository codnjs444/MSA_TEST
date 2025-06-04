package com.example.SpringGateway.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import com.example.SpringGateway.global.redis.service.RedisRefreshTokenService;
import com.example.SpringGateway.global.security.matcher.MatcherRequestManager;
import com.example.SpringGateway.global.security.point.CustomAccessDeniedHandler;
import com.example.SpringGateway.global.security.point.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    public SecurityConfig(
            CorsConfigurationSource corsConfigurationSource,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            RedisRefreshTokenService redisRefreshTokenService) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                // FormLogin 사용 X
                .formLogin(login -> login.disable())
                // httpBasic 사용
                .httpBasic(basic -> basic.disable())
                // CSRF 보호를 비활성화 (개발 환경에서만 사용)
                .csrf(csrf -> csrf.disable())
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // Header 설정
                .headers(headers -> headers
                        .contentTypeOptions(contentType -> contentType.disable())
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:"))
                        .frameOptions(frameOption -> frameOption.disable())
                )
                // 세션 사용하지 않으므로 STATELESS로 설정

                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                // exceptionHandler
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                // 모든 요청에 대해 접근 허용 (인증 여부만 제어하는 역할 : 세부 접근 허용은 WebFilter에서 제어)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(MatcherRequestManager.AUTH_WHITE_LIST).permitAll()
                        .pathMatchers(HttpMethod.TRACE).denyAll()
                        .pathMatchers(HttpMethod.OPTIONS).denyAll()
                        .anyExchange().authenticated()
                )
                 //jwt-set-uri 통해 공개키 활용 -> JWT 완전 자동 검증
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );

        return http.build();
    }
}

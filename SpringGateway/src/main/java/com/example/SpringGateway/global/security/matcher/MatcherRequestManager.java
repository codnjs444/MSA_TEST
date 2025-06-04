package com.example.SpringGateway.global.security.matcher;

/**
 * [엔드포인트 인가 관리 상수 클래스]
 *
 * Spring Security (WebFlux 기반) 또는 Gateway 환경
 * CORS 및 인증 예외 URL 관리, 공통 HTTP Header 등 상수 값 정의
 *
 * - CORS 허용 Origin, Method, Header 설정
 * - 인증 화이트리스트 (인증 없이 접근 가능한 URL)
 * - 로그아웃 경로 정의
 */
public class MatcherRequestManager {

    /** 로그아웃 처리 URL (SecurityConfig에서 logoutUrl에 사용됨) */
    public static final String LOGOUT_URL = "/auth/logout";

    /** 토큰 재발급 URL */
    public static final String REISSUE_TOKEN_URL = "/auth/refresh";

    /** ACTUATOR URL */
    public static final String ACTUATOR_URL = "/actuator/**";

    /**
     * CORS 허용 Origin 목록
     * - 개발환경과 운영환경 모두 포함
     * - 포트 지정된 localhost도 포함
     */
    public static final String[] ALLOWED_ORIGIN = {
            "http://localhost:5173",
            "http://localhost:5173/*",
            "https://piiep.kr",
            "https://eflow.piiep.kr",
            "https://ekpi.piiep.kr",
            "https://api.piiep.kr"
    };

    /**
     * CORS 허용 메서드 목록
     * - REST API에 주로 사용되는 메서드 지정
     */
    public static final String[] ALLOWED_METHOD = {
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
    };

    /**
     * 클라이언트가 보낼 수 있는 허용 헤더
     * - 사용자 정의 헤더 포함 (예: X-LANG, X-APIVERSION 등)
     * - 인증 관련 헤더 포함 (Authorization, Authorization-refresh)
     */
    public static final String[] ALLOWED_HEADER = {
            "Authorization", "Authorization-refresh", "Content-Type", "X-Requested-With",
            "X-APIVERSION", "X-LOGKEY", "X-CHANNEL", "X-VNAME", "X-LANG", "X-Frame-Options",
            "X-MID", "X-CALLTYPE", "X-APP"
    };

    /**
     * 인증 없이 접근 가능한 URL 화이트리스트
     * - 정적 리소스 또는 인증 제외 경로 지정
     * - Gateway 또는 SecurityFilterChain에서 permitAll에 사용
     */
    public static final String[] AUTH_WHITE_LIST = {
            LOGOUT_URL, REISSUE_TOKEN_URL, ACTUATOR_URL,
            // ✅ Resources 경로
            "/","/css/**","/images/**","/js/**","/favicon.ico",
            // ✅ Swagger UI 경로
            "/swagger-ui.html",
            "/swagger-ui/**",
            // ✅ OpenAPI 명세 경로 (springdoc-openapi 기준)
            "/api-docs",
            "/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api-docs/swagger-config",
            // ✅ springdoc-openapi 자동 구성 경로
            "/swagger-resources/**",
            "/swagger-config",
            // ✅ Webjars 경로 (JS/CSS 파일)
            "/webjars/**",
            // ✅ actuator
            "/actuator/**",

            // [User Service]
            // ✅ 회원가입, 중복ID 체크
            "/user/sign-up",
            "/user/checkId",

            // ✅ OTP 인증
            "/user/otp",
            "/user/certification",

            // ✅ 토큰 테스트
            "/user/token",

            // 채원 테스트
            "/ms1/**",
            "/ms2/**",
            "/user/**",
    };

}

package com.example.SpringGateway.global.log.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 로그 관련 유틸리티 클래스
 *
 * - WebFlux 기반 요청에서 메서드, URI, 클라이언트/서버 IP 추출
 * - AOP에서 전달된 메서드 파라미터 값을 JSON 형태로 문자열 변환
 * - 서버 IP는 현재 실행 중인 서버의 내부 네트워크 주소를 탐색하여 반환
 */
public class LogUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 요청의 HTTP 메서드 반환
     *
     * @param request WebFlux의 ServerHttpRequest 객체
     * @return GET, POST 등 메서드 명 (null일 경우 "UNKNOWN")
     */
    public static String getMethod(ServerHttpRequest request) {
        return request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";
    }

    /**
     * 요청의 URI 경로 반환
     *
     * @param request ServerHttpRequest
     * @return 요청된 URI 경로
     */
    public static String getURIPath(ServerHttpRequest request) {
        return request.getURI().getPath();
    }

    /**
     * 클라이언트 IP 주소 추출
     *
     * - X-FORWARDED-FOR 헤더 우선
     * - 없을 경우 실제 소켓 주소에서 IP 추출
     *
     * @param request ServerHttpRequest
     * @return 클라이언트 IP 문자열
     */
    public static String getClientIp(ServerHttpRequest request) {
        String xfHeader = request.getHeaders().getFirst("X-FORWARDED-FOR");
        if (xfHeader != null && !xfHeader.isEmpty() && !"unknown".equalsIgnoreCase(xfHeader)) {
            if (xfHeader.contains(",")) {
                xfHeader = xfHeader.split(",")[0];
            }
        } else {
            if (request.getRemoteAddress() != null) {
                xfHeader = request.getRemoteAddress().getAddress().getHostAddress();
            }
        }
        return xfHeader;
    }

    /**
     * 서버 내부 IP 주소 탐색
     *
     * @return 로컬 서버 IP 주소 (Loopback 제외)
     */
    public static String getServerIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            return "";
        }
        return "";
    }

    /**
     * AOP를 통해 전달받은 메서드 파라미터를 JSON 문자열로 변환
     *
     * @param proceedingJoinPoint AOP JoinPoint
     * @return 파라미터명=값 형태의 문자열 (JSON 직렬화)
     */
    public static String getPayload(ProceedingJoinPoint proceedingJoinPoint) {
        var signature = proceedingJoinPoint.getSignature();
        if (signature instanceof MethodSignature methodSignature) {
            var parameterNames = methodSignature.getParameterNames();
            var parameterValues = proceedingJoinPoint.getArgs();
            return IntStream.range(0, parameterNames.length)
                    .mapToObj(i -> parameterNames[i] + "=" + convertToJson(parameterValues[i]))
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    /**
     * 객체를 JSON 문자열로 변환
     *
     * @param value 변환할 객체
     * @return JSON 문자열 또는 toString 결과
     */
    public static String convertToJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }
}

package com.example.userService.global.log.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LogUtils {
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-FORWARDED-FOR");
        if (xfHeader != null && xfHeader.length() != 0 && !"unknown".equalsIgnoreCase(xfHeader)) {
            if (xfHeader.contains(",")) {
            	xfHeader = xfHeader.split(",")[0];
            }
        } else {
        	xfHeader = request.getRemoteAddr();
        }
        return xfHeader;
    }
    
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
    
	public static String convertToJson(Object value) {
		try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 중 오류 발생", e);
        }
    }
}

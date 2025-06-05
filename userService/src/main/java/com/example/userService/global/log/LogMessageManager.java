package com.example.userService.global.log;

import com.example.userService.global.log.event.LoggingEvent;
import com.example.userService.global.log.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class LogMessageManager implements ApplicationEventPublisherAware {
	
	private static final Logger log = LoggerFactory.getLogger(LogMessageManager.class);
	private static ApplicationEventPublisher eventPublisher;

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        LogMessageManager.eventPublisher = applicationEventPublisher;
    }
	
	public static void ConsoleLogInfo(HttpStatus status, String title, String message, HttpServletRequest request) {
		if (request != null) {
			log.info("[" + status.value() + "] [" + title + "] [" + message + "][Request Method : " + request.getMethod() + "][Request URI : " + request.getRequestURI() + "]");
			publishLogEvent("INFO", status, title, message, request);
		}
	}
	
	public static void ConsoleLogError(HttpStatus status, String title, String message, HttpServletRequest request) {		
		if (request != null) {
			log.error("[" + status.value() + "] [" + title + "] [" + message + "][Request Method : " + request.getMethod() + "][Request URI : " + request.getRequestURI() + "]");
			publishLogEvent("ERROR", status, title, message, request);
		}
	}
	
	public static void ConsoleLogWarn(HttpStatus status, String title, String message, HttpServletRequest request) {
		if (request != null) {
			log.warn("[" + status.value() + "] [" + title + "] [" + message + "][Request Method : " + request.getMethod() + "][Request URI : " + request.getRequestURI() + "]");
			publishLogEvent("WARN", status, title, message, request);
		}
	}	
	
	// "[" + title + "] " + message,
	private static void publishLogEvent(String type, HttpStatus status, String title, String message, HttpServletRequest request) {
		if(eventPublisher != null) {
			eventPublisher.publishEvent(
	        		new LoggingEvent(
	        				type,
	        				status.value(),
	        	            request.getMethod(),
	        	            request.getRequestURI(),
	        	            LogUtils.getServerIp(),
	        	            LogUtils.getClientIp(request)
	        	    )
	        );
		}
    }
}
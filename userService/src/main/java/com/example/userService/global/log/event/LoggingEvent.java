package com.example.userService.global.log.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoggingEvent {
	
	private final String logType;
	private final int status;
    private final String method;
    private final String apiUrl;
	//private final String payload;
    private final String serverIp;
    private final String clientIp;
    
}
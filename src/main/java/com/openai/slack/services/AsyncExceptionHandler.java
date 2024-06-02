package com.openai.slack.services;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	Logger logger = LoggerFactory.getLogger(AsyncExceptionHandler.class);
	
	@Autowired
	ExceptionNotificationService exceptionNotificationService;

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		logger.error("Unexpected asynchronous exception at : " + method.getDeclaringClass().getName() + "."
				+ method.getName(), ex);
		exceptionNotificationService.sendAsyncException(ex, method, params);	
	}
}
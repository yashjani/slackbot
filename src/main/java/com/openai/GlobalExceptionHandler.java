package com.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.openai.slack.services.ExceptionNotificationService;

@ControllerAdvice
public class GlobalExceptionHandler {

	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	ExceptionNotificationService exceptionNotificationService;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleExceptions(Exception e) {
		exceptionNotificationService.sendException(e);
		return null;
	}

}

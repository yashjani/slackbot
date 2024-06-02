package com.openai.slack.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;



@Service
public class ExceptionNotificationService {
	
	Logger logger = LoggerFactory.getLogger(ExceptionNotificationService.class);

	public void sendException(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		logger.error(exceptionAsString);
		if(exceptionAsString != null)
		sendTextMessage("***************************" + exceptionAsString.substring(0,900));
	}
	
	public void sendAsyncException(Throwable ex, Method method, Object... params) {
		StringBuilder sb = new StringBuilder("Exception Caught in Thread - " + Thread.currentThread().getName() + 
				"\nException message - " + ex.getMessage() 
				+ "\nMethod name - " + method.getName());
		for (Object param : params) {
			sb.append("Parameter value - " + param);
		}
		sendTextMessage(sb.toString());
	}
	
	public void sendTextMessage(String text) {
		try {
			MethodsClient methodsClient =  Slack.getInstance().methods();
			try {
				ChatPostMessageResponse chatPostMessageResponse = methodsClient.chatPostMessage(r -> r.token("")
						.channel("").text(text)
				);
			} catch (Exception e) {
				logger.error("replyMessage throw exception : " + e.toString());
			}
		}catch(Exception e) {
			logger.info("Exception in sendTextMessage " + e.getMessage());
		}
	}

}

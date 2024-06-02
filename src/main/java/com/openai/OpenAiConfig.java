package com.openai;

import java.time.Duration;

import javax.security.auth.login.LoginException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.openai.slack.services.AsyncExceptionHandler;
import com.theokanning.openai.service.OpenAiService;

@Configuration
@EnableAsync
public class OpenAiConfig extends AsyncConfigurerSupport {
	
	@Autowired
	private AsyncExceptionHandler asyncExceptionHandler;

	@Value("${botToken}")
	private String botToken;

	@Value("${openaiToken}")
	private String openaiToken;

	@Value("${slackBotToken}")
	private String slackBotToken;

	@Bean
	public DiscordApi javacordclient() throws LoginException {
		return new DiscordApiBuilder().setAllIntents().setToken(botToken).login().join();
	}

	@Bean
	public OpenAiService openaiClient() throws LoginException {
		Duration duration = Duration.ofSeconds(60);
		return new OpenAiService(openaiToken, duration);
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	@Nullable
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (throwable, method, obj) -> {
			asyncExceptionHandler.handleUncaughtException(throwable, method, obj);
		};
	}

}

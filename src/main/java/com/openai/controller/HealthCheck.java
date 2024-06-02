package com.openai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.eventlistner.EventService;
import com.openai.services.OpenAiMessageCreation;
import com.openai.slack.event.dto.EventPayload;
import com.openai.slack.services.SlackMessageServices;
import com.openai.slack.services.SlackUserService;
import com.slack.api.Slack;

@RestController
public class HealthCheck {

	Logger logger = LoggerFactory.getLogger(HealthCheck.class);

	@Autowired
	SlackUserService slackUserService;

	@Autowired
	SlackMessageServices slackMessageService;

	@Autowired
	OpenAiMessageCreation openAiMessageCreation;

	@GetMapping(path = "/dhbh1387/healthcheck")
	public String healthCheck() {
		return "Success";
	}



}

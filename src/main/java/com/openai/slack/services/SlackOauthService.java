package com.openai.slack.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.openai.entity.SlackOauthModal;
import com.openai.repository.SlackOauthResponseRepository;
import com.openai.slack.event.dto.SlackOAuthResponse;
import com.openai.utils.SlackDTOConvertor;

@Service
public class SlackOauthService {
	
	Logger logger = LoggerFactory.getLogger(SlackOauthService.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${slackClientId}")
	private String slackClientId;

	@Value("${slackClientSecret}")
	private String slackClientSecret;

	@Autowired
	SlackOauthResponseRepository oauthResponseRepository;
	
	public String saveUserAccessToken(String code) {
		logger.info("saveUserAccessToken start: code {}" , code);
		String url = "https://slack.com/api/oauth.v2.access";
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("code", code);
		body.add("client_id", slackClientId);
		body.add("client_secret", slackClientSecret);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		SlackOAuthResponse response = restTemplate.postForObject(url, requestEntity, SlackOAuthResponse.class);
		SlackOauthModal slackUser =  SlackDTOConvertor.convertSlackOauthResponseToSlackUser(response);
		oauthResponseRepository.save(slackUser);
		logger.info("saveUserAccessToken end: slackUser {}" , slackUser.toString());
		return "https://app.slack.com/client/"+ slackUser.getTeamId() + "/" + slackUser.getAppId();
	}
		
	public SlackOauthModal getUserAccessToken(String teamId) {
		logger.info("getUserAccessToken start: teamId {}" , teamId);
		SlackOauthModal slackOauth = oauthResponseRepository.findByTeamId(teamId);
		logger.info("getUserAccessToken end");
		return slackOauth;		
	}

}

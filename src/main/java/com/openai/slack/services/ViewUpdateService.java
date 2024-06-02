package com.openai.slack.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.openai.repository.SlackViewRepository;

@Service
public class ViewUpdateService {
	Logger logger = LoggerFactory.getLogger(ViewUpdateService.class);

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	SlackViewRepository slackViewRepository;

	@Value("${slackAppToken}")
	private String slackAppToken;

	// View for user whoes not primary
	public void updateViewForUserWithoutPrimeAccess(String viewId, String userId, String hashId, String token) throws IOException {
		logger.info("updateViewForUserWithoutPrimeAccess staretd with viewId {} userId {} hash {}", viewId, userId,
				hashId);
		String jsonPayload = slackViewRepository.findById("userwithnoprimeaccess").get().getView();
		jsonPayload = jsonPayload.replace("viewId", viewId).replace("hashId", hashId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
		String response = restTemplate.postForObject(
				"https://slack.com/api/views.update?user_id={userId}&view_id={viewId}", request, String.class, userId,
				viewId);
		logger.info("updateViewForUserWithoutPrimeAccess completed with responnse {} ", response);
	}

	public void openManageUserModal(String triggerId, int totalcountOfLicenses, String intiallySelectedUsers, String token)
			 {
		logger.info("openManageUserModal staretd with triggerId {} totalcountOfLicenses {} intiallySelectedUsers {}",
				triggerId, totalcountOfLicenses, intiallySelectedUsers);
		String jsonPayload = slackViewRepository.findById("userSelection").get().getView();
		jsonPayload = jsonPayload.replace("triggerId", triggerId)
				.replace("\"totalcountOfLicenses\"", totalcountOfLicenses + "")
				.replace("intialUsersId", intiallySelectedUsers);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
		String response = restTemplate.postForObject("https://slack.com/api/views.open", request, String.class);
		logger.info("pushViewFirstTime completed with response {} ", response);
	}
	
	public void openFeedbackModal(String userId, String triggerId, String viewName,  String token) {
		logger.info("codeViewModal start: triggerId {}",
				triggerId);
		String jsonPayload = slackViewRepository.findById(viewName).get().getView();
		jsonPayload = jsonPayload.replace("triggerId", triggerId).replace("userId", userId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
		String response = restTemplate.postForObject("https://slack.com/api/views.open", request, String.class);
		logger.info("codeViewModal end: response {} ", response);
	}
	
	public void codeViewModal(String triggerId, String viewName,  String token) {
		logger.info("codeViewModal start: triggerId {}",
				triggerId);
		String jsonPayload = slackViewRepository.findById(viewName).get().getView();
		jsonPayload = jsonPayload.replace("triggerId", triggerId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
		String response = restTemplate.postForObject("https://slack.com/api/views.open", request, String.class);
		logger.info("codeViewModal end: response {} ", response);
	}
}

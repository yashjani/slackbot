package com.openai.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

@SuppressWarnings("deprecation")
@Service
public class CustomeOpenAIClient {

	@Value("${openaiToken}")
	private String openaiToken;

	@SuppressWarnings("serial")
	public String processRequest(String prompt, String input, String fileUrl) {
		String apiKey = openaiToken;// Replace with your actual OpenAI API key
		String endpoint = "https://api.openai.com/v1/chat/completions";
		String result = "";
		// Construct the JSON payload
		Map<String, Object> requestData = new HashMap<>();
		requestData.put("model", "gpt-4o");

		Map<String, Object> systemMessage = new HashMap<>();
		systemMessage.put("role", "system");
		systemMessage.put("content", new Object[] { new HashMap<String, Object>() {
			{
				put("type", "text");
				put("text", prompt);
			}
		} });

		Map<String, Object> userMessage = new HashMap<>();
		userMessage.put("role", "user");
		if (fileUrl != null && !fileUrl.isEmpty()) {
			userMessage.put("content", new Object[] { new HashMap<String, Object>() {
				{
					put("type", "image_url");
					put("image_url", new HashMap<String, String>() {
						{
							put("url", fileUrl);
						}
					});
				}
			} });
		}

		if (input != null && !input.isEmpty()) {
			userMessage.put("content", new Object[] { new HashMap<String, Object>() {
				{
					put("type", "text");
					put("text", input);
				}
			} });
		}

		requestData.put("messages", new Object[] { systemMessage, userMessage });
		requestData.put("temperature", 1);
		requestData.put("max_tokens", 256);
		requestData.put("top_p", 1);
		requestData.put("frequency_penalty", 0);
		requestData.put("presence_penalty", 0);

		@SuppressWarnings("deprecation")
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

		try {
			HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(endpoint),
					new JsonHttpContent(jsonFactory, requestData));
			request.getHeaders().setContentType("application/json");
			request.getHeaders().setAuthorization("Bearer " + apiKey);

			HttpResponse response = request.execute();
			String responseBody = response.parseAsString();

			// Parse the response and extract the content
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			result = rootNode.path("choices").path(0).path("message").path("content").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}

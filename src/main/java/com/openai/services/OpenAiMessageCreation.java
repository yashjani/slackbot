package com.openai.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.openai.entity.OpenAiModal;
import com.openai.repository.OpenAiModalRepository;
import com.openai.slack.event.dto.SlideData;
import com.openai.utils.Constants;
import com.openai.utils.FileTypeChecker;
import com.openai.utils.FileTypeChecker.FileType;
import com.openai.utils.VideoToAudioConverter;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.Usage;
import com.theokanning.openai.assistants.ModifyAssistantRequest;
import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.CreateImageVariationRequest;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.Run;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.threads.ThreadRequest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import serpapi.GoogleSearch;
import serpapi.SerpApiSearchException;

@Service
public class OpenAiMessageCreation {

	Logger logger = LoggerFactory.getLogger(OpenAiMessageCreation.class);

	@Autowired
	OpenAiService openAiService;

	@Autowired
	OpenAiModalRepository aiModalRepository;
	


	static String token = "";
	private static final String API_KEY = "";
	private static final String BASE_URL = "https://www.alphavantage.co/query";

	public String processGPT4Request(String input, String userId, String assistantId, boolean isStopRequired,
			List<String> fileUrls) {

		ThreadRequest threadRequest = ThreadRequest.builder().build();
		com.theokanning.openai.threads.Thread thread = openAiService.createThread(threadRequest);
		com.theokanning.openai.file.File file = null;
		List<String> fileList = new ArrayList<>();
		if (fileUrls != null && !fileUrls.isEmpty()) {
			for (String fileUrl : fileUrls) {
				try {
					String filePath = downloadFile(fileUrl);
					FileType fileType = FileTypeChecker.getFileType(filePath);
					switch (fileType) {
					case IMAGE:
						if(!Constants.Laptop_Check_Assistant_ID.equals(assistantId))
						assistantId = "";
						file = openAiService.uploadFile("assistants", filePath);
						break;
					case VIDEO:
						String path = "";
						VideoToAudioConverter.convertMp4ToMp3(filePath, path);
						input = CreateSpeeach2Text("", null, path);
						file = openAiService.uploadFile("assistants", filePath);
						break;
					case AUDIO:
						String path2 = "";
						VideoToAudioConverter.convertMp4ToMp3(filePath, path2);
						input = CreateSpeeach2Text("", null, path2);
						file = openAiService.uploadFile("assistants", filePath);
					case UNKNOWN:
						file = openAiService.uploadFile("assistants", filePath);
						break;
					}

					if (file != null)
						fileList.add(file.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		MessageRequest messageRequest = MessageRequest.builder().content(input).fileIds(fileList).build();

		openAiService.createMessage(thread.getId(), messageRequest);

		RunCreateRequest runCreateRequest = RunCreateRequest.builder().assistantId(assistantId).model("gpt-4-turbo").build();

		Run run = openAiService.createRun(thread.getId(), runCreateRequest);

		Run retrievedRun;
		do {
			retrievedRun = openAiService.retrieveRun(thread.getId(), run.getId());
		} while (!(retrievedRun.getStatus().equals("completed")) && !(retrievedRun.getStatus().equals("failed")));

		OpenAiResponse<Message> response = openAiService.listMessages(thread.getId());
	
		List<Message> messages = response.getData();

		return messages.get(0).getContent().get(0).getText().getValue();

	}
	
	public String CreateTextToImage(String prompt) throws IOException {
		CreateImageRequest request = CreateImageRequest.builder().model("dall-e-3").prompt(prompt).build();
		return downloadFileOpenAi(openAiService.createImage(request).getData().get(0).getUrl());
	}

	public String CreateImageVariation(String fileUrl) throws IOException {
		String path = downloadFile(fileUrl);
		CreateImageVariationRequest request = CreateImageVariationRequest.builder().size("1024x1024")
				.responseFormat("url").build();
		return downloadFileOpenAi(openAiService.createImageVariation(request, path).getData().get(0).getUrl());
	}

	public String updateAssistant(String sector) {
		String template = "You are a Slack bot named Wicebot who has tremendous knowledge on {{placeholder}} and only answer questions about {{placeholder}}.";
		String result = template.replace("{{placeholder}}", sector.replace("_", " "));
		ModifyAssistantRequest modifyAssistantRequest = ModifyAssistantRequest.builder().instructions(result).build();
		openAiService.modifyAssistant(Constants.Mention_Assistant_ID, modifyAssistantRequest);
		return "";
	}

	public String CreateText2Speech(String input) throws IOException {
		CreateSpeechRequest request = CreateSpeechRequest.builder().model("tts-1").voice("onyx").input(input)
				.responseFormat("mp3").build();
		// Execute the request
		ResponseBody response = openAiService.createSpeech(request);

		// Get the input stream of the response
		InputStream inputStream = response.byteStream();
		// Get the file name from the URL
		String fileName = Math.random() + ".mp3";
		Path localPath = Paths.get(".", fileName);

		// Create directories if they do not exist
		Files.createDirectories(localPath.getParent());
		// Write the input stream to a file
		try (FileOutputStream fileOutputStream = new FileOutputStream(localPath.toString())) {
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, bytesRead);
			}
		}
		return localPath.toAbsolutePath().normalize().toString();

	}

	public String CreateSpeeach2Text(String input, String fileUrl, String filePath) throws IOException {
		
		String path = filePath ;
		if(filePath == null || filePath.isEmpty())
			path = downloadFile(fileUrl);
		CreateTranscriptionRequest request = CreateTranscriptionRequest.builder().model("whisper-1").prompt(input)
				.responseFormat("text").build();
		return openAiService.createTranscription(request, path).getText();
	}

	public List<SlideData> pptDetails(String input) throws JsonMappingException, JsonProcessingException {
		
		String jsonResponse = processGPT4Request(input, "", "",false, null);
		  // Extract JSON from the response (assuming response is formatted with a preamble)
        int jsonStart = jsonResponse.indexOf("[");
        int jsonEnd = jsonResponse.lastIndexOf("]") + 1;
        String jsonContent = jsonResponse.substring(jsonStart, jsonEnd);

        // Parse the JSON content to a list of SlideData objects
        ObjectMapper objectMapper = new ObjectMapper();
        List<SlideData> slides = objectMapper.readValue(jsonContent, objectMapper.getTypeFactory().constructCollectionType(List.class, SlideData.class));
        return slides;
	}

	public String downloadFile(String fileUrl) throws IOException {
		OkHttpClient client = new OkHttpClient();

		// Build the request with authorization header
		Request request = new Request.Builder().url(fileUrl).addHeader("Authorization", "Bearer " + token).build();

		// Execute the request
		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}

			// Get the input stream of the response
			InputStream inputStream = response.body().byteStream();
			// Get the file name from the URL
			String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
			Path localPath = Paths.get(".", fileName);

			// Create directories if they do not exist
			Files.createDirectories(localPath.getParent());
			// Write the input stream to a file
			try (FileOutputStream fileOutputStream = new FileOutputStream(localPath.toString())) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, bytesRead);
				}
				return localPath.toAbsolutePath().normalize().toString();
			}
		}
	}

	public String downloadFileOpenAi(String fileUrl) throws IOException {
		OkHttpClient client = new OkHttpClient();

		// Build the request with authorization header
		Request request = new Request.Builder().url(fileUrl).build();

		// Execute the request
		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}

			// Get the input stream of the response
			InputStream inputStream = response.body().byteStream();
			// Get the file name from the URL
			String fileName = Math.random() + ".jpeg";
			Path localPath = Paths.get(".", fileName);

			// Create directories if they do not exist
			Files.createDirectories(localPath.getParent());
			// Write the input stream to a file
			try (FileOutputStream fileOutputStream = new FileOutputStream(localPath.toString())) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, bytesRead);
				}
				return localPath.toAbsolutePath().normalize().toString();
			}
		}
	}

	public String liveSearch(String query) {
		Map<String, String> parameter = new HashMap<>();

		parameter.put("q", query);
		parameter.put("location", "Austin, Texas, United States");
		parameter.put("hl", "en");
		parameter.put("gl", "us");
		parameter.put("google_domain", "google.com");
		parameter.put("api_key", "");

		GoogleSearch search = new GoogleSearch(parameter);

		try {
			JsonObject results = search.getJson();
			return results.toString();
		} catch (SerpApiSearchException ex) {
			System.out.println(ex.toString());
		}
		return "";
	}

	public String getStockData(String symbol) throws IOException {
		OkHttpClient client = new OkHttpClient();

		String url = BASE_URL + "?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=5min&apikey=" + API_KEY;

		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			String responseBody = response.body().string();
			return JsonParser.parseString(responseBody).getAsJsonObject().toString();
		}
	}

	public String getStockAnalysis(String symbol) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String url = BASE_URL + "?function=NEWS_SENTIMENT&tickers=" + symbol + "&interval=5min&apikey=" + API_KEY;

		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			String responseBody = response.body().string();
			System.out.println(responseBody);
			return JsonParser.parseString(responseBody).getAsJsonObject().toString();
		}
	}
}

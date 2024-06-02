package com.openai.slack.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.repository.SlackViewRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsCreateRequest;
import com.slack.api.methods.request.conversations.ConversationsHistoryRequest;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.conversations.ConversationsRepliesRequest;
import com.slack.api.methods.request.files.FilesUploadV2Request;
import com.slack.api.methods.response.chat.ChatGetPermalinkResponse;
import com.slack.api.methods.response.chat.ChatPostEphemeralResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsCreateResponse;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.conversations.ConversationsRepliesResponse;
import com.slack.api.methods.response.files.FilesUploadV2Response;
import com.slack.api.model.Conversation;
import com.slack.api.model.ConversationType;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.theokanning.openai.completion.chat.ChatMessage;

@Service
public class SlackMessageServices {
	Logger logger = LoggerFactory.getLogger(SlackMessageServices.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	SlackViewRepository slackViewRepository;

	public void replyMessage(String requestType, String userId, String channelId, String ts,
			MethodsClient methodsClient, String output, String token) {
		logger.info("replyMessage started with channelId {} ts {} output {}", channelId, ts, output);
		try {
			ChatPostMessageResponse chatPostMessageResponse = methodsClient
					.chatPostMessage(r -> r.token(token).channel(channelId).threadTs(ts).text(output));
			if (chatPostMessageResponse.isOk()) {
				logger.info("replyMessage completed");
			}
		} catch (Exception e) {
			logger.error("replyMessage throw exception : " + e.toString());
		}
	}

	public void postMessage(String requestType, String userId, String channelId, MethodsClient methodsClient,
			String output, String token) {
		logger.info("replyMessage started with channelId {} output {}", channelId, output);
		try {
			List<LayoutBlock> blocks = createBlocks(output);
			ChatPostMessageResponse chatPostMessageResponse = methodsClient
					.chatPostMessage(r -> r.token(token).channel(channelId).blocks(blocks));

			if (chatPostMessageResponse.isOk()) {
				logger.info("replyMessage completed");
			}
		} catch (Exception e) {
			logger.error("replyMessage threw exception: " + e.toString());
		}
	}

	private List<LayoutBlock> createBlocks(String output) {
		return Arrays.asList(SectionBlock.builder().text(MarkdownTextObject.builder().text(output).build()).build());
	}

	public String getDmChannelId(MethodsClient client, String userId, String token)
			throws IOException, SlackApiException {
		List<ConversationType> types = Arrays.asList(ConversationType.IM);

		ConversationsListResponse response = client
				.conversationsList(ConversationsListRequest.builder().token(token).types(types).build());

		if (response.isOk() && response.getChannels() != null) {
			Optional<Conversation> dmChannel = response.getChannels().stream()
					.filter(channel -> channel.getUser().equals(userId)).findFirst();

			return dmChannel.map(Conversation::getId).orElse(null);
		}
		return "";
	}

	public void postFile(String title, String prompt, String channelId, MethodsClient client, String path,
			String token) {
		File file = new File(path);

		try {
			FilesUploadV2Request request = FilesUploadV2Request.builder().token(token).channel(channelId).file(file)
					.filename(file.getName()).title(title).initialComment(prompt).build();

			FilesUploadV2Response response = client.filesUploadV2(request);

			if (response.isOk()) {
				System.out.println("File uploaded successfully.");
			} else {
				System.err.println("Error uploading file: " + response.getError());
			}
		} catch (IOException | SlackApiException e) {
			e.printStackTrace();
		}
	}

	public void postEphemeralMessage(String requestType, String channelId, MethodsClient methodsClient, String output,
			String userId, String token) {
		logger.info("replyMessage started with channelId {} output {}", channelId, output);
		try {
			ChatPostEphemeralResponse chatPostMessageResponse = methodsClient
					.chatPostEphemeral(r -> r.token(token).channel(channelId).user(userId).text(output));
			if (chatPostMessageResponse.isOk()) {
				logger.info("replyMessage completed");
			}
		} catch (Exception e) {
			logger.error("replyMessage throw exception : " + e.toString());
		}
	}

	public void postEphemeralBlock(String requestType, String channelId, MethodsClient methodsClient, String output,
			String userId, String token) {
		logger.info("replyMessage started with channelIs {} output {}", channelId, output);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = null;
		try {
			actualObj = mapper.readTree(output);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String blocks = actualObj.get("blocks").toString();
		try {
			ChatPostEphemeralResponse chatPostEphemeralResponse = methodsClient
					.chatPostEphemeral(r -> r.token(token).channel(channelId).user(userId).blocksAsString(blocks));
			if (chatPostEphemeralResponse.isOk()) {
				logger.info("replyMessage completed");
			}
		} catch (Exception e) {
			logger.error("replyMessage throw exception : " + e.toString());
		}
	}

	public void replyEphemeralMessage(String requestType, String channelId, String ts, MethodsClient methodsClient,
			String output, String userId, String token) {
		logger.info("replyMessage started with channelId {} ts {} output {}", channelId, ts, output);
		try {
			ChatPostEphemeralResponse chatPostEphemeralResponse = methodsClient
					.chatPostEphemeral(r -> r.token(token).channel(channelId).threadTs(ts).text(output).user(userId));
			if (chatPostEphemeralResponse.isOk()) {
				logger.info("replyMessage completed");
			}
		} catch (Exception e) {
			logger.error("replyMessage throw exception : " + e.toString());
		}
	}

	public void dmMessage(String userId, MethodsClient methodsClient, String output, String token) {
		logger.info("dmMessage start: userId {}", userId);
		try {
			methodsClient.chatPostMessage(r -> r.token(token).channel(userId).text(output));
		} catch (Exception e) {
			logger.error("dmMessage throws exception : " + e.toString());
		}
		logger.info("dmMessage end");
	}

	public String getPermalink(String channelId, String ts, MethodsClient methodsClient, String token) {
		logger.info("getPermalink started with channelId {} ts {}", channelId, ts);
		ChatGetPermalinkResponse response = null;
		try {
			response = methodsClient.chatGetPermalink(r -> r.token(token).channel(channelId).messageTs(ts));
			return response.getPermalink();
		} catch (Exception e) {
			logger.error("dmMessage throws exception : " + e.toString());
		}
		return "";
	}

	public List<ChatMessage> getDmMessages(String channelId, String ts, MethodsClient methodsClient, String botId,
			String token) {
		logger.info("getDmMessages started with ts {} , conversationId {} botId {}", ts, channelId, botId);
		List<ChatMessage> chat = new ArrayList<>();
		ConversationsHistoryRequest historyRequest = ConversationsHistoryRequest.builder().token(token)
				.channel(channelId).limit(10).build();
		ConversationsHistoryResponse historyResponse = null;
		try {
			historyResponse = methodsClient.conversationsHistory(historyRequest);
			List<com.slack.api.model.Message> messages = historyResponse.getMessages();
			Collections.reverse(messages);
			if (messages != null) {
				ChatMessage chatMessage = null;
				for (com.slack.api.model.Message message : messages) {
					chatMessage = new ChatMessage();
					chatMessage.setRole(message.getBotId() == null ? "user" : "assistant");
					chatMessage.setContent(message.getText());
					chat.add(chatMessage);
				}
			}
			logger.info("getDmMessages completed with ts {} , channelId {} botId {}", ts, channelId, botId);
		} catch (Exception e) {
			logger.error("getDmMessages throws exception : " + e.toString());
		}
		return chat;
	}

	public List<com.slack.api.model.Message> getThreadMessages(String channelId, String ts, MethodsClient methodsClient,
			String input, String token) {
		logger.info("getThreadMessages started with ts {} , conversationId {} input {}", ts, channelId, input);
		ConversationsRepliesRequest builder = ConversationsRepliesRequest.builder().token(token).channel(channelId)
				.ts(ts).build();
		ConversationsRepliesResponse response = null;
		try {
			response = methodsClient.conversationsReplies(builder);
			return response.getMessages();
		} catch (Exception e) {
			logger.error("getThreadMessages throws exception : " + e.toString());
		}
		return null;
	}

	public com.slack.api.model.Message getMessageByTimestamp(String channelId, String ts, MethodsClient methodsClient,
			String token) {
		logger.info("getMessageByTimestamp started with ts {} , channelId {}", ts, channelId);

		ConversationsHistoryRequest request = ConversationsHistoryRequest.builder().token(token).channel(channelId)
				.inclusive(true).latest(ts).limit(1).build();

		ConversationsHistoryResponse response;
		try {
			response = methodsClient.conversationsHistory(request);
			if (response.isOk() && response.getMessages() != null && !response.getMessages().isEmpty()) {
				return response.getMessages().get(0);
			} else {
				logger.error("Error retrieving message: " + response.getError());
			}
		} catch (IOException | SlackApiException e) {
			logger.error("getMessageByTimestamp throws exception: " + e.toString());
		}

		return null;
	}

	public List<com.slack.api.model.Message> getMessagesWithTimeLimit(String channelId, String oldest,
			MethodsClient methodsClient, int limit, String token) {
		logger.info("getMessagesWithTimeLimit started with channelId {} , oldest {} limit {}", channelId, oldest,
				limit);
		ConversationsHistoryRequest historyRequest = ConversationsHistoryRequest.builder().token(token)
				.channel(channelId).oldest(oldest).limit(limit).build();
		ConversationsHistoryResponse historyResponse = null;
		try {
			historyResponse = methodsClient.conversationsHistory(historyRequest);
			return historyResponse.getMessages();
		} catch (Exception e) {
			logger.error("getThreadMessages throws exception : " + e.toString());
		}
		return null;
	}

	public void createChannel(MethodsClient client, String channelName, String token)
			throws IOException, SlackApiException {
		Slack slack = Slack.getInstance();

		ConversationsCreateRequest request = ConversationsCreateRequest.builder().token(token).name(channelName)
				.isPrivate(false) // Set to true if you want to create a private channel
				.build();

		ConversationsCreateResponse response = slack.methods().conversationsCreate(request);

		if (response.isOk()) {
			System.out.println("Channel created successfully: " + response.getChannel().getId());
		} else {
			System.err.println("Error creating channel: " + response.getError());
		}
	}

	// Not in use
	public String xyz(String responseUrl, String output, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String info = "{\"response_type\": \"in_channel\", \"replace_original\": \"false\", \"text\" : \" yo\" }";

		headers.setBearerAuth(token);
		HttpEntity<String> request = new HttpEntity<>(info, headers);
		ResponseEntity<String> result = restTemplate.exchange(responseUrl, HttpMethod.POST, request, String.class);
		return "";
	}

}

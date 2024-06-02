package com.openai.eventlistner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openai.services.OpenAiMessageCreation;
import com.openai.slack.services.SlackMessageServices;
import com.openai.slack.services.SlackUserService;
import com.openai.slack.services.ViewUpdateService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

@Service
public class EventService {

	Logger logger = LoggerFactory.getLogger(EventService.class);

	@Autowired
	DiscordApi discordApi;

	@Autowired
	ViewUpdateService viewUpdateService;

	@Autowired
	SlackUserService slackUserService;

	@Autowired
	OpenAiMessageCreation openAiMessageCreation;

	@Autowired
	SlackMessageServices slackMessageServices;


	String mood = "The following is a conversation with an AI Cognitron. Cognitron is helpful, creative, clever, and very friendly.\n\nHuman: Hello, who are you?\nCognitron: I am an Cognitron created by OpenAI. How can I help you today?\nHuman: ";

	public void registerEvents() throws Exception {

		discordApi.addMessageCreateListener(event -> {
			TextChannel channel = null;
			List<CompletionChoice> result = null;
			if (!event.getMessage().getUserAuthor().get().isBot()) {
				try {
					String text = event.getMessageContent();
					channel = event.getChannel();
					MessageSet messages = channel.getMessages(1000).get();
					Map<String, String> map = new HashMap<>();
					for (Message message : messages) {
						if (message.getUserAuthor().get().isBot()) {
							map.put("AI assistant:", message.getContent());
						} else {
							map.put("Human:", message.getContent());
						}
					}
					String userId = event.getMessage().getAuthor().getIdAsString();
					CompletionRequest completionRequest = CompletionRequest.builder()
							.prompt(mood + "\n\n" + map.toString()).temperature(0.3).maxTokens(500).topP(1.0)
							.model("text-davinci-003").echo(true).user(userId).build();
					// result = openAiService.createCompletion(completionRequest).getChoices();
				} catch (Exception e) {
					System.out.println("Ho");
				} finally {
					String output = result.get(result.size() - 1).getText();
					output = output.substring(output.lastIndexOf("}") + 1, output.length()).trim();
					channel.sendMessage(output);
				}
			}
		});

	}
}

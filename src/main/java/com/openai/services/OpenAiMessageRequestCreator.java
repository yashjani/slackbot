package com.openai.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openai.entity.TextRequestQueue;
import com.openai.repository.OpenAiModalRepository;
import com.openai.repository.TextRequestQueueRepository;
import com.openai.utils.Constants;
import com.openai.utils.SlackDTOConvertor;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

@Service
public class OpenAiMessageRequestCreator {

	Logger logger = LoggerFactory.getLogger(OpenAiMessageCreation.class);

	@Autowired
	OpenAiService openAiService;

	@Autowired
	OpenAiModalRepository aiModalRepository;

	@Autowired
	TextRequestQueueRepository queueRepository;

	public List<ChatMessage>  writeCode(String teamId, String userId, String channelId, String toLang, String instruction,
			String token) {
		logger.info("teamI: {} userId: {} channelId: {} toLang: {} instruction {} token {}", teamId, userId, channelId,
				toLang, instruction, token);
		List<ChatMessage> chatMessages = new ArrayList<>();
		chatMessages.add(new ChatMessage("system","The following is a conversation with an AI programming assistant. The assistant is helpful, creative, clever, very friendly and only response to coding question."));
		String prompt = "Write code for following instruction in " + toLang + " language\n" + instruction;
		prompt = prompt.trim();
		logger.info("input to open a Modal: {} ", prompt);
		chatMessages.add(new ChatMessage("user",prompt));	
		return chatMessages;
	}
	
	public String  writeCodeGPT4(String teamId, String userId, String channelId, String toLang, String instruction,
			String token) {
		logger.info("teamI: {} userId: {} channelId: {} toLang: {} instruction {} token {}", teamId, userId, channelId,
				toLang, instruction, token);
		String prompt = "Write code for following instruction in " + toLang + " language\n" + instruction;
		prompt = prompt.trim();
		return prompt;
	}

	public List<ChatMessage> transLateCode(String teamId, String userId, String channelId, String fromLang,
			String toLang, String code, String token) {
		logger.info("teamId: {} userId: {} channelId: {} fromLang {} toLang: {} code {} token {}", teamId, userId,
				channelId, fromLang, toLang, code, token);
		List<ChatMessage> chatMessages = new ArrayList<>();
		chatMessages.add(new ChatMessage("system","The following is a conversation with an AI programming assistant. The assistant is helpful, creative, clever, very friendly and only response to coding question."));
		String prompt = "Translate below code from from_lang into to_lang\n"
				+ "    codedetails";
		prompt = prompt.replaceAll("from_lang", fromLang).replaceAll("to_lang", toLang).replace("codedetails", code);
		chatMessages.add(new ChatMessage("user", prompt));
		logger.info("input to open a Modal: {} ", prompt);
		return chatMessages;
	}
	
	public String  transLateCodeGPT4(String teamId, String userId, String channelId, String fromLang,
			String toLang, String code, String token) {
		String prompt = "Translate below code from from_lang into to_lang\n"
				+ "    codedetails";
		prompt = prompt.replaceAll("from_lang", fromLang).replaceAll("to_lang", toLang).replace("codedetails", code);
		prompt = prompt.trim();
		return prompt;
	}
	
	
	public String  translateTextGPT4(String teamId, String userId, String channelId, String fromLang,
			String toLang, String code, String token) {
		String prompt = "Translate below text from from_lang into to_lang. Only give translated text in result no other words.";
		prompt = prompt.replaceAll("from_lang", fromLang).replaceAll("to_lang", toLang).replace("codedetails", code);
		prompt = prompt.trim();
		return prompt;
	}

}

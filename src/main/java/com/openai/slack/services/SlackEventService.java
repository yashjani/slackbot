package com.openai.slack.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openai.entity.SlackOauthModal;
import com.openai.entity.SlackUser;
import com.openai.entity.SlackView;
import com.openai.repository.SlackViewRepository;
import com.openai.repository.TextRequestQueueRepository;
import com.openai.services.CustomeOpenAIClient;
import com.openai.services.OpenAiMessageCreation;
import com.openai.services.OpenAiMessageRequestCreator;
import com.openai.services.PowerPointCreator;
import com.openai.slack.event.dto.Action;
import com.openai.slack.event.dto.EventPayload;
import com.openai.slack.event.dto.Files;
import com.openai.slack.event.dto.SlideData;
import com.openai.slack.event.dto.View;
import com.openai.utils.Constants;
import com.openai.utils.SlackUtils;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Message;
import com.theokanning.openai.completion.chat.ChatMessage;

@Service
public class SlackEventService {

	Logger logger = LoggerFactory.getLogger(SlackEventService.class);

	@Autowired
	SlackUserService slackUserService;

	@Autowired
	SlackMessageServices slackMessageService;

	@Autowired
	OpenAiMessageCreation openAiMessageCreation;

	@Autowired
	SlackOauthService slackOauthService;

	@Autowired
	ViewUpdateService viewUpdateService;

	@Autowired
	SlackViewRepository slackViewRepository;

	@Autowired
	OpenAiMessageRequestCreator openAiMessageRequestCreator;

	@Autowired
	TextRequestQueueRepository textRequestQueueRepository;

	@Autowired
	PowerPointCreator pointCreator;
	
	@Autowired
	CustomeOpenAIClient customeOpenAIClient;

	Slack slack = Slack.getInstance();

	static String token = "";

	@Async
	public void mentionEvent(EventPayload eventPayload) throws Exception {
		String teamId = eventPayload.getTeam_id();
		String userId = eventPayload.getEvent().getUser();
		String channelId = eventPayload.getEvent().getChannel();
		String ts = eventPayload.getEvent().getEvent_ts();
		try {
			logger.info("mentionEvent start: event {}", eventPayload);
			String input = eventPayload.getEvent().getText()
					.substring(eventPayload.getEvent().getText().indexOf(">") + 1);
			String fileUrl = findFileById(eventPayload.getEvent().getFiles());
			if (input == null || input.isEmpty()) {
				// send basic reponse
				return;
			}

			String output = "";
			input = input.trim() + "\n";
			List<String> fileUrls = new ArrayList<>();
			fileUrls.add(fileUrl);
			output = openAiMessageCreation.processGPT4Request(input, userId, Constants.Mention_Assistant_ID, false,
					fileUrls);
			if (output != null && !output.isEmpty()) {
				slackMessageService.replyMessage(Constants.MENTION, userId, channelId, ts, slack.methods(), output,
						token);
			}
			logger.info("mentionEvent end: output {}", output);
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: "
					+ eventPayload.getEvent().getUser() + " Team Id: " + teamId + " Channel Id: " + channelId + " ts: "
					+ ts + " Exception details: " + e.toString());
		}
	}

	@Async
	public void createTextImage(EventPayload eventPayload) throws Exception {
		String teamId = eventPayload.getTeam_id();
		String userId = eventPayload.getEvent().getUser();
		String channelId = eventPayload.getEvent().getChannel();
		String ts = eventPayload.getEvent().getEvent_ts();
		try {
			logger.info("mentionEvent start: event {}", eventPayload);
			String input = eventPayload.getEvent().getText()
					.substring(eventPayload.getEvent().getText().indexOf(">") + 1);
			if (input == null || input.isEmpty()) {
				// send basic reponse
				return;
			}

			String output = "";
			input = input.trim() + "\n";
			output = openAiMessageCreation.CreateTextToImage(input);
			if (output != null && !output.isEmpty()) {
				slackMessageService.replyMessage(Constants.MENTION, userId, channelId, ts, slack.methods(), output,
						token);
			}
			logger.info("mentionEvent end: output {}", output);
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: "
					+ eventPayload.getEvent().getUser() + " Team Id: " + teamId + " Channel Id: " + channelId + " ts: "
					+ ts + " Exception details: " + e.toString());
		}
	}

	public void createImageVariation(EventPayload eventPayLoad, String channelId, String ts) throws Exception {
		String userId = eventPayLoad.getUser().getId();
		String output = "";
		try {
			Message message = slackMessageService.getMessageByTimestamp(channelId, ts, slack.methods(), token);
			String fileUrl = message.getFiles().get(0).getUrlPrivate();
			if (fileUrl != null && !fileUrl.isEmpty()) {
				output = openAiMessageCreation.CreateImageVariation(fileUrl);
				if (output != null && !output.isEmpty()) {
					slackMessageService.postFile("", "", channelId, slack.methods(), output, token);
				}
			}
			logger.info("codeToWrite end");
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Channel Id: "
					+ channelId + e.toString());
		}
	}

	@Async
	public void customerService(EventPayload eventPayLoad, String channelId, String ts, String userReason,
			String imageUrl, String userEmail) throws Exception {
		String userId = eventPayLoad.getUser().getId();
		String output = "";
		try {
			String fileUrl = imageUrl;
			if (fileUrl != null && !fileUrl.isEmpty()) {
				String prompt = "You are a diagnostic assistant. Please analyze the following laptop's condition based on the provided details and return a JSON object indicating the action to be taken.\n\nDetails:\n- The laptop powers on: [yes/no]\n- The screen displays correctly: [yes/no]\n- The keyboard responds to input: [yes/no]\n- The trackpad works correctly: [yes/no]\n- Any error messages displayed: [list any error messages]\n- Physical damage: [describe any visible damage]\n\nPossible actions:\n- \"good_condition\": The laptop is in good working condition.\n- \"broken\": The laptop is broken and needs repair.\n- \"human_interaction_needed\": The condition cannot be determined and needs human intervention.\n\nPlease only provide the JSON object with the appropriate action based on the analysis. Only return json output. Must return only valid JSON text. Ensure the output follows this structure:\n\n\nExample JSON object:\n{\n  \"action\": \"good_condition\"\n}\n\nNow, analyze the laptop's condition based on the provided details and return the appropriate JSON object.\n";
				output = customeOpenAIClient.processRequest(prompt, "", fileUrl);
				if (output != null && !output.isEmpty()) {
					if (output.contains("broken")) {
						String brokenMessage = String.format(
								"Our diagnostics indicate that your laptop is broken and requires repair. As a result, we will process a refund for your purchase. Please expect to receive the refund within the next 5-7 business days. If you have any questions or need further assistance, please contact support at %s.\n\n{\"action\": \"broken\", \"next_step\": \"send_refund\"}",
								userEmail);
						slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(),
								brokenMessage, token);
					} else if (output.contains("good_condition")) {
						String goodConditionMessage = String.format(
								"Your laptop has been thoroughly checked and is in good working condition. No issues were found. No further action is needed at this time. If you have any questions or need further assistance, please contact support at %s.\n\n{\"action\": \"good_condition\"}",
								userEmail);
						slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(),
								goodConditionMessage, token);
					} else {
						String humanInteractionMessage = String.format(
								"Our diagnostics were unable to determine the exact condition of your laptop. It requires further human inspection to identify potential issues. Please contact support for further assistance at %s.\n\n{\"action\": \"human_interaction_needed\"}",
								userEmail);
						slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(),
								humanInteractionMessage, token);
					}
				}
				logger.info("mentionEvent end: output {}", output);
			}
			logger.info("codeToWrite end");
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Channel Id: "
					+ channelId + e.toString());
		}
	}

	public void createSTT(EventPayload eventPayLoad, String channelId, String ts) throws Exception {
		String userId = eventPayLoad.getUser().getId();
		String output = "";
		try {
			Message message = slackMessageService.getMessageByTimestamp(channelId, ts, slack.methods(), token);
			String fileUrl = message.getFiles().get(0).getUrlPrivate();
			if (fileUrl != null && !fileUrl.isEmpty()) {
				output = openAiMessageCreation.CreateSpeeach2Text("", fileUrl, "");
				if (output != null && !output.isEmpty()) {
					slackMessageService.postFile("", "", channelId, slack.methods(), output, token);
				}
			}
			logger.info("codeToWrite end");
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Channel Id: "
					+ channelId + e.toString());
		}
	}

	public void createTTS(EventPayload eventPayLoad, String channelId, String ts) throws Exception {
		String userId = eventPayLoad.getUser().getId();
		String output = "";
		try {
			Message message = slackMessageService.getMessageByTimestamp(channelId, ts, slack.methods(), token);
			String input = message.getText();
			if (input != null && !input.isEmpty()) {
				output = openAiMessageCreation.CreateText2Speech(input);
				if (output != null && !output.isEmpty()) {
					slackMessageService.postFile("", "", channelId, slack.methods(), output, token);
				}
			}
			logger.info("codeToWrite end");
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Channel Id: "
					+ channelId + e.toString());
		}
	}

	@Async
	public void dmEvent(EventPayload eventPayload) throws Exception {
		String teamId = eventPayload.getTeam_id();
		String userId = eventPayload.getEvent().getUser();
		String channelId = eventPayload.getEvent().getChannel();
		String ts = eventPayload.getEvent().getEvent_ts();
		try {
			// Add input condition
			logger.info("dmEvent started with: event {}", eventPayload);
			List<ChatMessage> chat = new ArrayList<>();
			chat = slackMessageService.getDmMessages(channelId, ts, slack.methods(), "D0754LTARV0", token);
			String fileUrl = findFileById(eventPayload.getEvent().getFiles());
			List<String> fileUrls = new ArrayList<>();
			fileUrls.add(fileUrl);
			String output = "";
			if (chat != null && !chat.isEmpty()) {
				output = openAiMessageCreation.processGPT4Request(chat.toString(), eventPayload.getEvent().getUser(),
						Constants.Mention_Assistant_ID, false, fileUrls);
				if (output != null && !output.isEmpty()) {
					slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(), output,
							token);
				}
			}
			logger.info("dmEvent start: output {}", output);
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: "
					+ eventPayload.getEvent().getUser() + " Team Id: " + teamId + " Channel Id: " + channelId + " ts: "
					+ ts + " Exception details: " + e.toString());
		}
	}

	@Async
	public void codeToTranslate(EventPayload eventPayload, String channelId, String from, String to, String code)
			throws Exception {
		logger.info("codeToTranslate start: channelId {} from {} to {}", channelId, from, to);
		String userId = eventPayload.getUser().getId();
		String teamId = eventPayload.getView().getTeam_id();
		String output = "";
		try {
			String chat = openAiMessageRequestCreator.transLateCodeGPT4(teamId, userId, channelId, from, to, code,
					token);
			if (chat != null && !chat.isEmpty()) {
				output = openAiMessageCreation.processGPT4Request(chat, userId, Constants.Coder_Assistant_ID, false,
						null);
				if (output != null && !output.isEmpty()) {
					slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(), output,
							token);
				}
			}
			logger.info("codeToTranslate end: channelId {} from {} to {}", channelId, from, to);
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Team Id: "
					+ teamId + " Channel Id: " + channelId + " from: " + from + " to:" + to + " code: " + code
					+ " Exception details: " + e.toString());
		}
	}

	@Async
	public void codeToWrite(EventPayload eventPayLoad, String channelId, String to, String instruction)
			throws Exception {
		logger.info("codeToWrite start: channelId {} to {} instruction {}", channelId, to, instruction);
		String userId = eventPayLoad.getUser().getId();
		String teamId = eventPayLoad.getView().getTeam_id();
		String output = "";
		try {
			instruction = openAiMessageRequestCreator.writeCodeGPT4(teamId, userId, channelId, to, instruction, token);
			if (instruction != null && !instruction.isEmpty()) {
				output = openAiMessageCreation.processGPT4Request(instruction, userId, Constants.Coder_Assistant_ID,
						false, null);
				if (output != null && !output.isEmpty()) {
					slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(), output,
							token);
				}
			}
			logger.info("codeToWrite end");
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Team Id: "
					+ teamId + " Channel Id: " + channelId + " to:" + to + " instruction: " + instruction
					+ " Exception details: " + e.toString());
		}
	}
	
	@Async
	public void textToTranslate(EventPayload eventPayload, String channelId, String from, String to, String code)
			throws Exception {
		logger.info("codeToTranslate start: channelId {} from {} to {}", channelId, from, to);
		String userId = eventPayload.getUser().getId();
		String teamId = eventPayload.getView().getTeam_id();
		String output = "";
		try {
			String chat = openAiMessageRequestCreator.transLateCodeGPT4(teamId, userId, channelId, from, to, code,
					token);
			if (chat != null && !chat.isEmpty()) {
				output = openAiMessageCreation.processGPT4Request(chat, userId, Constants.Translator_Assistant_ID,
						false, null);
				if (output != null && !output.isEmpty()) {
					slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(), output,
							token);
				}
			}
			logger.info("codeToTranslate end: channelId {} from {} to {}", channelId, from, to);
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Team Id: "
					+ teamId + " Channel Id: " + channelId + " from: " + from + " to:" + to + " code: " + code
					+ " Exception details: " + e.toString());
		}
	}

	@Async
	public void updateSector(EventPayload eventPayload, String sector) throws Exception {
		String userId = eventPayload.getUser().getId();
		String teamId = eventPayload.getView().getTeam_id();
		try {
			openAiMessageCreation.updateAssistant(sector);
			String channelId = slackMessageService.getDmChannelId(slack.methods(), userId, token);
			slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(),
					"Your Sector has been successfully updated.", token);
			logger.info("codeToTranslate end: channelId {} from {} to {}", channelId);
		} catch (Exception e) {
			throw new Exception(
					"Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Team Id: " + teamId);
		}
	}

	// loading message tab first time
	// Todo: thing about creating alert table

	@Async
	public void appMessageEvent(EventPayload eventPayload) throws Exception {
		logger.info("appMessageEvent start: eventPayload {} ", eventPayload.toString());
		String userId = eventPayload.getEvent().getUser();
		String teamId = eventPayload.getTeam_id();
		try {
			SlackUser slackUser = slackUserService.getSlackUser(teamId, userId);
			if (slackUserService.isMessagesTabFirstTime(slackUser, eventPayload.getEvent().getChannel())) {
				SlackView welcomeMessageView = slackViewRepository.findById(Constants.WELCOMEMESSAGE).get();
				SlackOauthModal slackOauthModal = slackOauthService.getUserAccessToken(teamId);
				slackMessageService.dmMessage(userId, slack.methods(),
						welcomeMessageView.getView().replace("userId", userId), token);
			}
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Team Id: "
					+ teamId + " Exception details: " + e.toString());
		}
		logger.info("appMessageEvent end");
	}

	// save feedback

	// Modal submission
	@Async
	public void modalActionEvent(EventPayload eventPayLoad) {
		logger.info("blockActionEvent start: eventPayLoad {}", eventPayLoad);
		String triggerId = eventPayLoad.getTrigger_id();
		String userId = eventPayLoad.getUser().getId();
		Action action = eventPayLoad.getActions().get(0);
		if (action.getAction_id() != null && action.getAction_id().contains("PostMessageId")) {
			String output = action.getAction_id().replace("PostMessageId", "");
			slackMessageService.xyz(eventPayLoad.getResponse_url(), output, token);
			return;
		}

		if ("feedback".equals(action.getAction_id())) {
			try {
				viewUpdateService.openFeedbackModal(userId, triggerId, Constants.FEEDBACK, token);
			} catch (Exception e) {
				logger.info(e.toString());
			}
		}
	}

	// Update user access for shared plan user
	@Async
	public void submitActionEvent(EventPayload eventPayLoad, List<String> selectedUserIds) {
		logger.info("submitActionEvent start: eventPayLoad {}", eventPayLoad);
		slackUserService.updateUserAccess(selectedUserIds, eventPayLoad.getView().getTeam_id(),
				eventPayLoad.getUser().getId());
		logger.info("submitActionEvent end: eventPayLoad {}", eventPayLoad);
	}

	// Shortcuts events
	@Async
	public void messageActionEvent(EventPayload eventPayLoad) throws Exception {
		logger.info("messageActionEvent start: eventPayLoad {}", eventPayLoad);
		String teamId = eventPayLoad.getUser().getTeam_id();
		String userId = eventPayLoad.getUser().getId();
		String channelId = eventPayLoad.getChannel().getId();
		String ts = eventPayLoad.getMessage_ts();
		try {

			List<Message> messages = slackMessageService.getThreadMessages(channelId, ts, slack.methods(), "", token);
			List<String> chunks = new ArrayList<>();
			Set<String> userIdSet = new HashSet<>();
			List<String> fileUrls = new ArrayList<>();
			if (messages != null) {
				userIdSet = SlackUtils.getUsersFromMessages(messages);
				chunks = SlackUtils.getChunkFromMessages(messages);
				fileUrls = SlackUtils.getFileUrls(messages);
			}
			String output = "";
			String command = getCommand(eventPayLoad.getCallback_id(), false);

			if (command.equalsIgnoreCase(Constants.IMAGEVARIATION_ID)) {
				createImageVariation(eventPayLoad, channelId, ts);
				return;
			}

			if (eventPayLoad.getCallback_id().equalsIgnoreCase(Constants.TTS)) {
				createTTS(eventPayLoad, channelId, ts);
				return;
			}

			if (eventPayLoad.getCallback_id().equalsIgnoreCase(Constants.STT)) {
				createSTT(eventPayLoad, channelId, ts);
				return;
			}

			int depth = 0;
			if (chunks != null && !chunks.isEmpty()) {
				while (output.isEmpty() || chunks.size() != 1) {
					if (chunks.size() > 1) {
						depth++;
					}
					for (String input : chunks) {
						output += openAiMessageCreation.processGPT4Request(input, channelId, command, true, null);
					}
					chunks = new ArrayList<>();
					SlackUtils.getChunk(output, chunks);
				}
				if (depth >= 1) {
					output = openAiMessageCreation.processGPT4Request(output, channelId, command, true, null);
				}
				output = SlackUtils.replaceUserIdWithTags(output, userIdSet);
				slackMessageService.replyMessage(command, userId, channelId, ts, slack.methods(), output, token);
			}
			logger.info("messageActionEvent end");
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " User Id: " + userId + " Team Id: "
					+ teamId + " Exception details: " + e.toString());
		}
	}

	// command events
	@Async
	public void commandEvent(String responseUrl, String teamId, String userId, String channelId, String triggerId,
			String input, String commandName) {
		try {
			logger.info("mentionEvent started with: teamId {} userId {} input {}", teamId, userId, input);
			if (commandName.equals(Constants.TEXT2IMG)) {
				String prompt = openAiMessageCreation.processGPT4Request(input, "", Constants.Prompt_Assistant_ID,
						false, null);
				if (prompt.length() > 995) {
					prompt = prompt.substring(0, 996);
				}
				String result = openAiMessageCreation.CreateTextToImage(prompt);
				slackMessageService.postFile(input, prompt, channelId, slack.methods(), result, token);
				return;
			} else if (commandName.equals("/livesearch")) {
				String search = openAiMessageCreation.liveSearch(input);
				String result = openAiMessageCreation.processGPT4Request(search.substring(0, 2000), "",
						Constants.Live_Search_Assistant_ID, false, null);
				slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(), result,
						token);
				return;
			} else if (commandName.equals("/stockanalysis")) {
				String search = openAiMessageCreation.getStockData(input);
				String result = openAiMessageCreation.processGPT4Request(search, "", Constants.Live_Stock_Analysis_ID,
						false, null);
				slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(), result,
						token);
				return;
			} else if (commandName.equals("/stocksentiment")) {
				String search = openAiMessageCreation.getStockAnalysis(input);
				String result = openAiMessageCreation.processGPT4Request(search, "", Constants.Live_Stock_Sentiment_ID,
						false, null);
				slackMessageService.postMessage(Constants.CONVERSATION, userId, channelId, slack.methods(), result,
						token);
				return;
			} else if (commandName.equals("/powerpoint")) {
				try {
					List<SlideData> slideInfo = openAiMessageCreation.pptDetails(input);
					List<SlideData> result = new ArrayList<>();
					SlideData slide = null;
					for (SlideData data : slideInfo) {
						String prompt = openAiMessageCreation.processGPT4Request(input, "",
								Constants.Prompt_Assistant_ID, false, null);
						if (prompt.length() > 995) {
							prompt = prompt.substring(0, 996);
						}
						String imagePath = openAiMessageCreation.CreateTextToImage(prompt);
						slide = new SlideData(data.getTitle(), data.getText(), imagePath);
						result.add(slide);
					}
					String pptResult = pointCreator.createPresentation(result);
					slackMessageService.postFile(input, input, channelId, slack.methods(), pptResult, token);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public void shortCuts(EventPayload eventPayLoad) throws Exception {
		logger.info("shortCuts start");
		String triggerId = eventPayLoad.getTrigger_id();
		String teamId = eventPayLoad.getUser().getTeam_id();
		try {
			viewUpdateService.codeViewModal(triggerId, getView(eventPayLoad.getCallback_id()), token);
		} catch (Exception e) {
			throw new Exception("Thread: " + Thread.currentThread().getName() + " Team Id: " + teamId
					+ " Exception details: " + e.toString());
		}
		logger.info("shortCuts end");
	}

	public String getCommand(String name, boolean isSlash) {
		if (isSlash) {
			if (name.equals(Constants.ELABORATE_COMMAND)) {
				return Constants.ELABORATE;
			}

			if (name.equals(Constants.SUMMARIZE_COMMAND)) {
				return Constants.Summriser_Assistant_ID;
			}
		} else {
			if (name.equals(Constants.ELABORATE_ID)) {
				return Constants.ELABORATE;
			}

			if (name.equals(Constants.IMAGEVARIATION_ID)) {
				return Constants.IMAGEVARIATION_ID;
			}

			if (name.equals(Constants.SUMMARIZE_ID)) {
				return Constants.Summriser_Assistant_ID;
			}

			if (name.equals(Constants.SENTIMENT)) {
				return Constants.Sentiment_Assistant_ID;
			}
		}

		return "";
	}

	public String getView(String callbackId) {
		String result = "";
		switch (callbackId) {
		case Constants.TRANSLATE_CALLBACKID:
			result = Constants.TRANSLATE_VIEW;
			break;
		case Constants.RAPIDCODE_CALLBACKID:
			result = Constants.CODETOWRITE_VIEW;
			break;
		case Constants.LANG_TRANSLATE_CALLBACKID:
			result = "langToTranslate";
			break;
		case Constants.CUSTOMERSERVICE_ID:
			result = Constants.CUSTOMERSERVICE_ID;
			break;
		case "industry_select_modal":
			result = "industry_select_modal";
			break;
		default:
			break;

		}
		return result;
	}

	private String convertToBase64(String filePath) {
		File file = new File(filePath);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			byte[] byteArray = new byte[(int) file.length()];
			fis.read(byteArray);
			fis.close();
			// Encode byte array to Base64
			return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(byteArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String findFileById(List<Files> fiels) throws IOException, SlackApiException {
		if (fiels == null || fiels.isEmpty())
			return null;

		return fiels.get(0).getUrl_private();
	}
}
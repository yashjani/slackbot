package com.openai.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.slack.event.dto.AccessToken;
import com.openai.slack.event.dto.EventPayload;
import com.openai.slack.services.SlackEventService;
import com.openai.slack.services.SlackOauthService;
import com.openai.utils.SlackUtils;


@RestController
@CrossOrigin(origins = {"http://127.0.0.1:8000", "https://3776-73-170-43-86.ngrok.io"})
public class SlackController {

	Logger logger = LoggerFactory.getLogger(SlackController.class);

	@Autowired
	SlackOauthService slackOauthService;

	@Autowired
	SlackEventService slackEventService;
	
	@Value("${slackClientSecret}")
	private String slackClientSecret;

	@PostMapping(path = "/slackevents")
	public String slackEvents(@RequestHeader Map<String, String> headers,@RequestBody String event) throws Exception {
		
 		if(SlackUtils.verifySlackRequest(slackClientSecret, 
				headers.get("x-slack-request-timestamp"), event, headers.get("x-slack-signature"))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Reqeust");
		}
		if (event.contains("challenge")) {
			return event;
		}
		ObjectMapper obj = new ObjectMapper();
		EventPayload eventPayload = obj.readValue(event, EventPayload.class);
		switch (eventPayload.getEvent().getType()) {
		case "app_mention":
			slackEventService.mentionEvent(eventPayload);
			break;
		case "app_home_opened":
			if("messages".equalsIgnoreCase(eventPayload.getEvent().getTab())) {
				slackEventService.appMessageEvent(eventPayload);
			}
			break;
		case "message":
			if (eventPayload.getEvent().getBot_id() == null && 
			"im".equals(eventPayload.getEvent().getChannel_type()) && 
			eventPayload.getEvent().getText() != null) {
				slackEventService.dmEvent(eventPayload);
			}
			break;
		default:
			break;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
	}

	@PostMapping(path = "/interactiveevents")
	public String slackInteractiveEvents(@RequestHeader Map<String, String> headers, @RequestBody String event)
			throws Exception {
		if(SlackUtils.verifySlackRequest(slackClientSecret, 
				headers.get("x-slack-request-timestamp"), event, headers.get("x-slack-signature"))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Reqeust");
		}
		if (event.contains("challenge")) {
			return event;
		}
		event = event.substring(event.indexOf("payload=") + 8);
		event = URLDecoder.decode(event, "UTF-8");
		ObjectMapper obj = new ObjectMapper();
		EventPayload eventPayload = obj.readValue(event, EventPayload.class);
		switch (eventPayload.getType()) {
		case "block_actions":
			slackEventService.modalActionEvent(eventPayload);
			break;
		case "message_action":
			slackEventService.messageActionEvent(eventPayload);
			break;
		case "view_submission":
			if ("UserSelection".equalsIgnoreCase(eventPayload.getView().getCallback_id())) {
				JsonNode userIdNode = obj.readTree(event).get("view").get("state").get("values")
						.get("update_selected_users").get("AssignedUsers").get("selected_users");
				List<String> userIdList = new ArrayList<>();
				for (JsonNode userNode : userIdNode) {
					userIdList.add(userNode.asText());
				}
				slackEventService.submitActionEvent(eventPayload, userIdList);
			}else if("CodeToTranslate".equalsIgnoreCase(eventPayload.getView().getCallback_id())) {
				String from = obj.readTree(event).get("view").get("state").get("values")
						.get("from_block").get("lang_value").get("selected_option").get("value").asText();
				String to = obj.readTree(event).get("view").get("state").get("values")
						.get("to_block").get("lang_value").get("selected_option").get("value").asText();
				String code = obj.readTree(event).get("view").get("state").get("values")
						.get("code").get("lang_value").get("value").asText();
				String channelId = obj.readTree(event).get("response_urls").get(0).get("channel_id").asText();
				slackEventService.codeToTranslate(eventPayload, channelId, from, to, code);
			}else if("CodeToWrite".equalsIgnoreCase(eventPayload.getView().getCallback_id())) {
				String to = obj.readTree(event).get("view").get("state").get("values")
						.get("in_lang").get("lang_value").get("selected_option").get("value").asText();
				String code = obj.readTree(event).get("view").get("state").get("values")
						.get("instruction").get("lang_value").get("value").asText();
				String channelId = obj.readTree(event).get("response_urls").get(0).get("channel_id").asText();
				slackEventService.codeToWrite(eventPayload, channelId, to, code);
			}else if("LangToTranslate".equalsIgnoreCase(eventPayload.getView().getCallback_id())) {
				String from = obj.readTree(event).get("view").get("state").get("values")
						.get("from_block").get("lang_value").get("selected_option").get("value").asText();
				String to = obj.readTree(event).get("view").get("state").get("values")
						.get("to_block").get("lang_value").get("selected_option").get("value").asText();
				String code = obj.readTree(event).get("view").get("state").get("values")
						.get("code").get("lang_value").get("value").asText();
				String channelId = obj.readTree(event).get("response_urls").get(0).get("channel_id").asText();
				slackEventService.textToTranslate(eventPayload, channelId, from, to, code);
			}else if("industry_select_modal".equalsIgnoreCase(eventPayload.getView().getCallback_id())) {
				String sector = obj.readTree(event).get("view").get("state").get("values")
						.get("industry_select_block").get("industry_select_action").get("selected_option").get("value").asText();
				slackEventService.updateSector(eventPayload, sector);
			}else if("CustomerService".equalsIgnoreCase(eventPayload.getView().getCallback_id())) {
				String imageUrl = obj.readTree(event).get("view").get("state").get("values")
						.get("imageUrl").get("image_url_value").get("value").asText();
				String userReason = obj.readTree(event).get("view").get("state").get("values")
						.get("userReason").get("reason_value").get("value").asText();
				String userEmail = obj.readTree(event).get("view").get("state").get("values")
						.get("userEmail").get("email_value").get("value").asText();
				String channelId = obj.readTree(event).get("response_urls").get(0).get("channel_id").asText();

				slackEventService.customerService(eventPayload, channelId, event, userReason, imageUrl, userEmail);
			}
			break;
		case "shortcut":
			slackEventService.shortCuts(eventPayload);
			
		default:
			break;
		}
		return "";
	}
	
	@PostMapping(path = "/adduser")
	public Map<String,String> addUserToServer(@RequestBody AccessToken token) throws IOException {
		Map<String,String> result =  new HashMap<>();
		result.put("result", slackOauthService.saveUserAccessToken(token.getToken()));
		return result;
	}

	@PostMapping(path = "/commands/elaborate")
	public String elaborateCommand(@RequestBody String event) throws JsonMappingException, JsonProcessingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/elaborate");
		return "";
	}

	@PostMapping(path = "/commands/summarize")
	public String summariseCommand(@RequestBody String event) throws JsonMappingException, JsonProcessingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/summarize");
		return "";
	}

	@PostMapping(path = "/commands/text2img")
	public String text2Img(@RequestBody String event) throws JsonMappingException, JsonProcessingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/imagine");
		return "";
	}
	
	@PostMapping(path = "/commands/livesearch")
	public String liveSearch(@RequestBody String event) throws JsonMappingException, JsonProcessingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/livesearch");
		return "";
	}
	
	@PostMapping(path = "/commands/stockanalysis")
	public String stockAnalysis(@RequestBody String event) throws JsonMappingException, JsonProcessingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/stockanalysis");
		return "";
		
	}	
	
	@PostMapping(path = "/commands/stocksentiment")
	public String stockSentiment(@RequestBody String event) throws JsonMappingException, JsonProcessingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/stocksentiment");
		return "";
	}

	
	@PostMapping(path = "/twitter")
	public String twitter(@RequestBody String event) throws JsonMappingException, JsonProcessingException {
		return "";
	}
   
	@PostMapping(path = "/stableevents")
	public String callback(@RequestHeader Map<String, String> headers,@RequestBody String event) throws Exception {
		
		if(SlackUtils.verifySlackRequest(slackClientSecret, 
				headers.get("x-slack-request-timestamp"), event, headers.get("x-slack-signature"))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Reqeust");
		}
		if (event.contains("challenge")) {
			return event;
		}
		ObjectMapper obj = new ObjectMapper();
		EventPayload eventPayload = obj.readValue(event, EventPayload.class);
		switch (eventPayload.getEvent().getType()) {
		case "app_mention":
			slackEventService.mentionEvent(eventPayload);
			break;
		case "message":
			if (eventPayload.getEvent().getBot_id() == null && 
			"im".equals(eventPayload.getEvent().getChannel_type()) && 
			eventPayload.getEvent().getText() != null) {
				slackEventService.dmEvent(eventPayload);
			}
			break;
		default:
			break;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
	}
	
	@PostMapping(path = "/commands/powerpoint")
	public String powerPoint(@RequestHeader Map<String, String> headers,@RequestBody String event) throws Exception {
		
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/powerpoint");
		return "";
	}
	
	@PostMapping(path = "/commands/customerservice")
	public String customerService(@RequestHeader Map<String, String> headers,@RequestBody String event) throws Exception {
		
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = event;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		slackEventService.commandEvent(query_pairs.get("response_url"), query_pairs.get("team_id"),
				query_pairs.get("user_id"), query_pairs.get("channel_id"), query_pairs.get("trigger_id"),
				query_pairs.get("text"), "/customerservice");
		return "";
	}
	
}

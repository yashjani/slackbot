package com.openai.utils;

import java.util.Date;

import com.openai.entity.SlackOauthModal;
import com.openai.entity.TextRequestQueue;
import com.openai.slack.event.dto.SlackOAuthResponse;
import com.theokanning.openai.completion.CompletionRequest;

public class SlackDTOConvertor {
	
	public static SlackOauthModal convertSlackOauthResponseToSlackUser(SlackOAuthResponse oAuthResponse) {
		SlackOauthModal slackUser = new SlackOauthModal();
		slackUser.setAppId(oAuthResponse.getApp_id());
		slackUser.setUserId(oAuthResponse.getAuthed_user().getId());
		slackUser.setScope(oAuthResponse.getScope());
		slackUser.setTokenType(oAuthResponse.getToken_type());
		slackUser.setBotTokens(oAuthResponse.getAccess_token());
		slackUser.setBotId(oAuthResponse.getBot_user_id());
		slackUser.setTeamId(oAuthResponse.getTeam().getId());
		slackUser.setTeamName(oAuthResponse.getTeam().getName());
		return slackUser;
	}
	
	public static TextRequestQueue completionRequestToTextRequestQueue(String teamId, String userId, String channelId,
			String status, int retryCount, String requestType, String lastStop, CompletionRequest completionRequest, Date createdDate, String input, String token) {
		TextRequestQueue textRequestQueue = new TextRequestQueue();
		textRequestQueue.setChannelId(channelId);
		textRequestQueue.setCreatedDate(createdDate);
		textRequestQueue.setLastStop(lastStop);
		textRequestQueue.setRetryCount(retryCount);
		textRequestQueue.setStatus(status);
		textRequestQueue.setRequestType(requestType);
		textRequestQueue.setTeamId(teamId);
		textRequestQueue.setUpdatedDate(createdDate);
		textRequestQueue.setUserId(userId);
		textRequestQueue.setModel(completionRequest.getModel());
		textRequestQueue.setMaxTokens(completionRequest.getMaxTokens());
		textRequestQueue.setFrequencyPenalty(completionRequest.getFrequencyPenalty());
		textRequestQueue.setPresencePenalty(completionRequest.getPresencePenalty());
		textRequestQueue.setPrompt(completionRequest.getPrompt());
		textRequestQueue.setStop(completionRequest.getStop().toString());
		textRequestQueue.setTemperature(completionRequest.getTemperature());
		textRequestQueue.setTopP(completionRequest.getTopP());
		textRequestQueue.setInput(input);
		return textRequestQueue;	
	}

	public static CompletionRequest textRequestQueueToCompletionRequest(TextRequestQueue textRequestQueue) {
		CompletionRequest completionRequest = CompletionRequest.builder().prompt(textRequestQueue.getPrompt())
				.maxTokens(textRequestQueue.getMaxTokens())
				.frequencyPenalty(textRequestQueue.getFrequencyPenalty())
				.presencePenalty(textRequestQueue.getPresencePenalty())
				.model(textRequestQueue.getModel())
				.stop(SlackUtils.stringToList(textRequestQueue.getStop()))
				.temperature(textRequestQueue.getTemperature())
				.topP(textRequestQueue.getTopP())
				.build();
		return completionRequest;	
	}
}

package com.openai.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(SlackUseridTeamid.class)
public class SlackOauthModal {

	@Id
	private String teamId;

	@Id
	private String userId;

	private String botTokens;
	private String botId;
	private String scope;
	private String appId;
	private String tokenType;
	private String teamName;

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBotTokens() {
		return "";
	}

	public void setBotTokens(String botTokens) {
		this.botTokens = botTokens;
	}

	public String getBotId() {
		return botId;
	}

	public void setBotId(String botId) {
		this.botId = botId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	@Override
	public String toString() {
		return "SlackOauthModal [teamId=" + teamId + ", userId=" + userId + ", botTokens=" + botTokens + ", botId="
				+ botId + ", scope=" + scope + ", appId=" + appId + ", tokenType=" + tokenType + ", teamName="
				+ teamName + "]";
	}

}

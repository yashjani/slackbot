package com.openai.slack.event.dto;

public class AccessToken {


	private String token;
	private String plan;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	@Override
	public String toString() {
		return "AccessToken [token=" + token + ", plan=" + plan + "]";
	}
	
}

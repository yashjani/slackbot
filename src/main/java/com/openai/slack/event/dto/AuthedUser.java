package com.openai.slack.event.dto;

public class AuthedUser {
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AuthedUser [id=" + id + "]";
	}
	
}

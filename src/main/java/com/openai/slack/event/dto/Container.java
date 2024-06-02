package com.openai.slack.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Container {
	private String type;
	private String message_ts;
	private String channel_id;
	private String is_ephemeral;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage_ts() {
		return message_ts;
	}
	public void setMessage_ts(String message_ts) {
		this.message_ts = message_ts;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getIs_ephemeral() {
		return is_ephemeral;
	}
	public void setIs_ephemeral(String is_ephemeral) {
		this.is_ephemeral = is_ephemeral;
	}
	@Override
	public String toString() {
		return "Container [type=" + type + ", message_ts=" + message_ts + ", channel_id=" + channel_id
				+ ", is_ephemeral=" + is_ephemeral + "]";
	}
	
	

}

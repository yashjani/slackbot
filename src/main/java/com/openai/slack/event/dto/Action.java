package com.openai.slack.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
	private String action_id;
	private String block_id;
	private String value;
	private String type;
	private String action_ts;

	public String getAction_id() {
		return action_id;
	}

	public void setAction_id(String action_id) {
		this.action_id = action_id;
	}

	public String getBlock_id() {
		return block_id;
	}

	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction_ts() {
		return action_ts;
	}

	public void setAction_ts(String action_ts) {
		this.action_ts = action_ts;
	}

	@Override
	public String toString() {
		return "Action [action_id=" + action_id + ", block_id=" + block_id + ", value=" + value + ", type=" + type
				+ ", action_ts=" + action_ts + "]";
	}
	

}

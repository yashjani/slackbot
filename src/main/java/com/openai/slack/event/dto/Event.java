package com.openai.slack.event.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

	private String client_msg_id;
	private String type;
	private String text;
	private String user;
	private String ts;
	private String thread_ts;
	private ArrayList<Block> blocks;
	private String team;
	private String channel;
	private String event_ts;
	private String tab;
	private View view;
	private String channel_type;
	private String bot_id;
	private String app_id;
	private BotProfile bot_profile;
	private String parent_user_id;
    private String file_id;
    private List<Files> files;
    
	public String getClient_msg_id() {
		return client_msg_id;
	}

	public void setClient_msg_id(String client_msg_id) {
		this.client_msg_id = client_msg_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getEvent_ts() {
		return event_ts;
	}

	public void setEvent_ts(String event_ts) {
		this.event_ts = event_ts;
	}

	public String getThread_ts() {
		return thread_ts;
	}

	public void setThread_ts(String thread_ts) {
		this.thread_ts = thread_ts;
	}
	
	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	
	public String getChannel_type() {
		return channel_type;
	}

	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}
	
	

	public String getBot_id() {
		return bot_id;
	}

	public void setBot_id(String bot_id) {
		this.bot_id = bot_id;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public BotProfile getBot_profile() {
		return bot_profile;
	}

	public void setBot_profile(BotProfile bot_profile) {
		this.bot_profile = bot_profile;
	}

	public String getParent_user_id() {
		return parent_user_id;
	}

	public void setParent_user_id(String parent_user_id) {
		this.parent_user_id = parent_user_id;
	}

	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	
	public List<Files> getFiles() {
		return files;
	}

	public void setFiles(List<Files> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "Event [client_msg_id=" + client_msg_id + ", type=" + type + ", text=" + text + ", user=" + user
				+ ", ts=" + ts + ", thread_ts=" + thread_ts + ", blocks=" + blocks + ", team=" + team + ", channel="
				+ channel + ", event_ts=" + event_ts + ", tab=" + tab + ", view=" + view + ", channel_type="
				+ channel_type + ", bot_id=" + bot_id + ", app_id=" + app_id + ", bot_profile=" + bot_profile
				+ ", parent_user_id=" + parent_user_id + ", file_id=" + file_id + ", files=" + files + "]";
	}
	
}

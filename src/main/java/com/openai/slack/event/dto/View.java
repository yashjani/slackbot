package com.openai.slack.event.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class View {
	private String id;
	private String team_id;
	private String type;
	private List<Object> blocks;
	private String private_metadata;
	private String callback_id;
	private String hash;
	private boolean clear_on_close;
	private boolean notify_on_close;
	private String root_view_id;
	private String app_id;
	private String external_id;
	private String app_installed_team_id;
	private String bot_id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Object> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Object> blocks) {
		this.blocks = blocks;
	}

	public String getPrivate_metadata() {
		return private_metadata;
	}

	public void setPrivate_metadata(String private_metadata) {
		this.private_metadata = private_metadata;
	}

	public String getCallback_id() {
		return callback_id;
	}

	public void setCallback_id(String callback_id) {
		this.callback_id = callback_id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public boolean isClear_on_close() {
		return clear_on_close;
	}

	public void setClear_on_close(boolean clear_on_close) {
		this.clear_on_close = clear_on_close;
	}

	public boolean isNotify_on_close() {
		return notify_on_close;
	}

	public void setNotify_on_close(boolean notify_on_close) {
		this.notify_on_close = notify_on_close;
	}

	public String getRoot_view_id() {
		return root_view_id;
	}

	public void setRoot_view_id(String root_view_id) {
		this.root_view_id = root_view_id;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getExternal_id() {
		return external_id;
	}

	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}

	public String getApp_installed_team_id() {
		return app_installed_team_id;
	}

	public void setApp_installed_team_id(String app_installed_team_id) {
		this.app_installed_team_id = app_installed_team_id;
	}

	public String getBot_id() {
		return bot_id;
	}

	public void setBot_id(String bot_id) {
		this.bot_id = bot_id;
	}

	@Override
	public String toString() {
		return "View [id=" + id + ", team_id=" + team_id + ", type=" + type + ", blocks=" + blocks
				+ ", private_metadata=" + private_metadata + ", callback_id=" + callback_id + ", hash=" + hash
				+ ", clear_on_close=" + clear_on_close + ", notify_on_close=" + notify_on_close + ", root_view_id="
				+ root_view_id + ", app_id=" + app_id + ", external_id=" + external_id + ", app_installed_team_id="
				+ app_installed_team_id + ", bot_id=" + bot_id + "]";
	}

}

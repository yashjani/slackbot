package com.openai.slack.event.dto;

public class SlackOAuthResponse {
	
	private boolean ok;
	private String app_id;
	private AuthedUser authed_user;
	private String scope;
	private String token_type;
	private String access_token;
	private String bot_user_id;
	private Team team;
	private Object enterprise;
	private boolean is_enterprise_install;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public AuthedUser getAuthed_user() {
		return authed_user;
	}

	public void setAuthed_user(AuthedUser authed_user) {
		this.authed_user = authed_user;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getBot_user_id() {
		return bot_user_id;
	}

	public void setBot_user_id(String bot_user_id) {
		this.bot_user_id = bot_user_id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Object getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Object enterprise) {
		this.enterprise = enterprise;
	}

	public boolean isIs_enterprise_install() {
		return is_enterprise_install;
	}

	public void setIs_enterprise_install(boolean is_enterprise_install) {
		this.is_enterprise_install = is_enterprise_install;
	}
	
	@Override
	public String toString() {
		return "SlackOAuthResponse [ok=" + ok + ", app_id=" + app_id + ", authed_user=" + authed_user + ", scope="
				+ scope + ", token_type=" + token_type + ", access_token=" + access_token + ", bot_user_id="
				+ bot_user_id + ", team=" + team + ", enterprise=" + enterprise + ", is_enterprise_install="
				+ is_enterprise_install + "]";
	}

}

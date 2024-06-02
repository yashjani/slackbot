package com.openai.slack.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Authorization {
	public Object enterprise_id;
	public String team_id;
	public String user_id;
	public boolean is_bot;
	public boolean is_enterprise_install;

	public Object getEnterprise_id() {
		return enterprise_id;
	}

	public void setEnterprise_id(Object enterprise_id) {
		this.enterprise_id = enterprise_id;
	}

	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public boolean isIs_bot() {
		return is_bot;
	}

	public void setIs_bot(boolean is_bot) {
		this.is_bot = is_bot;
	}

	public boolean isIs_enterprise_install() {
		return is_enterprise_install;
	}

	public void setIs_enterprise_install(boolean is_enterprise_install) {
		this.is_enterprise_install = is_enterprise_install;
	}

	@Override
	public String toString() {
		return "Authorization [enterprise_id=" + enterprise_id + ", team_id=" + team_id + ", user_id=" + user_id
				+ ", is_bot=" + is_bot + ", is_enterprise_install=" + is_enterprise_install + "]";
	}

}
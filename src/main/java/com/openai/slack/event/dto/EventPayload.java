package com.openai.slack.event.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventPayload {

	private String token;
	private String team_id;
	private String api_app_id;
	private Event event;
	private String type;
	private String event_id;
	private int event_time;
	private ArrayList<Authorization> authorizations;
	private boolean is_ext_shared_channel;
	private String event_context;
	private String trigger_id;
	private String callback_id;
	private List<Action> actions;
	private User user;
	private View view;
	private String response_url;
	private Channel channel;
	private Container container;
	private String message_ts;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}

	public String getApi_app_id() {
		return api_app_id;
	}

	public void setApi_app_id(String api_app_id) {
		this.api_app_id = api_app_id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public int getEvent_time() {
		return event_time;
	}

	public void setEvent_time(int event_time) {
		this.event_time = event_time;
	}

	public ArrayList<Authorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(ArrayList<Authorization> authorizations) {
		this.authorizations = authorizations;
	}

	public boolean isIs_ext_shared_channel() {
		return is_ext_shared_channel;
	}

	public void setIs_ext_shared_channel(boolean is_ext_shared_channel) {
		this.is_ext_shared_channel = is_ext_shared_channel;
	}

	public String getEvent_context() {
		return event_context;
	}

	public void setEvent_context(String event_context) {
		this.event_context = event_context;
	}

	public String getTrigger_id() {
		return trigger_id;
	}

	public void setTrigger_id(String trigger_id) {
		this.trigger_id = trigger_id;
	}

	public String getCallback_id() {
		return callback_id;
	}

	public void setCallback_id(String callback_id) {
		this.callback_id = callback_id;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public String getResponse_url() {
		return response_url;
	}

	public void setResponse_url(String response_url) {
		this.response_url = response_url;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public String getMessage_ts() {
		return message_ts;
	}

	public void setMessage_ts(String message_ts) {
		this.message_ts = message_ts;
	}
	
	

	
}

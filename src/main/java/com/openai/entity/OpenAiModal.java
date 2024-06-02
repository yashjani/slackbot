package com.openai.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.openai.utils.Constants;

@Entity
public class OpenAiModal {

	@Id
	String command;
	String modal;
	String prompt;
	String postFix;
	double temperature;
	int max_tokens;
	double top_p;
	double frequency_penalty;
	double presence_penalty;
	String stopSequence;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double d) {
		this.temperature = d;
	}

	public int getMax_tokens() {
		return max_tokens;
	}

	public void setMax_tokens(int max_tokens) {
		this.max_tokens = max_tokens;
	}

	public double getTop_p() {
		return top_p;
	}

	public double getFrequency_penalty() {
		return frequency_penalty;
	}

	public double getPresence_penalty() {
		return presence_penalty;
	}

	public String getPostFix() {
		return postFix;
	}

	public void setPostFix(String postFix) {
		this.postFix = postFix;
	}

	public void setTop_p(double top_p) {
		this.top_p = top_p;
	}

	public void setFrequency_penalty(double frequency_penalty) {
		this.frequency_penalty = frequency_penalty;
	}

	public void setPresence_penalty(double presence_penalty) {
		this.presence_penalty = presence_penalty;
	}

	public String getStopSequence() {
		return stopSequence;
	}

	public void setStopSequence(String stopSequence) {
		this.stopSequence = stopSequence;
	}

	public List<String> getListOfStopSequence() {
		return this.stopSequence == null || this.stopSequence.isEmpty() ? Constants.STOP_SEQUENCE
				: new ArrayList<>(Arrays.asList(this.stopSequence.replace("[", "").replace("]", "").split(",")));
	}

	@Override
	public String toString() {
		return "OpenAiModal [command=" + command + ", modal=" + modal + ", prompt=" + prompt + ", postFix=" + postFix
				+ ", temperature=" + temperature + ", max_tokens=" + max_tokens + ", top_p=" + top_p
				+ ", frequency_penalty=" + frequency_penalty + ", presence_penalty=" + presence_penalty
				+ ", stopSequence=" + stopSequence + "]";
	}

}

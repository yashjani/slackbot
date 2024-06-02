package com.openai.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TextRequestQueue {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userId;
	private String channelId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate", columnDefinition = "DATETIME")
	private Date createdDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedDate", columnDefinition = "DATETIME")
	private Date updatedDate;
	@Lob
	private String input;
	private String lastStop;
	private String requestType;
	private int retryCount;
	private String status;
	private String teamId;
	@Column(name = "`usage`")
	private long usage;
	private String model;
	@Lob
	private String prompt;
	private String suffix;
	private Integer maxTokens;
	private Double temperature;
	private Double topP;
	private Integer n;
	private Boolean stream;
	private Integer logprobs;
	private Boolean echo;
	private String stop;
	private Double presencePenalty;
	private Double frequencyPenalty;
	private Integer bestOf;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getLastStop() {
		return lastStop;
	}
	public void setLastStop(String lastStop) {
		this.lastStop = lastStop;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public long getUsage() {
		return usage;
	}
	public void setUsage(long usage) {
		this.usage = usage;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public Integer getMaxTokens() {
		return maxTokens;
	}
	public void setMaxTokens(Integer maxTokens) {
		this.maxTokens = maxTokens;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getTopP() {
		return topP;
	}
	public void setTopP(Double topP) {
		this.topP = topP;
	}
	public Integer getN() {
		return n;
	}
	public void setN(Integer n) {
		this.n = n;
	}
	public Boolean getStream() {
		return stream;
	}
	public void setStream(Boolean stream) {
		this.stream = stream;
	}
	public Integer getLogprobs() {
		return logprobs;
	}
	public void setLogprobs(Integer logprobs) {
		this.logprobs = logprobs;
	}
	public Boolean getEcho() {
		return echo;
	}
	public void setEcho(Boolean echo) {
		this.echo = echo;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public Double getPresencePenalty() {
		return presencePenalty;
	}
	public void setPresencePenalty(Double presencePenalty) {
		this.presencePenalty = presencePenalty;
	}
	public Double getFrequencyPenalty() {
		return frequencyPenalty;
	}
	public void setFrequencyPenalty(Double frequencyPenalty) {
		this.frequencyPenalty = frequencyPenalty;
	}
	public Integer getBestOf() {
		return bestOf;
	}
	public void setBestOf(Integer bestOf) {
		this.bestOf = bestOf;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	@Override
	public String toString() {
		return "TextRequestQueue [id=" + id + ", userId=" + userId + ", channelId=" + channelId + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + ", input=" + input + ", lastStop=" + lastStop
				+ ", requestType=" + requestType + ", retryCount=" + retryCount + ", status=" + status + ", teamId="
				+ teamId + ", usage=" + usage + ", model=" + model + ", prompt=" + prompt + ", suffix=" + suffix
				+ ", maxTokens=" + maxTokens + ", temperature=" + temperature + ", topP=" + topP + ", n=" + n
				+ ", stream=" + stream + ", logprobs=" + logprobs + ", echo=" + echo + ", stop=" + stop
				+ ", presencePenalty=" + presencePenalty + ", frequencyPenalty=" + frequencyPenalty + ", bestOf="
				+ bestOf + "]";
	}

}

package com.openai.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@IdClass(SlackUseridTeamid.class)
public class SlackUser {

	@Id
	private String teamId;

	@Id
	private String userId;
	private String subscriptionId;
	private String plan;
	private boolean isPrimary;
	private int count;
	private String dmChannelId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate", columnDefinition = "DATETIME")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validTill", columnDefinition = "DATETIME")
	private Date validTill;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedDate", columnDefinition = "DATETIME")
	private Date updatedDate;

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getDmChannelId() {
		return dmChannelId;
	}

	public void setDmChannelId(String dmChannelId) {
		this.dmChannelId = dmChannelId;
	}

	@Override
	public String toString() {
		return "SlackUser [teamId=" + teamId + ", userId=" + userId + ", subscriptionId=" + subscriptionId + ", plan="
				+ plan + ", isPrimary=" + isPrimary + ", count=" + count + ", dmChannelId=" + dmChannelId
				+ ", createdDate=" + createdDate + ", validTill=" + validTill + ", updatedDate=" + updatedDate + "]";
	}

}

package com.openai.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UserRequestDetails {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String requestType;
	private String userId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;
	
	
	public UserRequestDetails(String requestType, String userId) {
		this.requestDate = new Date();
		this.requestType = requestType;
		this.userId = userId;
	}
	

	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	@Override
	public String toString() {
		return "UserRequestDetails [requestType=" + requestType + ", userId=" + userId + ", requestDate=" + requestDate
				+ "]";
	}
}

package com.openai.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class SlackView {

	@Id
	private String name;

	@Lob
	private String view;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	@Override
	public String toString() {
		return "SlackView [name=" + name + ", view=" + view + "]";
	}

}

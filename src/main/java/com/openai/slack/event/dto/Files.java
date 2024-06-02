package com.openai.slack.event.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Files {

	private String id;
    private String file_id;
    private String url_private;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getUrl_private() {
		return url_private;
	}
	public void setUrl_private(String url_private) {
		this.url_private = url_private;
	}
    
    
	
}

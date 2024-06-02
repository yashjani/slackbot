package com.openai.slack.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlideData {
    private String title;
    private String text;
    private String imagePath;

    public SlideData() {
        // Default constructor
    }

    @JsonCreator
    public SlideData(
        @JsonProperty("title") String title,
        @JsonProperty("text") String text,
        @JsonProperty("imagePath") String imagePath
    ) {
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

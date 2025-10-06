package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an image with URL and dimensions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("width")
    private int width;
    
    @JsonProperty("height")
    private int height;
    
    // Constructors
    public Image() {}
    
    public Image(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }
    
    // Getters and Setters
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    @Override
    public String toString() {
        return String.format("Image{url='%s', width=%d, height=%d}", url, width, height);
    }
}

package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the context in which a track was played
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Context {
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("href")
    private String href;
    
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    
    @JsonProperty("uri")
    private String uri;
    
    // Constructors
    public Context() {}
    
    public Context(String type, String href, ExternalUrls externalUrls, String uri) {
        this.type = type;
        this.href = href;
        this.externalUrls = externalUrls;
        this.uri = uri;
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getHref() {
        return href;
    }
    
    public void setHref(String href) {
        this.href = href;
    }
    
    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }
    
    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    @Override
    public String toString() {
        return String.format("Context{type='%s', uri='%s'}", type, uri);
    }
}

package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents external URLs for Spotify resources
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalUrls {
    @JsonProperty("spotify")
    private String spotify;
    
    // Constructors
    public ExternalUrls() {}
    
    public ExternalUrls(String spotify) {
        this.spotify = spotify;
    }
    
    // Getters and Setters
    public String getSpotify() {
        return spotify;
    }
    
    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }
    
    @Override
    public String toString() {
        return String.format("ExternalUrls{spotify='%s'}", spotify);
    }
}

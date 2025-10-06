package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents follower count information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Followers {
    @JsonProperty("href")
    private String href;
    
    @JsonProperty("total")
    private int total;
    
    // Constructors
    public Followers() {}
    
    public Followers(String href, int total) {
        this.href = href;
        this.total = total;
    }
    
    // Getters and Setters
    public String getHref() {
        return href;
    }
    
    public void setHref(String href) {
        this.href = href;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        return String.format("Followers{total=%d}", total);
    }
}

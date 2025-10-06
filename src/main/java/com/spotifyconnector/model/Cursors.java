package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents cursor information for pagination
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cursors {
    @JsonProperty("after")
    private String after;
    
    @JsonProperty("before")
    private String before;
    
    // Constructors
    public Cursors() {}
    
    public Cursors(String after, String before) {
        this.after = after;
        this.before = before;
    }
    
    // Getters and Setters
    public String getAfter() {
        return after;
    }
    
    public void setAfter(String after) {
        this.after = after;
    }
    
    public String getBefore() {
        return before;
    }
    
    public void setBefore(String before) {
        this.before = before;
    }
    
    @Override
    public String toString() {
        return String.format("Cursors{after='%s', before='%s'}", after, before);
    }
}

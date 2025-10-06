package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the response from Spotify's recently played tracks endpoint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentlyPlayedResponse {
    @JsonProperty("items")
    private List<PlayHistoryItem> items;
    
    @JsonProperty("next")
    private String next;
    
    @JsonProperty("cursors")
    private Cursors cursors;
    
    @JsonProperty("limit")
    private int limit;
    
    @JsonProperty("href")
    private String href;
    
    // Constructors
    public RecentlyPlayedResponse() {}
    
    public RecentlyPlayedResponse(List<PlayHistoryItem> items, String next, Cursors cursors, 
                                 int limit, String href) {
        this.items = items;
        this.next = next;
        this.cursors = cursors;
        this.limit = limit;
        this.href = href;
    }
    
    // Getters and Setters
    public List<PlayHistoryItem> getItems() {
        return items;
    }
    
    public void setItems(List<PlayHistoryItem> items) {
        this.items = items;
    }
    
    public String getNext() {
        return next;
    }
    
    public void setNext(String next) {
        this.next = next;
    }
    
    public Cursors getCursors() {
        return cursors;
    }
    
    public void setCursors(Cursors cursors) {
        this.cursors = cursors;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public String getHref() {
        return href;
    }
    
    public void setHref(String href) {
        this.href = href;
    }
}

package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single item in the recently played tracks history
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayHistoryItem {
    @JsonProperty("track")
    private Track track;
    
    @JsonProperty("played_at")
    private String playedAt;
    
    @JsonProperty("context")
    private Context context;
    
    // Constructors
    public PlayHistoryItem() {}
    
    public PlayHistoryItem(Track track, String playedAt, Context context) {
        this.track = track;
        this.playedAt = playedAt;
        this.context = context;
    }
    
    // Getters and Setters
    public Track getTrack() {
        return track;
    }
    
    public void setTrack(Track track) {
        this.track = track;
    }
    
    public String getPlayedAt() {
        return playedAt;
    }
    
    public void setPlayedAt(String playedAt) {
        this.playedAt = playedAt;
    }
    
    public Context getContext() {
        return context;
    }
    
    public void setContext(Context context) {
        this.context = context;
    }
    
    @Override
    public String toString() {
        return String.format("PlayHistoryItem{track=%s, playedAt='%s'}", track, playedAt);
    }
}

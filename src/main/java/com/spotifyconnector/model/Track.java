package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a Spotify track with all relevant information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("artists")
    private List<Artist> artists;
    
    @JsonProperty("album")
    private Album album;
    
    @JsonProperty("duration_ms")
    private long durationMs;
    
    @JsonProperty("popularity")
    private int popularity;
    
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    
    @JsonProperty("played_at")
    private String playedAt; // For recently played tracks
    
    // Constructors
    public Track() {}
    
    public Track(String id, String name, List<Artist> artists, Album album, 
                 long durationMs, int popularity, ExternalUrls externalUrls) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.album = album;
        this.durationMs = durationMs;
        this.popularity = popularity;
        this.externalUrls = externalUrls;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Artist> getArtists() {
        return artists;
    }
    
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
    
    public Album getAlbum() {
        return album;
    }
    
    public void setAlbum(Album album) {
        this.album = album;
    }
    
    public long getDurationMs() {
        return durationMs;
    }
    
    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }
    
    public int getPopularity() {
        return popularity;
    }
    
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
    
    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }
    
    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }
    
    public String getPlayedAt() {
        return playedAt;
    }
    
    public void setPlayedAt(String playedAt) {
        this.playedAt = playedAt;
    }
    
    /**
     * Get formatted duration as MM:SS
     */
    public String getFormattedDuration() {
        long seconds = durationMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    
    /**
     * Get artist names as a comma-separated string
     */
    public String getArtistNames() {
        if (artists == null || artists.isEmpty()) {
            return "Unknown Artist";
        }
        return artists.stream()
                .map(Artist::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Unknown Artist");
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", getArtistNames(), name, getFormattedDuration());
    }
}

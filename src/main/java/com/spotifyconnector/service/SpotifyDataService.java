package com.spotifyconnector.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotifyconnector.model.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Service for retrieving Spotify user data
 */
public class SpotifyDataService {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyDataService.class);
    
    private static final String BASE_URL = "https://api.spotify.com/v1";
    private static final String RECENTLY_PLAYED_URL = BASE_URL + "/me/player/recently-played";
    private static final String TOP_ARTISTS_URL = BASE_URL + "/me/top/artists";
    private static final String TOP_TRACKS_URL = BASE_URL + "/me/top/tracks";
    private static final String USER_PROFILE_URL = BASE_URL + "/me";
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public SpotifyDataService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Get user's recently played tracks
     */
    public RecentlyPlayedResponse getRecentlyPlayedTracks(String accessToken, int limit) throws IOException {
        String url = RECENTLY_PLAYED_URL + "?limit=" + limit;
        return makeRequest(url, accessToken, RecentlyPlayedResponse.class);
    }
    
    /**
     * Get user's recently played tracks with time range
     */
    public RecentlyPlayedResponse getRecentlyPlayedTracks(String accessToken, int limit, String after, String before) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(RECENTLY_PLAYED_URL);
        urlBuilder.append("?limit=").append(limit);
        
        if (after != null && !after.isEmpty()) {
            urlBuilder.append("&after=").append(after);
        }
        if (before != null && !before.isEmpty()) {
            urlBuilder.append("&before=").append(before);
        }
        
        return makeRequest(urlBuilder.toString(), accessToken, RecentlyPlayedResponse.class);
    }
    
    /**
     * Get user's top artists
     */
    public TopItemsResponse<Artist> getTopArtists(String accessToken, String timeRange, int limit) throws IOException {
        String url = TOP_ARTISTS_URL + "?time_range=" + timeRange + "&limit=" + limit;
        return makeRequest(url, accessToken, TopItemsResponse.class);
    }
    
    /**
     * Get user's top tracks
     */
    public TopItemsResponse<Track> getTopTracks(String accessToken, String timeRange, int limit) throws IOException {
        String url = TOP_TRACKS_URL + "?time_range=" + timeRange + "&limit=" + limit;
        return makeRequest(url, accessToken, TopItemsResponse.class);
    }
    
    /**
     * Get user profile
     */
    public UserProfile getUserProfile(String accessToken) throws IOException {
        return makeRequest(USER_PROFILE_URL, accessToken, UserProfile.class);
    }
    
    /**
     * Generic method to make HTTP requests to Spotify API
     */
    private <T> T makeRequest(String url, String accessToken, Class<T> responseClass) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                logger.error("API request failed: {} - {}", response.code(), errorBody);
                throw new IOException("API request failed: " + response.code() + " - " + errorBody);
            }
            
            String responseBody = response.body().string();
            logger.debug("API response for {}: {}", url, responseBody);
            
            return objectMapper.readValue(responseBody, responseClass);
        } catch (Exception e) {
            logger.error("Error making request to {}: {}", url, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Generic response wrapper for top items endpoints
     */
    public static class TopItemsResponse<T> {
        @JsonProperty("href")
        private String href;
        
        @JsonProperty("limit")
        private int limit;
        
        @JsonProperty("next")
        private String next;
        
        @JsonProperty("offset")
        private int offset;
        
        @JsonProperty("previous")
        private String previous;
        
        @JsonProperty("total")
        private int total;
        
        @JsonProperty("items")
        private java.util.List<T> items;
        
        // Getters and setters
        public String getHref() { return href; }
        public void setHref(String href) { this.href = href; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
        public String getNext() { return next; }
        public void setNext(String next) { this.next = next; }
        public int getOffset() { return offset; }
        public void setOffset(int offset) { this.offset = offset; }
        public String getPrevious() { return previous; }
        public void setPrevious(String previous) { this.previous = previous; }
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
        public java.util.List<T> getItems() { return items; }
        public void setItems(java.util.List<T> items) { this.items = items; }
    }
}

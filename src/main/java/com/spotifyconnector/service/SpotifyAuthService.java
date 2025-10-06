package com.spotifyconnector.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotifyconnector.config.SpotifyConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Service for handling Spotify OAuth authentication
 */
public class SpotifyAuthService {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthService.class);
    
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String USER_PROFILE_URL = "https://api.spotify.com/v1/me";
    
    private final SpotifyConfig config;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public SpotifyAuthService() {
        this.config = SpotifyConfig.getInstance();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Exchange authorization code for access token
     */
    public String exchangeCodeForToken(String authorizationCode) throws IOException {
        if (!config.isValid()) {
            throw new IllegalStateException("Spotify configuration is not valid. Please check your credentials.");
        }
        
        String credentials = config.getClientId() + ":" + config.getClientSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", authorizationCode)
                .add("redirect_uri", config.getRedirectUri())
                .build();
        
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(formBody)
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new IOException("Failed to exchange code for token: " + response.code() + " - " + errorBody);
            }
            
            String responseBody = response.body().string();
            logger.debug("Token response: {}", responseBody);
            
            // Parse the response to extract access token
            TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);
            return tokenResponse.getAccessToken();
        }
    }
    
    /**
     * Get user profile information
     */
    public String getUserProfile(String accessToken) throws IOException {
        Request request = new Request.Builder()
                .url(USER_PROFILE_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new IOException("Failed to get user profile: " + response.code() + " - " + errorBody);
            }
            
            return response.body().string();
        }
    }
    
    /**
     * Get the authorization URL for user to visit
     */
    public String getAuthorizationUrl() {
        return config.getAuthorizationUrl();
    }
    
    /**
     * Get the authorization URL with a custom redirect URI
     */
    public String getAuthorizationUrl(String customRedirectUri) {
        return config.getAuthorizationUrl(customRedirectUri);
    }
    
    /**
     * Check if configuration is valid
     */
    public boolean isConfigurationValid() {
        return config.isValid();
    }
    
    /**
     * Inner class for token response
     */
    private static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
        
        @JsonProperty("token_type")
        private String tokenType;
        
        @JsonProperty("expires_in")
        private int expiresIn;
        
        @JsonProperty("refresh_token")
        private String refreshToken;
        
        @JsonProperty("scope")
        private String scope;
        
        // Getters and setters
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        public int getExpiresIn() { return expiresIn; }
        public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
    }
}

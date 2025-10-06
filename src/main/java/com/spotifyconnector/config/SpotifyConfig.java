package com.spotifyconnector.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for Spotify API credentials and settings
 */
public class SpotifyConfig {
    private static final String CONFIG_FILE = "spotify.properties";
    private static final String DEFAULT_CONFIG_FILE = "spotify-default.properties";
    
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
    
    private static SpotifyConfig instance;
    
    private SpotifyConfig() {
        loadConfiguration();
    }
    
    public static synchronized SpotifyConfig getInstance() {
        if (instance == null) {
            instance = new SpotifyConfig();
        }
        return instance;
    }
    
    /**
     * Load configuration from properties file
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        
        // Try to load from user config first
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
            } else {
                // Fall back to default config
                try (InputStream defaultInput = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
                    if (defaultInput != null) {
                        props.load(defaultInput);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
        
        // Load properties with defaults
        this.clientId = props.getProperty("spotify.client.id", "");
        this.clientSecret = props.getProperty("spotify.client.secret", "");
        this.redirectUri = props.getProperty("spotify.redirect.uri", "http://localhost:8080/callback");
        this.scope = props.getProperty("spotify.scope", "user-read-recently-played user-top-read user-read-private");
    }
    
    /**
     * Validate that required configuration is present
     */
    public boolean isValid() {
        return clientId != null && !clientId.trim().isEmpty() &&
               clientSecret != null && !clientSecret.trim().isEmpty();
    }
    
    /**
     * Get the authorization URL for Spotify OAuth
     */
    public String getAuthorizationUrl() {
        try {
            return String.format(
                "https://accounts.spotify.com/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s",
                clientId, 
                java.net.URLEncoder.encode(redirectUri, "UTF-8"), 
                java.net.URLEncoder.encode(scope, "UTF-8")
            );
        } catch (java.io.UnsupportedEncodingException e) {
            // Fallback to non-encoded version if UTF-8 is not supported
            return String.format(
                "https://accounts.spotify.com/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s",
                clientId, redirectUri, scope
            );
        }
    }
    
    /**
     * Get the authorization URL for Spotify OAuth with custom redirect URI
     */
    public String getAuthorizationUrl(String customRedirectUri) {
        try {
            return String.format(
                "https://accounts.spotify.com/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s",
                clientId, 
                java.net.URLEncoder.encode(customRedirectUri, "UTF-8"), 
                java.net.URLEncoder.encode(scope, "UTF-8")
            );
        } catch (java.io.UnsupportedEncodingException e) {
            // Fallback to non-encoded version if UTF-8 is not supported
            return String.format(
                "https://accounts.spotify.com/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s",
                clientId, customRedirectUri, scope
            );
        }
    }
    
    // Getters
    public String getClientId() {
        return clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public String getScope() {
        return scope;
    }
    
    // Setters for runtime configuration
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
}

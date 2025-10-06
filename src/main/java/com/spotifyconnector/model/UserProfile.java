package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a Spotify user profile
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("country")
    private String country;
    
    @JsonProperty("followers")
    private Followers followers;
    
    @JsonProperty("images")
    private List<Image> images;
    
    @JsonProperty("product")
    private String product; // free, premium, etc.
    
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    
    @JsonProperty("uri")
    private String uri;
    
    // Constructors
    public UserProfile() {}
    
    public UserProfile(String id, String displayName, String email, String country, 
                      Followers followers, List<Image> images, String product) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.country = country;
        this.followers = followers;
        this.images = images;
        this.product = product;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public Followers getFollowers() {
        return followers;
    }
    
    public void setFollowers(Followers followers) {
        this.followers = followers;
    }
    
    public List<Image> getImages() {
        return images;
    }
    
    public void setImages(List<Image> images) {
        this.images = images;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
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
    
    /**
     * Get the best available profile image URL
     */
    public String getBestImageUrl() {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
                .filter(img -> img.getUrl() != null)
                .max((a, b) -> Integer.compare(a.getWidth(), b.getWidth()))
                .map(Image::getUrl)
                .orElse(null);
    }
    
    @Override
    public String toString() {
        return String.format("UserProfile{displayName='%s', email='%s', country='%s', product='%s'}", 
                           displayName, email, country, product);
    }
}

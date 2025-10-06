package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a Spotify artist with relevant information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("genres")
    private List<String> genres;
    
    @JsonProperty("popularity")
    private int popularity;
    
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    
    @JsonProperty("images")
    private List<Image> images;
    
    @JsonProperty("followers")
    private Followers followers;
    
    // Constructors
    public Artist() {}
    
    public Artist(String id, String name, List<String> genres, int popularity, 
                 ExternalUrls externalUrls, List<Image> images, Followers followers) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.popularity = popularity;
        this.externalUrls = externalUrls;
        this.images = images;
        this.followers = followers;
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
    
    public List<String> getGenres() {
        return genres;
    }
    
    public void setGenres(List<String> genres) {
        this.genres = genres;
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
    
    public List<Image> getImages() {
        return images;
    }
    
    public void setImages(List<Image> images) {
        this.images = images;
    }
    
    public Followers getFollowers() {
        return followers;
    }
    
    public void setFollowers(Followers followers) {
        this.followers = followers;
    }
    
    /**
     * Get genres as a comma-separated string
     */
    public String getGenresString() {
        if (genres == null || genres.isEmpty()) {
            return "No genres available";
        }
        return String.join(", ", genres);
    }
    
    /**
     * Get the best available image URL (prefer larger images)
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
        return String.format("%s (Popularity: %d)", name, popularity);
    }
}

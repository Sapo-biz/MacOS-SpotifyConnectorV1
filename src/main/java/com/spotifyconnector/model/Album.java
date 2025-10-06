package com.spotifyconnector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a Spotify album
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("album_type")
    private String albumType;
    
    @JsonProperty("artists")
    private List<Artist> artists;
    
    @JsonProperty("images")
    private List<Image> images;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    @JsonProperty("total_tracks")
    private int totalTracks;
    
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    
    // Constructors
    public Album() {}
    
    public Album(String id, String name, String albumType, List<Artist> artists, 
                 List<Image> images, String releaseDate, int totalTracks, ExternalUrls externalUrls) {
        this.id = id;
        this.name = name;
        this.albumType = albumType;
        this.artists = artists;
        this.images = images;
        this.releaseDate = releaseDate;
        this.totalTracks = totalTracks;
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
    
    public String getAlbumType() {
        return albumType;
    }
    
    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }
    
    public List<Artist> getArtists() {
        return artists;
    }
    
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
    
    public List<Image> getImages() {
        return images;
    }
    
    public void setImages(List<Image> images) {
        this.images = images;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public int getTotalTracks() {
        return totalTracks;
    }
    
    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }
    
    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }
    
    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
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
        return String.format("%s - %s (%s)", getArtistNames(), name, releaseDate);
    }
}

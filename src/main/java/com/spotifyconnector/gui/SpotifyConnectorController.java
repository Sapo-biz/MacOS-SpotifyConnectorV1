package com.spotifyconnector.gui;

import com.spotifyconnector.model.*;
import com.spotifyconnector.service.SpotifyAuthService;
import com.spotifyconnector.service.SpotifyDataService;
import com.spotifyconnector.service.CallbackServer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * Main controller for the Spotify Connector GUI
 */
public class SpotifyConnectorController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyConnectorController.class);
    
    private SpotifyAuthService authService;
    private SpotifyDataService dataService;
    private CallbackServer callbackServer;
    private String accessToken;
    private UserProfile currentUser;
    
    // FXML injected components
    @FXML private TabPane mainTabPane;
    @FXML private Tab loginTab;
    @FXML private Tab recentlyPlayedTab;
    @FXML private Tab topArtistsTab;
    @FXML private Tab topTracksTab;
    @FXML private Tab userProfileTab;
    
    // Login Tab
    @FXML private VBox loginContainer;
    @FXML private Label statusLabel;
    @FXML private Button loginButton;
    @FXML private TextField clientIdField;
    @FXML private PasswordField clientSecretField;
    @FXML private TextField redirectUriField;
    @FXML private TextArea authCodeArea;
    @FXML private Button connectButton;
    
    // Recently Played Tab
    @FXML private TableView<PlayHistoryItem> recentlyPlayedTable;
    @FXML private TableColumn<PlayHistoryItem, String> trackNameColumn;
    @FXML private TableColumn<PlayHistoryItem, String> artistNameColumn;
    @FXML private TableColumn<PlayHistoryItem, String> albumNameColumn;
    @FXML private TableColumn<PlayHistoryItem, String> playedAtColumn;
    @FXML private TableColumn<PlayHistoryItem, String> durationColumn;
    @FXML private Button refreshRecentButton;
    @FXML private ComboBox<Integer> recentLimitCombo;
    
    // Top Artists Tab
    @FXML private TableView<Artist> topArtistsTable;
    @FXML private TableColumn<Artist, String> artistNameColumn2;
    @FXML private TableColumn<Artist, Integer> artistPopularityColumn;
    @FXML private TableColumn<Artist, String> artistGenresColumn;
    @FXML private TableColumn<Artist, Integer> artistFollowersColumn;
    @FXML private Button refreshArtistsButton;
    @FXML private ComboBox<String> artistTimeRangeCombo;
    @FXML private ComboBox<Integer> artistLimitCombo;
    
    // Top Tracks Tab
    @FXML private TableView<Track> topTracksTable;
    @FXML private TableColumn<Track, String> trackNameColumn2;
    @FXML private TableColumn<Track, String> trackArtistColumn;
    @FXML private TableColumn<Track, String> trackAlbumColumn;
    @FXML private TableColumn<Track, Integer> trackPopularityColumn;
    @FXML private TableColumn<Track, String> trackDurationColumn;
    @FXML private Button refreshTracksButton;
    @FXML private ComboBox<String> trackTimeRangeCombo;
    @FXML private ComboBox<Integer> trackLimitCombo;
    
    // User Profile Tab
    @FXML private VBox profileContainer;
    @FXML private VBox profileContent;
    @FXML private ImageView profileImageView;
    @FXML private Label displayNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label countryLabel;
    @FXML private Label productLabel;
    @FXML private Label followersCountLabel;
    @FXML private Label totalTracksLabel;
    @FXML private Label accountAgeLabel;
    @FXML private Button refreshProfileButton;
    @FXML private Button spotifyProfileButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authService = new SpotifyAuthService();
        dataService = new SpotifyDataService();
        
        setupUI();
        // setupTableColumns(); // Temporarily commented out for testing
        setupEventHandlers();
        
        // Check if configuration is valid
        if (authService.isConfigurationValid()) {
            statusLabel.setText("Configuration loaded. Ready to connect!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } else {
            statusLabel.setText("Please configure your Spotify credentials first.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    private void setupUI() {
        // Setup login tab
        loginContainer.setSpacing(10);
        loginContainer.setPadding(new Insets(20));
        
        // Setup recently played tab
        recentlyPlayedTable.setPlaceholder(new Label("No recently played tracks available"));
        recentLimitCombo.setItems(FXCollections.observableArrayList(10, 20, 50));
        recentLimitCombo.setValue(20);
        
        // Setup top artists tab
        topArtistsTable.setPlaceholder(new Label("No top artists available"));
        artistTimeRangeCombo.setItems(FXCollections.observableArrayList("short_term", "medium_term", "long_term"));
        artistTimeRangeCombo.setValue("medium_term");
        artistLimitCombo.setItems(FXCollections.observableArrayList(10, 20, 50));
        artistLimitCombo.setValue(20);
        
        // Setup top tracks tab
        topTracksTable.setPlaceholder(new Label("No top tracks available"));
        trackTimeRangeCombo.setItems(FXCollections.observableArrayList("short_term", "medium_term", "long_term"));
        trackTimeRangeCombo.setValue("medium_term");
        trackLimitCombo.setItems(FXCollections.observableArrayList(10, 20, 50));
        trackLimitCombo.setValue(20);
        
        // Setup profile tab
        profileContainer.setSpacing(15);
        profileContainer.setPadding(new Insets(20));
        
        // Initialize profile labels with default values
        displayNameLabel.setText("Loading...");
        emailLabel.setText("Loading...");
        countryLabel.setText("Loading...");
        productLabel.setText("Loading...");
        followersCountLabel.setText("0");
        totalTracksLabel.setText("0");
        accountAgeLabel.setText("N/A");
    }
    
    private void setupTableColumns() {
        // Recently played table
        trackNameColumn.setCellValueFactory(new PropertyValueFactory<>("track.name"));
        artistNameColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getTrack().getArtistNames()));
        albumNameColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getTrack().getAlbum().getName()));
        playedAtColumn.setCellValueFactory(new PropertyValueFactory<>("playedAt"));
        durationColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getTrack().getFormattedDuration()));
        
        // Top artists table
        artistNameColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistPopularityColumn.setCellValueFactory(new PropertyValueFactory<>("popularity"));
        artistGenresColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getGenresString()));
        artistFollowersColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createIntegerBinding(() -> 
                cellData.getValue().getFollowers() != null ? 
                cellData.getValue().getFollowers().getTotal() : 0).asObject());
        
        // Top tracks table
        trackNameColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        trackArtistColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getArtistNames()));
        trackAlbumColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getAlbum().getName()));
        trackPopularityColumn.setCellValueFactory(new PropertyValueFactory<>("popularity"));
        trackDurationColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> 
                cellData.getValue().getFormattedDuration()));
    }
    
    private void setupEventHandlers() {
        loginButton.setOnAction(e -> openSpotifyAuth());
        connectButton.setOnAction(e -> connectWithAuthCode());
        refreshRecentButton.setOnAction(e -> loadRecentlyPlayed());
        refreshArtistsButton.setOnAction(e -> loadTopArtists());
        refreshTracksButton.setOnAction(e -> loadTopTracks());
        refreshProfileButton.setOnAction(e -> loadUserProfile());
        spotifyProfileButton.setOnAction(e -> openSpotifyProfile());
    }
    
    @FXML
    private void openSpotifyAuth() {
        try {
            // Start the callback server
            callbackServer = new CallbackServer();
            CompletableFuture<String> authCodeFuture = callbackServer.start();
            
            // Wait a moment for the server to start and get the actual port
            Thread.sleep(100);
            int actualPort = callbackServer.getActualPort();
            
            if (actualPort == -1) {
                throw new RuntimeException("Callback server failed to start on any port");
            }
            
            // Update the redirect URI to use the actual port
            String redirectUri = "http://127.0.0.1:" + actualPort + "/callback";
            redirectUriField.setText(redirectUri);
            
            // Get authorization URL with the correct redirect URI
            String authUrl = authService.getAuthorizationUrl(redirectUri);
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(authUrl));
            
            statusLabel.setText("Please authorize the application in your browser...");
            statusLabel.setStyle("-fx-text-fill: blue;");
            
            // Handle the authorization code when it's received
            authCodeFuture.thenAccept(authCode -> {
                Platform.runLater(() -> {
                    authCodeArea.setText(authCode);
                    statusLabel.setText("Authorization code received! Click Connect to proceed.");
                    statusLabel.setStyle("-fx-text-fill: green;");
                });
            }).exceptionally(throwable -> {
                Platform.runLater(() -> {
                    statusLabel.setText("Error receiving authorization code: " + throwable.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                });
                return null;
            });
            
        } catch (Exception e) {
            logger.error("Error opening browser", e);
            statusLabel.setText("Error opening browser: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    @FXML
    private void connectWithAuthCode() {
        String authCode = authCodeArea.getText().trim();
        if (authCode.isEmpty()) {
            statusLabel.setText("Please enter the authorization code.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        Task<String> connectTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("Connecting to Spotify...");
                return authService.exchangeCodeForToken(authCode);
            }
            
            @Override
            protected void succeeded() {
                accessToken = getValue();
                statusLabel.setText("Successfully connected to Spotify!");
                statusLabel.setStyle("-fx-text-fill: green;");
                
                // Switch to data tabs
                mainTabPane.getTabs().remove(loginTab);
                mainTabPane.getSelectionModel().select(recentlyPlayedTab);
                
                // Load initial data
                loadUserProfile();
                loadRecentlyPlayed();
                loadTopArtists();
                loadTopTracks();
            }
            
            @Override
            protected void failed() {
                statusLabel.setText("Connection failed: " + getException().getMessage());
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        };
        
        new Thread(connectTask).start();
    }
    
    @FXML
    private void loadRecentlyPlayed() {
        if (accessToken == null) return;
        
        Task<RecentlyPlayedResponse> task = new Task<RecentlyPlayedResponse>() {
            @Override
            protected RecentlyPlayedResponse call() throws Exception {
                updateMessage("Loading recently played tracks...");
                Integer limit = recentLimitCombo.getValue();
                if (limit == null) {
                    limit = 20; // Default value
                }
                return dataService.getRecentlyPlayedTracks(accessToken, limit);
            }
            
            @Override
            protected void succeeded() {
                RecentlyPlayedResponse response = getValue();
                ObservableList<PlayHistoryItem> items = FXCollections.observableArrayList(response.getItems());
                recentlyPlayedTable.setItems(items);
            }
            
            @Override
            protected void failed() {
                logger.error("Failed to load recently played tracks", getException());
                Platform.runLater(() -> {
                    recentlyPlayedTable.setPlaceholder(new Label("Error loading data: " + getException().getMessage()));
                });
            }
        };
        
        new Thread(task).start();
    }
    
    @FXML
    private void loadTopArtists() {
        if (accessToken == null) return;
        
        Task<SpotifyDataService.TopItemsResponse<Artist>> task = new Task<SpotifyDataService.TopItemsResponse<Artist>>() {
            @Override
            protected SpotifyDataService.TopItemsResponse<Artist> call() throws Exception {
                updateMessage("Loading top artists...");
                String timeRange = artistTimeRangeCombo.getValue();
                Integer limit = artistLimitCombo.getValue();
                if (timeRange == null) timeRange = "medium_term";
                if (limit == null) limit = 20;
                return dataService.getTopArtists(accessToken, timeRange, limit);
            }
            
            @Override
            protected void succeeded() {
                SpotifyDataService.TopItemsResponse<Artist> response = getValue();
                ObservableList<Artist> items = FXCollections.observableArrayList(response.getItems());
                topArtistsTable.setItems(items);
            }
            
            @Override
            protected void failed() {
                logger.error("Failed to load top artists", getException());
                Platform.runLater(() -> {
                    topArtistsTable.setPlaceholder(new Label("Error loading data: " + getException().getMessage()));
                });
            }
        };
        
        new Thread(task).start();
    }
    
    @FXML
    private void loadTopTracks() {
        if (accessToken == null) return;
        
        Task<SpotifyDataService.TopItemsResponse<Track>> task = new Task<SpotifyDataService.TopItemsResponse<Track>>() {
            @Override
            protected SpotifyDataService.TopItemsResponse<Track> call() throws Exception {
                updateMessage("Loading top tracks...");
                String timeRange = trackTimeRangeCombo.getValue();
                Integer limit = trackLimitCombo.getValue();
                if (timeRange == null) timeRange = "medium_term";
                if (limit == null) limit = 20;
                return dataService.getTopTracks(accessToken, timeRange, limit);
            }
            
            @Override
            protected void succeeded() {
                SpotifyDataService.TopItemsResponse<Track> response = getValue();
                ObservableList<Track> items = FXCollections.observableArrayList(response.getItems());
                topTracksTable.setItems(items);
            }
            
            @Override
            protected void failed() {
                logger.error("Failed to load top tracks", getException());
                Platform.runLater(() -> {
                    topTracksTable.setPlaceholder(new Label("Error loading data: " + getException().getMessage()));
                });
            }
        };
        
        new Thread(task).start();
    }
    
    @FXML
    private void loadUserProfile() {
        if (accessToken == null) return;
        
        Task<UserProfile> task = new Task<UserProfile>() {
            @Override
            protected UserProfile call() throws Exception {
                updateMessage("Loading user profile...");
                return dataService.getUserProfile(accessToken);
            }
            
            @Override
            protected void succeeded() {
                currentUser = getValue();
                updateProfileUI();
            }
            
            @Override
            protected void failed() {
                logger.error("Failed to load user profile", getException());
            }
        };
        
        new Thread(task).start();
    }
    
    private void updateProfileUI() {
        Platform.runLater(() -> {
            displayNameLabel.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Unknown");
            emailLabel.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "Not available");
            countryLabel.setText(currentUser.getCountry() != null ? currentUser.getCountry() : "Not available");
            productLabel.setText(currentUser.getProduct() != null ? currentUser.getProduct() : "Not available");
            
            if (currentUser.getFollowers() != null) {
                followersCountLabel.setText(String.format("%,d", currentUser.getFollowers().getTotal()));
            } else {
                followersCountLabel.setText("0");
            }
            
            // Calculate account age (approximate)
            if (currentUser.getUri() != null) {
                // Extract user ID from URI (spotify:user:USER_ID)
                String userId = currentUser.getUri().replace("spotify:user:", "");
                accountAgeLabel.setText("Active User");
            } else {
                accountAgeLabel.setText("N/A");
            }
            
            // Set total tracks (this would need to be calculated from API calls)
            totalTracksLabel.setText("Calculating...");
            
            // Load profile image
            String imageUrl = currentUser.getBestImageUrl();
            if (imageUrl != null) {
                try {
                    Image profileImage = new Image(imageUrl);
                    profileImageView.setImage(profileImage);
                } catch (Exception e) {
                    logger.warn("Failed to load profile image", e);
                }
            }
        });
    }
    
    @FXML
    private void openSpotifyProfile() {
        if (currentUser != null && currentUser.getExternalUrls() != null && currentUser.getExternalUrls().getSpotify() != null) {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(currentUser.getExternalUrls().getSpotify()));
            } catch (Exception e) {
                logger.error("Failed to open Spotify profile", e);
            }
        }
    }
    
    @FXML
    private void handleExit() {
        if (callbackServer != null) {
            callbackServer.stop();
        }
        Platform.exit();
    }
    
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Spotify Connector");
        alert.setHeaderText("Spotify Connector v1.0.0");
        alert.setContentText("A Java application to connect to Spotify and extract user's music data.\n\n" +
                           "Features:\n" +
                           "• Recently played tracks\n" +
                           "• Top artists and tracks\n" +
                           "• User profile information\n" +
                           "• Professional GUI with JavaFX");
        alert.showAndWait();
    }
}

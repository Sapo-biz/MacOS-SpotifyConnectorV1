package com.spotifyconnector;

import com.spotifyconnector.gui.SpotifyConnectorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main application class for Spotify Connector
 */
public class SpotifyConnectorApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyConnectorApp.class);
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/spotifyconnector/gui/spotify-connector.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);
            
            // Set application title
            primaryStage.setTitle("Spotify Connector");
            // Note: Icon removed to avoid file not found errors
            
            // Set minimum size
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            
            primaryStage.setScene(scene);
            primaryStage.show();
            
            logger.info("Spotify Connector application started successfully");
            
        } catch (IOException e) {
            logger.error("Failed to start application", e);
            System.err.println("Failed to start application: " + e.getMessage());
        }
    }
    
    @Override
    public void stop() {
        logger.info("Spotify Connector application shutting down");
    }
    
    public static void main(String[] args) {
        // Set system properties for better logging
        System.setProperty("logback.configurationFile", "logback.xml");
        
        // Add macOS-specific JVM arguments for JavaFX compatibility
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("javafx.animation.pulse", "60");
        System.setProperty("javafx.animation.quantum", "16666666");
        
        logger.info("Starting Spotify Connector application...");
        launch(args);
    }
}

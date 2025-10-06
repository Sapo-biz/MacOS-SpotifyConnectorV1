package com.spotifyconnector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple test application to verify JavaFX works on macOS
 */
public class SimpleTestApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.getChildren().add(new Label("Hello, JavaFX!"));
        root.getChildren().add(new Label("If you can see this, JavaFX is working!"));
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("JavaFX Test");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        System.out.println("Simple test application started successfully!");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

#!/bin/bash

# Run Spotify Connector with macOS-specific JVM arguments
java --module-path /Users/jasonhe/.m2/repository/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1-mac-aarch64.jar:/Users/jasonhe/.m2/repository/org/openjfx/javafx-controls/21.0.1/javafx-controls-21.0.1-mac-aarch64.jar:/Users/jasonhe/.m2/repository/org/openjfx/javafx-fxml/21.0.1/javafx-fxml-21.0.1-mac-aarch64.jar:/Users/jasonhe/.m2/repository/org/openjfx/javafx-graphics/21.0.1/javafx-graphics-21.0.1-mac-aarch64.jar \
     --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics \
     -Dprism.order=sw \
     -Djavafx.animation.fullspeed=true \
     -classpath target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout) \
     com.spotifyconnector.SpotifyConnectorApp
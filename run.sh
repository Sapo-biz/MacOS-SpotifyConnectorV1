#!/bin/bash

# Spotify Connector Run Script
# This script compiles and runs the Spotify Connector application

echo "Starting Spotify Connector..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "Error: Java 11 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "Java version: $(java -version 2>&1 | head -n 1)"
echo "Maven version: $(mvn -version | head -n 1)"

# Check if configuration exists
if [ ! -f "src/main/resources/spotify.properties" ]; then
    echo "Configuration file not found. Creating from template..."
    cp src/main/resources/spotify-default.properties src/main/resources/spotify.properties
    echo "Please edit src/main/resources/spotify.properties with your Spotify credentials"
    echo "Then run this script again."
    exit 1
fi

# Compile the application
echo "Compiling application..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Compilation failed. Please check the error messages above."
    exit 1
fi

echo "Compilation successful. Starting application..."

# Run the application
mvn javafx:run

echo "Application finished."

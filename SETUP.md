# Setup Guide

This guide will walk you through setting up the Spotify Connector application step by step.

## Prerequisites

Before you begin, ensure you have:

- **Java 11 or higher** installed on your system
- **Maven 3.6 or higher** installed
- A **Spotify account** (free or premium)
- Access to the **Spotify Developer Dashboard**

## Step 1: Get Spotify API Credentials

1. **Visit the Spotify Developer Dashboard**
   - Go to [https://developer.spotify.com/dashboard/applications](https://developer.spotify.com/dashboard/applications)
   - Log in with your Spotify account

2. **Create a New App**
   - Click "Create an App"
   - Fill in the required information:
     - **App name**: "Spotify Connector" (or any name you prefer)
     - **App description**: "A Java application to extract Spotify user data"
     - **Website**: You can leave this blank or add your website
     - **Redirect URI**: `http://localhost:8080/callback`
     - **API/SDKs**: Select "Web API"
   - Click "Save"

3. **Get Your Credentials**
   - After creating the app, you'll see your **Client ID** and **Client Secret**
   - **Important**: Keep these credentials secure and never share them publicly

## Step 2: Configure the Application

1. **Copy the Configuration Template**
   ```bash
   cp src/main/resources/spotify-default.properties src/main/resources/spotify.properties
   ```

2. **Edit the Configuration File**
   Open `src/main/resources/spotify.properties` and replace the placeholder values:
   ```properties
   # Replace these with your actual credentials
   spotify.client.id=your_actual_client_id_here
   spotify.client.secret=your_actual_client_secret_here
   
   # This should match what you set in Spotify Developer Dashboard
   spotify.redirect.uri=http://localhost:8080/callback
   
   # These scopes are required for the application to work
   spotify.scope=user-read-recently-played user-top-read user-read-private
   ```

## Step 3: Build and Run

1. **Build the Application**
   ```bash
   mvn clean compile
   ```

2. **Run the Application**
   ```bash
   mvn javafx:run
   ```

   If you encounter issues with JavaFX, you can also run:
   ```bash
   mvn clean package
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/spotify-connector-1.0.0.jar
   ```

## Step 4: First Time Authentication

1. **Launch the Application**
   - The application should open with the Login tab visible
   - You should see "Configuration loaded. Ready to connect!" in green

2. **Start Authentication**
   - Click "Open Spotify Authorization"
   - Your default browser will open with the Spotify authorization page

3. **Authorize the Application**
   - Log in to your Spotify account if prompted
   - Review the permissions the app is requesting
   - Click "Agree" to authorize the application

4. **Get the Authorization Code**
   - After authorization, you'll be redirected to a URL like:
     `http://localhost:8080/callback?code=AQC...`
   - **Copy the entire code value** (the part after `code=`)

5. **Complete the Connection**
   - Paste the authorization code into the "Authorization Code" field
   - Click "Connect"
   - If successful, you'll see "Successfully connected to Spotify!" and the app will switch to the data tabs

## Step 5: Using the Application

Once connected, you can explore different tabs:

### Recently Played Tab
- View your recently played tracks
- Adjust the limit (10, 20, or 50 tracks)
- Click "Refresh" to get the latest data

### Top Artists Tab
- See your most listened to artists
- Choose time range: Short term (4 weeks), Medium term (6 months), or Long term (all time)
- Adjust the number of results to display

### Top Tracks Tab
- View your favorite songs
- Filter by time range
- See popularity scores and duration

### Profile Tab
- View your Spotify profile information
- See your profile picture, name, email, and follower count

## Troubleshooting

### Common Issues and Solutions

#### "Configuration not valid" Error
- **Problem**: The app can't find valid Spotify credentials
- **Solution**: 
  - Check that `spotify.properties` exists and contains your credentials
  - Verify that Client ID and Client Secret are correct
  - Ensure there are no extra spaces or characters

#### "Connection failed" Error
- **Problem**: Authentication with Spotify failed
- **Solution**:
  - Check your internet connection
  - Verify the authorization code is complete and recent
  - Ensure your Spotify app redirect URI matches exactly: `http://localhost:8080/callback`
  - Try getting a new authorization code

#### "No data available" Messages
- **Problem**: Some features show no data
- **Solution**:
  - Some features require Spotify Premium
  - Ensure you have recent listening activity
  - Check that you granted all required permissions during authorization

#### JavaFX Runtime Issues
- **Problem**: Application won't start due to JavaFX issues
- **Solution**:
  - Ensure you have Java 11 or higher
  - Install JavaFX separately if needed
  - Use the Maven JavaFX plugin: `mvn javafx:run`

#### Browser Doesn't Open
- **Problem**: Clicking "Open Spotify Authorization" doesn't open browser
- **Solution**:
  - Manually copy the authorization URL from the status message
  - Paste it into your browser
  - Complete the authorization process

### Getting Help

If you encounter issues not covered here:

1. **Check the logs**: Look in `logs/spotify-connector.log` for detailed error messages
2. **Verify your setup**: Ensure all prerequisites are met
3. **Test your credentials**: Try creating a new Spotify app if needed
4. **Check Spotify API status**: Visit [Spotify Web API status page](https://status.spotify.com/)

## Security Notes

- **Never share your Client Secret** publicly
- **Keep your credentials secure** and don't commit them to version control
- **The application only stores data locally** and doesn't transmit it anywhere
- **All API communication uses HTTPS** for security

## Next Steps

Once you have the application running:

1. **Explore your data**: Use different time ranges to see how your music taste changes
2. **Export data**: The application displays data in tables that you can copy
3. **Customize**: Modify the code to add new features or change the interface
4. **Share**: Show your music statistics to friends and family

## Additional Resources

- [Spotify Web API Documentation](https://developer.spotify.com/documentation/web-api/)
- [JavaFX Documentation](https://openjfx.io/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [OAuth 2.0 Guide](https://oauth.net/2/)

# 🎵 Spotify Connector

A modern, professional Java application that connects to your Spotify account and provides detailed insights into your music listening habits. Built with JavaFX for a beautiful, responsive user interface.

## ✨ Features

### 🔐 **Seamless Authentication**
- **One-click Spotify authorization** with automatic callback handling
- **Dynamic port selection** to avoid conflicts
- **Secure OAuth 2.0 flow** with automatic token management
- **Real-time status updates** during the connection process

### 🎵 **Music Data Analytics**
- **Recently Played Tracks** - View your latest listening history with timestamps
- **Top Artists** - Discover your most-listened artists across different time periods
- **Top Tracks** - See your favorite songs with popularity metrics
- **Customizable time ranges** - short_term, medium_term, and long_term analysis
- **Flexible data limits** - Choose how many items to display (10, 20, or 50)

### 👤 **Enhanced User Profile**
- **Detailed profile information** with high-resolution profile images
- **Account statistics** including follower count and account status
- **External links** to your Spotify profile
- **Real-time data refresh** capabilities
- **Modern card-based layout** for better information presentation

### 🎨 **Modern User Interface**
- **Dark theme** inspired by Spotify's design language
- **Responsive layout** that adapts to different screen sizes
- **Smooth animations** and hover effects
- **Professional typography** and iconography
- **Intuitive navigation** with tabbed interface
- **Status indicators** with color-coded feedback

## 🚀 Quick Start

### Prerequisites
- **Java 11 or higher**
- **Maven 3.6+**
- **Spotify Developer Account** (free)

### 1. Clone the Repository
```bash
git clone <repository-url>
cd SpotifyConnector
```

### 2. Set Up Spotify Developer Account
1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Create a new app
3. Set the **Redirect URI** to: `http://127.0.0.1:8080/callback`
4. Copy your **Client ID** and **Client Secret**

### 3. Configure the Application
1. Copy `src/main/resources/spotify-default.properties` to `src/main/resources/spotify.properties`
2. Edit `spotify.properties` and add your credentials:
```properties
spotify.client.id=your_client_id_here
spotify.client.secret=your_client_secret_here
spotify.redirect.uri=http://127.0.0.1:8080/callback
spotify.scope=user-read-recently-played user-top-read user-read-private
```

### 4. Run the Application

#### Option A: Using Maven (Recommended)
```bash
mvn javafx:run
```

#### Option B: Using the provided scripts
**On macOS/Linux:**
```bash
chmod +x run.sh
./run.sh
```

**On Windows:**
```cmd
run.bat
```

#### Option C: macOS-specific script (with compatibility fixes)
```bash
chmod +x run-mac.sh
./run-mac.sh
```

## 🎯 How to Use

### 1. **Authentication**
- Click "🔗 Open Spotify Authorization"
- Authorize the application in your browser
- The authorization code will be captured automatically
- Click "✅ Connect to Spotify"

### 2. **Explore Your Data**
- **Recently Played**: View your latest listening history
- **Top Artists**: See your most-listened artists (with genres and popularity)
- **Top Tracks**: Discover your favorite songs (with duration and popularity)
- **Profile**: View detailed account information and statistics

### 3. **Customize Your View**
- Use the dropdown menus to change time ranges and data limits
- Click refresh buttons to update data in real-time
- Switch between tabs to explore different aspects of your music data

## 🛠️ Technical Details

### Architecture
- **MVC Pattern** with clear separation of concerns
- **Service Layer** for API communication
- **Model Classes** with Jackson JSON mapping
- **JavaFX Controllers** for UI management

### Key Technologies
- **JavaFX 21.0.1** - Modern UI framework
- **Jackson** - JSON serialization/deserialization
- **OkHttp** - HTTP client for API requests
- **SLF4J + Logback** - Logging framework
- **Maven** - Build and dependency management

### API Integration
- **Spotify Web API** for data retrieval
- **OAuth 2.0** for secure authentication
- **RESTful endpoints** for various data types
- **Error handling** with detailed logging

## 📁 Project Structure

```
SpotifyConnector/
├── src/main/java/com/spotifyconnector/
│   ├── config/          # Configuration management
│   ├── gui/             # JavaFX controllers and UI
│   ├── model/           # Data models (Track, Artist, User, etc.)
│   ├── service/         # API services and business logic
│   └── SpotifyConnectorApp.java
├── src/main/resources/
│   ├── com/spotifyconnector/gui/
│   │   └── spotify-connector.fxml  # UI layout
│   ├── styles.css       # Modern CSS styling
│   ├── spotify.properties  # Configuration file
│   └── logback.xml      # Logging configuration
├── run.sh              # Unix/Linux run script
├── run.bat             # Windows run script
├── run-mac.sh          # macOS-specific run script
└── README.md           # This file
```

## 🎨 UI Features

### Modern Design Elements
- **Dark theme** with Spotify-inspired color palette
- **Gradient effects** and subtle shadows
- **Rounded corners** and modern spacing
- **Icon integration** with emoji and Unicode symbols
- **Responsive tables** with hover effects
- **Status indicators** with color-coded feedback

### User Experience
- **One-click operations** for common tasks
- **Real-time feedback** during data loading
- **Error handling** with user-friendly messages
- **Keyboard shortcuts** and accessibility features
- **Smooth transitions** between different views

## 🔧 Configuration Options

### Time Ranges
- **short_term** - Last 4 weeks
- **medium_term** - Last 6 months
- **long_term** - Last several years

### Data Limits
- **10 items** - Quick overview
- **20 items** - Balanced view
- **50 items** - Comprehensive analysis

### Scopes
- `user-read-recently-played` - Access to recently played tracks
- `user-top-read` - Access to top artists and tracks
- `user-read-private` - Access to user profile information

## 🐛 Troubleshooting

### Common Issues

**1. "Address already in use" error**
- The application automatically tries different ports (8080-8084)
- If all ports are busy, restart your system or kill existing Java processes

**2. "Illegal character in query" error**
- This has been fixed with proper URL encoding
- Ensure you're using the latest version

**3. "Connection failed" error**
- Check your internet connection
- Verify your Spotify credentials are correct
- Ensure the redirect URI matches exactly: `http://127.0.0.1:8080/callback`

**4. JavaFX compatibility issues on macOS**
- Use the `run-mac.sh` script which includes compatibility flags
- Ensure you're using JavaFX 21.0.1 or higher

### Debug Mode
Enable debug logging by modifying `logback.xml`:
```xml
<logger name="com.spotifyconnector" level="DEBUG"/>
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spotify** for providing the excellent Web API
- **OpenJFX** team for the JavaFX framework
- **Jackson** project for JSON processing
- **OkHttp** team for the HTTP client library

## 📞 Support

If you encounter any issues or have questions:
1. Check the troubleshooting section above
2. Review the logs in the console output
3. Create an issue in the repository
4. Ensure you're using the latest version

---

**Made with ❤️ for music lovers and data enthusiasts**
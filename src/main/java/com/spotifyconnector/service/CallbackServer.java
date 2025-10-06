package com.spotifyconnector.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple HTTP server to capture the Spotify authorization callback
 */
public class CallbackServer {
    private static final Logger logger = LoggerFactory.getLogger(CallbackServer.class);
    
    private static final int[] PORTS = {8080, 8081, 8082, 8083, 8084};
    private static final String CALLBACK_PATH = "/callback";
    
    private ServerSocketChannel serverChannel;
    private ExecutorService executor;
    private CompletableFuture<String> authCodeFuture;
    private volatile boolean running = false;
    private int actualPort = -1;
    
    public CallbackServer() {
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    /**
     * Start the callback server and return a future that will complete with the authorization code
     */
    public CompletableFuture<String> start() {
        authCodeFuture = new CompletableFuture<>();
        
        executor.submit(() -> {
            try {
                serverChannel = ServerSocketChannel.open();
                
                // Try to bind to any available port
                boolean bound = false;
                for (int port : PORTS) {
                    try {
                        serverChannel.bind(new InetSocketAddress("127.0.0.1", port));
                        actualPort = port;
                        bound = true;
                        logger.info("Callback server started on port {}", port);
                        break;
                    } catch (IOException e) {
                        logger.debug("Port {} is not available, trying next port", port);
                        if (serverChannel.isOpen()) {
                            serverChannel.close();
                            serverChannel = ServerSocketChannel.open();
                        }
                    }
                }
                
                if (!bound) {
                    throw new IOException("Could not bind to any available port from " + java.util.Arrays.toString(PORTS));
                }
                
                running = true;
                
                while (running && !Thread.currentThread().isInterrupted()) {
                    try (SocketChannel clientChannel = serverChannel.accept()) {
                        handleRequest(clientChannel);
                    } catch (IOException e) {
                        if (running) {
                            logger.error("Error handling client request", e);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Error starting callback server", e);
                authCodeFuture.completeExceptionally(e);
            }
        });
        
        return authCodeFuture;
    }
    
    /**
     * Get the actual port the server is running on
     */
    public int getActualPort() {
        return actualPort;
    }
    
    /**
     * Stop the callback server
     */
    public void stop() {
        running = false;
        if (serverChannel != null) {
            try {
                serverChannel.close();
            } catch (IOException e) {
                logger.error("Error closing server channel", e);
            }
        }
        if (executor != null) {
            executor.shutdown();
        }
        logger.info("Callback server stopped");
    }
    
    private void handleRequest(SocketChannel clientChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);
        
        if (bytesRead > 0) {
            buffer.flip();
            String request = StandardCharsets.UTF_8.decode(buffer).toString();
            
            logger.debug("Received request: {}", request);
            
            // Parse the request to extract the authorization code
            String authCode = extractAuthCode(request);
            
            if (authCode != null) {
                // Send success response
                String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: text/html; charset=UTF-8\r\n" +
                                "Content-Length: 200\r\n" +
                                "\r\n" +
                                "<!DOCTYPE html>" +
                                "<html><head><title>Authorization Successful</title></head>" +
                                "<body>" +
                                "<h1>Authorization Successful!</h1>" +
                                "<p>You can close this window and return to the Spotify Connector application.</p>" +
                                "<p>The authorization code has been automatically captured.</p>" +
                                "</body></html>";
                
                clientChannel.write(ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
                
                // Complete the future with the authorization code
                authCodeFuture.complete(authCode);
                running = false; // Stop the server after capturing the code
            } else {
                // Send error response
                String response = "HTTP/1.1 400 Bad Request\r\n" +
                                "Content-Type: text/html; charset=UTF-8\r\n" +
                                "Content-Length: 100\r\n" +
                                "\r\n" +
                                "<!DOCTYPE html>" +
                                "<html><head><title>Authorization Error</title></head>" +
                                "<body><h1>Authorization Error</h1><p>No authorization code found.</p></body></html>";
                
                clientChannel.write(ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
            }
        }
    }
    
    private String extractAuthCode(String request) {
        try {
            // Look for the authorization code in the request
            String[] lines = request.split("\r\n");
            String requestLine = lines[0];
            
            if (requestLine.startsWith("GET " + CALLBACK_PATH)) {
                URI uri = URI.create("http://localhost" + requestLine.substring(4).split(" ")[0]);
                String query = uri.getQuery();
                
                if (query != null) {
                    String[] params = query.split("&");
                    for (String param : params) {
                        if (param.startsWith("code=")) {
                            return param.substring(5);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error extracting authorization code", e);
        }
        
        return null;
    }
}

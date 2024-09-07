package com.online.auction.config.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for setting up WebSocket support with STOMP (Streaming Text Oriented Messaging Protocol).
 * <p>
 * This class configures the message broker and WebSocket endpoints for handling WebSocket communication in the application.
 * It enables WebSocket message brokering and sets up STOMP endpoints for clients to connect to and send/receive messages.
 * </p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    /**
     * Configures the message broker with the required settings.
     * <p>
     * This method sets up the message broker to handle messages sent to destinations prefixed with "/topic".
     * It also sets the application destination prefix to "/app", which is used for client requests to the server.
     * </p>
     *
     * @param config The {@link MessageBrokerRegistry} used to configure the message broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers STOMP endpoints for WebSocket connections.
     * <p>
     * This method registers the WebSocket endpoints where clients can connect to. It sets the allowed origins for CORS (Cross-Origin Resource Sharing)
     * and enables SockJS fallback options for clients that do not support WebSocket.
     * </p>
     *
     * @param registry The {@link StompEndpointRegistry} used to register WebSocket endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins(allowedOrigins);
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins(allowedOrigins).withSockJS();
    }
}

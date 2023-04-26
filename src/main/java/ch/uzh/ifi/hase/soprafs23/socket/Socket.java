package ch.uzh.ifi.hase.soprafs23.socket;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * [KADEN] ADDED Socket.java for websocket feature
 *
 * Socket
 * This class is responsible for creating the socket configuration to communicate
 * with the client.
 *
 * https://www.toptal.com/java/stomp-spring-boot-websocket
 * https://ehsanasadev.github.io/Create_interactive_game_with_Spring_Boot_and_WebSocket/
 */

@Configuration
@EnableWebSocketMessageBroker
public class Socket implements WebSocketMessageBrokerConfigurer{

    private static final String WEBSOCKET_PREFIX = "/topic";
    private static final String WEBSOCKET_SUFFIX = "/sopra-websocket";
    private static final String ORIGIN_LOCALHOST = "http://localhost:3000";
    private static final String ORIGIN_PROD = "https://sopra-fs23-ta-m1-client.uc.r.appspot.com"; // Change to your GCP client url

    @Override
    public void configureMessageBroker(@NotNull MessageBrokerRegistry config) {
        config.enableSimpleBroker(WEBSOCKET_PREFIX);
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NotNull StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_SUFFIX)
                .setAllowedOrigins(ORIGIN_LOCALHOST, ORIGIN_PROD)
                .withSockJS();
    }
}
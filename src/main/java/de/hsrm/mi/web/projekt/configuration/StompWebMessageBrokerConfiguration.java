package de.hsrm.mi.web.projekt.configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Aktiviert den SimpleBroker für Destinations, die mit /topic beginnen
        registry.enableSimpleBroker("/topic");
        
        // Optional: Präfix für Nachrichten, die an @MessageMapping-Methoden geroutet werden
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registriert den STOMP-Endpunkt für Verbindungsanfragen
        registry.addEndpoint("/stompbroker");
        
        // Optional: Aktiviert SockJS-Fallback-Optionen, falls WebSocket nicht verfügbar ist
        registry.addEndpoint("/stompbroker").withSockJS();
    }
}

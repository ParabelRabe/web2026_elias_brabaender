package de.hsrm.mi.web.projekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FrontendNachrichtService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(FrontendNachrichtService.class);

    public FrontendNachrichtService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void sendEvent(FrontendNachrichtEvent ev) {
        String destination = "/topic/anzeige";
        logger.info("Versende STOMP-Nachricht an {}: Typ={}, ID={}, Operation={}", destination, ev.getTyp(), ev.getId(), ev.getArt());
        messagingTemplate.convertAndSend(destination, ev);
    }

}

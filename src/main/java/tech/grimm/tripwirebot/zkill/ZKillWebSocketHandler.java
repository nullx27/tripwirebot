package tech.grimm.tripwirebot.zkill;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ZKillWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;
    private ObjectMapper mapper = new ObjectMapper();
    private ApplicationEventPublisher eventPublisher;

    public ZKillWebSocketHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("ZKillWebSocketHandler afterConnectionEstablished");
        this.session = session;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        ZKillmail killmail = mapper.readValue(message.getPayload(), ZKillmail.class);
        System.out.println(killmail);

        eventPublisher.publishEvent(new KillmailEvent(this, killmail));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("ZKillWebSocketHandler afterConnectionClosed");
    }

    public void sendMessage(String message) throws Exception {
        this.session.sendMessage(new TextMessage(message));
        System.out.println("ZKillWebSocketHandler sendMessage: " + message);
    }
}
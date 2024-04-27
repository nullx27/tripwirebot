package tech.grimm.tripwirebot.zkill;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;

@Service
public class ZKillService {

    @Value("${tripwirebot.zkill-websocket-server}")
    private String zkillWebSocketServer;

    private WebSocketConnectionManager connectionManager;
    private StandardWebSocketClient webSocketClient;
    private ZKillWebSocketHandler handler;

    private ObjectMapper mapper;

    public ZKillService(ApplicationEventPublisher applicationEventPublisher) throws Exception {
        this.mapper = new ObjectMapper();

        this.webSocketClient = new StandardWebSocketClient();
        this.handler = new ZKillWebSocketHandler(applicationEventPublisher);
        this.connectionManager = new WebSocketConnectionManager(webSocketClient, handler, URI.create("wss://zkillboard.com/websocket/"));
        this.connectionManager.start();
    }

    public void subscribe(String systemID) throws Exception {
        ZKillWebsocketMessage subscription = new ZKillWebsocketMessage("sub", "system:" + systemID);
        String serialized = mapper.writeValueAsString(subscription);
        this.handler.sendMessage(serialized);
    }

    public void unsubscribe(String systemID) throws Exception {
        // TODO [AP]: verify the unsub action, can't really find any documentation about it but it looks like it works
        ZKillWebsocketMessage subscription = new ZKillWebsocketMessage("unsub", "system:" + systemID);
        String serialized = mapper.writeValueAsString(subscription);
        this.handler.sendMessage(serialized);
    }
}
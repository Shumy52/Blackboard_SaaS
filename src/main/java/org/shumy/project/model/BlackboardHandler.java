package org.shumy.project.model;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class BlackboardHandler extends TextWebSocketHandler {

    private static final Logger logger = Logger.getLogger(BlackboardHandler.class.getName());
    private Set<WebSocketSession> sessions = new HashSet<>();
    private List<String> drawingData = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished method called");
        logger.info("Connection established with session: " + session.getId());
        sessions.add(session);
        for (String data : drawingData) {
            session.sendMessage(new TextMessage(data));
        }
        logger.info("Sent existing drawing data to session: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        logger.info("Received message from session " + session.getId() + ": " + message.getPayload());
        // Store the drawing data
        drawingData.add(message.getPayload());
        // Broadcast incoming message to all sessions
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(message);
                logger.info("Sent message to session " + webSocketSession.getId());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Connection closed with session: " + session.getId() + ", status: " + status);
        sessions.remove(session);
    }
}

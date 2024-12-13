package org.shumy.project.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.springframework.security.core.GrantedAuthority;

public class BlackboardHandler extends TextWebSocketHandler {

    private static final Logger logger = Logger.getLogger(BlackboardHandler.class.getName());
    private Set<WebSocketSession> sessions = new HashSet<>();
    private List<String> drawingData = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection established with session: " + session.getId());
        sessions.add(session);
        for (String data : drawingData) {
            session.sendMessage(new TextMessage(data));
        }
        logger.info("Sent existing drawing data to session: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        if (session.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) session.getPrincipal();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("ROLE_ADMIN"::equals);

            if (isAdmin) {
                logger.info("Received message from session " + session.getId() + ": " + message.getPayload());
                drawingData.add(message.getPayload());
                for (WebSocketSession webSocketSession : sessions) {
                    if (webSocketSession.isOpen()) {
                        webSocketSession.sendMessage(message);
                        logger.info("Sent message to session " + webSocketSession.getId());
                    }
                }
            } else {
                logger.warning("Unauthorized drawing attempt by session " + session.getId());
                return;
            }
        } else {
            logger.warning("Session principal is not an instance of UsernamePasswordAuthenticationToken");
            return;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Connection closed with session: " + session.getId() + ", status: " + status);
        sessions.remove(session);
    }

    public void clearBoard() {
        drawingData.clear();
        TextMessage clearMessage = new TextMessage("{\"type\":\"clear\"}");
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(clearMessage);
                } catch (IOException e) {
                    logger.severe("Failed to send clear message to session " + session.getId());
                }
            }
        }
    }
}
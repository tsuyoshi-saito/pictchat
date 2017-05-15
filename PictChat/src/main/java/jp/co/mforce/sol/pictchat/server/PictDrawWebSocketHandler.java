package jp.co.mforce.sol.pictchat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class PictDrawWebSocketHandler extends TextWebSocketHandler {

	private final List<String> drawHistory = new ArrayList<>();

	private final Map<String, WebSocketSession> sessions = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println(String.format("online:", session.getId()));
		drawHistory.stream().map(TextMessage::new).forEach(t -> {
			try {
				session.sendMessage(t);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		sessions.put(session.getId(), session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		drawHistory.add(message.getPayload());
		broadcast(message.getPayload());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		System.out.println(String.format("remove:%s,status=", session.getId(), status.toString()));
		sessions.remove(session.getId());
	}

	private void broadcast(String payload) throws IOException {
		for (WebSocketSession session : sessions.values()) {
			if (session.isOpen()) {
				session.sendMessage(new TextMessage(payload));
			} else {
				sessions.remove(session.getId());
			}
		}
	}
}

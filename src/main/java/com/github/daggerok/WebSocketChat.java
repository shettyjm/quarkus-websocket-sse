package com.github.daggerok;

import org.jboss.logmanager.LogManager;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@ApplicationScoped
@ServerEndpoint("/chat/{username}")
public class WebSocketChat {

  private static final Logger log = LogManager.getLogManager().getLogger(WebSocketChat.class.getName());

  private Map<String, Session> sessions = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session, @PathParam("username") String username) {
    sessions.put(username, session);
    broadcast(String.format("%s joined.", username));
  }

  @OnClose
  public void onClose(Session session, @PathParam("username") String username) {
    sessions.remove(username);
    broadcast(String.format("%s left.", username));
  }

  @OnError
  public void onError(Session session, @PathParam("username") String username, Throwable error) {
    sessions.remove(username);
    broadcast(String.format("%s failed with: %s.", username, error.getLocalizedMessage()));
  }

  @OnMessage
  public void onMessage(String message, @PathParam("username") String username) {
    broadcast(String.format("%s: %s", username, message));
  }

  private void broadcast(String message) {
    log.info(message);
    sessions.forEach((username, session) ->
      session.getAsyncRemote().sendObject(message, sendResult -> {
        Throwable exception = sendResult.getException();
        if (Objects.nonNull(exception))
          log.warning(String.format("unable to send '%s' to '%s': %s",
                                    message, username, exception.getLocalizedMessage()));
      })
    );
  }
}

package server.handlers;

import commons.model.Message;
import server.WebSocketMainServerEndPoint;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

public interface ServerMessageHandler {
    void handle(Message message, Session session) throws IOException, EncodeException;
}

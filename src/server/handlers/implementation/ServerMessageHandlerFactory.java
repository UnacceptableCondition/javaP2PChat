package server.handlers.implementation;

import console.handlers.MessageHandler;
import commons.model.Message;
import server.WebSocketMainServerEndPoint;
import server.handlers.ServerMessageHandler;

import java.util.Date;

public class ServerMessageHandlerFactory {
    public static ServerMessageHandler getHandler(String mode) {
        ServerMessageHandler handler = null;
        switch (mode) {
            case "agentRegistry" :
                handler = agentRegistryHandler;
                break;
            case "clientRegistry" :
                handler = clientRegistryHandler;
        }
        return handler;
    }

    private static ServerMessageHandler agentRegistryHandler =
      (message, session) -> {
        WebSocketMainServerEndPoint.registry(0, session, message.getFrom(), message.getContent());
        session.getBasicRemote().sendObject(new Message(
                "server",
                "agentRegistry",
                "agentRegistry",
                new Date()
        ));
        session.close();
        WebSocketMainServerEndPoint.resolveConnection();
      };


    private static ServerMessageHandler clientRegistryHandler =
            (message, session) -> {
                WebSocketMainServerEndPoint.registry(1, session, message.getFrom(), null);
                WebSocketMainServerEndPoint.resolveConnection();
            };
}

package server;

import commons.endPoints.HandlerEndPoint;
import commons.model.Message;
import commons.model.User;
import commons.model.UsersPair;
import server.handlers.implementation.ServerMessageHandlerFactory;
import commons.service.data.ChatServerDao;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;

@ServerEndpoint(
        value="/ws",
        decoders = {commons.model.MessageDecoder.class},
        encoders = {commons.model.MessageEncoder.class}
        )
public class WebSocketMainServerEndPoint extends HandlerEndPoint {
    private static ChatServerDao data = new ChatServerDao();

    @OnOpen
    public void open(Session session) throws IOException, EncodeException {

    }

    @OnClose
    public void close(Session session) throws IOException, EncodeException {

    }

    @OnMessage
    public void handleMessage(Session session, Message message) throws IOException, EncodeException {
        ServerMessageHandlerFactory.getHandler(
                message.getType()
        ).handle(message, session);
    }

    public static void registry(int role, Session session, String name, String serverUri) {
        WebSocketMainServerEndPoint.data.saveUser(name, session, role, serverUri);
    }

    public static void resolveConnection () throws IOException, EncodeException {
        UsersPair pair = data.pollUsersPair();
        if (pair != null) {
            notifyClientAboutResolveConnection(pair);
        }

    }


    private static void notifyClientAboutResolveConnection (UsersPair pair) throws IOException, EncodeException {
        User admin = pair.getAdmin();
        User client = pair.getClient();
        client.getSession().getBasicRemote().sendObject( new Message(
                "server",
                admin.getServerUri(),
                "clientRegistry",
                new Date()
        ));
    }

}
package console;

import commons.endPoints.HandlerEndPoint;
import commons.model.ClientAppStates;
import commons.model.Message;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URISyntaxException;

@ServerEndpoint(
        value="/clientServer",
        decoders = {commons.model.MessageDecoder.class},
        encoders = {commons.model.MessageEncoder.class}
)
public class WebSocketConsoleServerEndpoint extends HandlerEndPoint {

    @OnOpen
    public void open(Session clientSession) {
        ChatCondition.state = ClientAppStates.CONVERSATION_AGENT;
        ConsoleClient.setCurrentSession(clientSession);
        ConsoleClient.rootLogger.info("opening websocket" + clientSession.getRequestURI());
        try {
            ConsoleClient.setupConsoleInput();
        } catch (Exception e) {
            ConsoleClient.rootLogger.error("Exception " + e);
        }
    }

    @OnClose
    public void onClose(Session clientSession) {
        if(ChatCondition.state == ClientAppStates.CONVERSATION_AGENT) {
            ConsoleClient.setCurrentSession(null);
            ConsoleClient.rootLogger.info("closing websocket" + clientSession.getRequestURI());
            ChatCondition.getCDL().countDown();
        }
    }

    @OnMessage
    public void msg(Message message){
        messageHandler.handle(message);
    }



}
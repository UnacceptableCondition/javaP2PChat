package console;

import commons.endPoints.HandlerEndPoint;
import commons.model.Message;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.*;

@ClientEndpoint(
        decoders = {commons.model.MessageDecoder.class},
        encoders = {commons.model.MessageEncoder.class}
)
public class WebSocketClientEndpoint extends HandlerEndPoint {

    public WebSocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        ConsoleClient.setCurrentSession(session);
        ConsoleClient.rootLogger.info("opening websocket" + session.getRequestURI());
    }

    @OnClose
    public void onClose(Session session) throws URISyntaxException {
        ConsoleClient.setCurrentSession(null);
        ConsoleClient.rootLogger.info("closing websocket" + session.getRequestURI());
    }

    @OnMessage
    public void onMessage(Message message) {
       messageHandler.handle(message);
    }

}
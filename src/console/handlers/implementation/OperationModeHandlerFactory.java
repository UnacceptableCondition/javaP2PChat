package console.handlers.implementation;

import commons.model.Message;
import console.ChatCondition;
import console.ConsoleClient;
import console.WebSocketClientEndpoint;
import console.WebSocketConsoleServerEndpoint;
import console.handlers.OperationModeHandler;
import commons.model.ClientAppStates;
import org.glassfish.tyrus.server.Server;

import java.net.URI;
import java.util.Date;
import java.util.Scanner;

public class OperationModeHandlerFactory {


    public OperationModeHandler getHandler(String mode) {
        return (mode.equals("server")) ? serverMode : clientMode;
    }

    private  OperationModeHandler serverMode = () -> {
        Server server = new Server(
                ChatCondition.CLIENT_WS_SERVER_HOST,
                ChatCondition.CLIENT_WS_SERVER_PORT,
                "",
                null,
                WebSocketConsoleServerEndpoint.class
        );
        WebSocketClientEndpoint.addMessageHandler(
                MessageHandlerFactory.getHandler("correspondence")
        );
        ChatCondition.setServer(server);
        server.start();
        ConsoleClient.freezeClientThread();
    };

    private  OperationModeHandler clientMode = () -> {
        ConsoleClient.rootLogger.info("Setup connection with agent");
        WebSocketClientEndpoint.addMessageHandler(
                message -> System.out.println(message.getContent())
        );
        new WebSocketClientEndpoint(
                   URI.create(ChatCondition.getAgentUri())
        );
        WebSocketClientEndpoint.addMessageHandler(
                MessageHandlerFactory.getHandler("correspondence")
        );
        ConsoleClient.setupConsoleInput();
    };

}

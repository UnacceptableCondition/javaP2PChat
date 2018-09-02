package console;


import console.handlers.MessageHandler;
import console.handlers.OperationModeHandler;
import console.handlers.implementation.MessageHandlerFactory;
import console.handlers.implementation.OperationModeHandlerFactory;
import commons.model.ClientAppStates;
import commons.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class ConsoleClient {

    public static Logger rootLogger;
    private static Session currentSession;

    static {
      //  property.PropertyHandler propertyHandler = new property.PropertyHandler();
        rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        setupAppState(keyboard);
        setupApplication();
    }

    private static void setupAppState (BufferedReader keyboard) throws IOException {
        String[] line;
        while(true) {
            line = keyboard.readLine().split(" ");
            ChatCondition.state = line[0].contains(ChatCondition.AGENT_PATTERN) ?
                    ClientAppStates.AGENT :
                    line[0].contains(ChatCondition.CLIENT_PATTERN) ?
                            ClientAppStates.CLIENT :
                            ClientAppStates.INIT;
            if(line.length > 1) {
                ChatCondition.userName = line[1];
            } else {
                ChatCondition.userName = "TestName";
            }
            if(ChatCondition.state != ClientAppStates.INIT) {
                break;
            }
        }
    }

    public static void setupApplication () {
        String modeAlias;
        try {
            registryOnServer(
                    MessageHandlerFactory.getHandler("registry"),
                    ChatCondition.SERVER_WS_URI,
                    ChatCondition.state,
                    ChatCondition.userName
            );
            freezeClientThread();
            modeAlias = getOperationModeAlias();
            rootLogger.info("setup mode: " + modeAlias);
            startOperatingMode(modeAlias);
            rootLogger.info(ChatCondition.state + " ");
            if(ChatCondition.state == ClientAppStates.CONVERSATION_AGENT) {
                reloadAgentConnection();
            }
        } catch (Exception e) {
            rootLogger.error("Exception: " + e);
        }
    }

    private static void registryOnServer (MessageHandler serverAnswerHandler, String uri, ClientAppStates state, String userName) throws URISyntaxException, IOException, EncodeException {
        new WebSocketClientEndpoint(new URI(uri));
        WebSocketClientEndpoint.addMessageHandler(serverAnswerHandler);

        rootLogger.info("Setup server connection for registry");
        String typeOfMessage = (state == ClientAppStates.AGENT) ?
                ChatCondition.AGENT_REGISTRY_PATTERN :
                ChatCondition.CLIENT_REGISTRY_PATTERN;

        currentSession.getBasicRemote().sendObject(
                getControlMessage(
                        typeOfMessage,
                        userName,
                        ChatCondition.getLocalServerWsPath()
                        ));

        ChatCondition.state = (state == ClientAppStates.AGENT) ?
                ClientAppStates.PENDING_AGENT :
                ClientAppStates.PENDING_CLIENT;
    }

    private static void startOperatingMode (String mode) throws URISyntaxException, DeploymentException, InterruptedException, IOException, EncodeException {
        OperationModeHandlerFactory factory = new OperationModeHandlerFactory();
        OperationModeHandler handler = factory.getHandler(mode);
        handler.setup();
    }

    public static void freezeClientThread() throws InterruptedException {
        CountDownLatch cld = new CountDownLatch(1);
        ChatCondition.setCDL(cld);
        cld.await();
    }


    private static Message getControlMessage (String type, String userName, String content) {
        return new Message(
                userName,
                content,
                type,
                new Date());
    }

    private static String getOperationModeAlias () {
        String alias = "server";
        switch (ChatCondition.state) {
            case CONVERSATION_CLIENT:
                alias = "client";
                break;
            case PENDING_AGENT:
                alias = "server";
                break;
        }
        return alias;
    }

    public static void setupConsoleInput()  {
        Thread inputThread = new Thread(() -> {
            while(true) {
                BufferedReader consoleReader = new BufferedReader(
                        new InputStreamReader(System.in)
                );
                try {
                    String line = consoleReader.readLine();
                    if(line.equals(ChatCondition.CLOSE_CONVERSATION_ALIAS) ||
                            currentSession == null || !currentSession.isOpen()
                            ) {
                            closeConnection();
                            break;
                        }
                    sendMessage(line);
                } catch (Exception e) {
                    rootLogger.error("Exception: " + e);
                }
            }
        });
        ChatCondition.setInputThread(inputThread);
        inputThread.start();
    }

    public static void setCurrentSession(Session currentSession) {
        ConsoleClient.currentSession = currentSession;
    }

    private static void closeConnection() throws IOException {
        rootLogger.info("Close connection");
        if(ChatCondition.state == ClientAppStates.CONVERSATION_AGENT) {
            ChatCondition.state = ClientAppStates.CLOSE;
            ChatCondition.getCDL().countDown();
        }
        if(currentSession != null && currentSession.isOpen()) {
            currentSession.close();
        }

    }

    private static void reloadAgentConnection () {
        ChatCondition.state = ClientAppStates.AGENT;
        ChatCondition.getServer().stop();
        setupApplication();

    }

    private static void sendMessage(String content) throws IOException, EncodeException {
        currentSession.getBasicRemote().sendObject(new Message(
                ChatCondition.userName,
                content,
                "message",
                new Date()
        ));
    }
}
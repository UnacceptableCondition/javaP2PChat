package console;

import commons.model.ClientAppStates;
import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.util.concurrent.CountDownLatch;

public class ChatCondition {

    private static Server server;
    private static String agentUri;
    private static CountDownLatch CDL;
    private static BufferedReader consoleReader;
    private static Thread inputThread;

    public static final String AGENT_PATTERN  = "agent";
    public static final String CLIENT_PATTERN  = "client";
    public static final String AGENT_REGISTRY_PATTERN  = "agentRegistry";
    public static final String CLIENT_REGISTRY_PATTERN  = "clientRegistry";
    public static String SERVER_WS_URI = "ws://localhost:8080/ChatTest_war_exploded/ws";
    public static String CLIENT_WS_SERVER_HOST = "localhost";
    public static String CLIENT_WS_SERVER_PATH = "/clientServer";
    public static int CLIENT_WS_SERVER_PORT = 9090;
    public static final String CLOSE_CONVERSATION_ALIAS = "close";


    public static ClientAppStates state;
    public static String userName;


    public static Server getServer() {
        return server;
    }

    public static void setServer(Server server) {
        ChatCondition.server = server;
    }

    public static String getAgentUri() {
        return agentUri;
    }

    public static void setAgentUri(String agentUri) {
        ChatCondition.agentUri = agentUri;
    }

    public static CountDownLatch getCDL() {
        return CDL;
    }

    public static void setCDL(CountDownLatch serverCDL) {
        ChatCondition.CDL = serverCDL;
    }

    public static String getLocalServerWsPath () {
        return "ws://" + ChatCondition.CLIENT_WS_SERVER_HOST + ":"
                + ChatCondition.CLIENT_WS_SERVER_PORT
                + ChatCondition.CLIENT_WS_SERVER_PATH;
    }


    public static Thread getInputThread() {
        return inputThread;
    }

    public static void setInputThread(Thread inputThread) {
        ChatCondition.inputThread = inputThread;
    }
}

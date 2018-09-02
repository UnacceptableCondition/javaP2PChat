package commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import property.PropertyHandler;

import java.io.*;
import java.net.*;

public class Server {

    private static int serverPort;
    private static byte[] serverHost;
    private static UserQueue agentQueue;
    private static UserQueue clientQueue;
    private static Logger rootLogger;

    private static boolean isRunning = true;

    static {
        PropertyHandler propertyHandler = new PropertyHandler();
        serverPort = propertyHandler.getServerPort();
        serverHost = propertyHandler.getServerHost();
        agentQueue = new UserQueue();
        clientQueue = new UserQueue();

        rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void main(String[] arg) {
        ServerSocket srvSocket = null;
        try {
            try {
                InetAddress serverAddress = InetAddress.getByAddress(serverHost);
                srvSocket = new ServerSocket(serverPort, 0, serverAddress);
                while(isRunning) {
                    Socket socket = srvSocket.accept();
                    rootLogger.info(socket.getInetAddress() + " " + socket.getPort());
                    new UserRequestHandler().setSocket(socket);
                }
            } catch(Exception e) {
                rootLogger.error("Exception : " + e);
            }
        } finally {
            try {
                if (srvSocket != null)
                    srvSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public static UserQueue getAgentQueue () {
        return agentQueue;
    }

    public static UserQueue getClientQueue () {
        return clientQueue;
    }

    public static Logger getLogger() {
        return rootLogger;
    }

    @Override
    public void finalize() {
        isRunning = false;
    }
}
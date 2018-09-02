//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import property.PropertyHandler;
//
//import java.io.*;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class Client {
//
//    private static  int serverPort;
//    private static  byte[] serverHost;
//
//    private static  int localPort;
//    private static  byte[] localServerHost;
//    private static  byte[] localClientHost;
//
//    private static final String AGENT_PATTERN  = "agent";
//    private static final String CLIENT_PATTERN  = "console";
//
//    private static DataOutputStream dataOutputStream = null;
//    private static DataInputStream dataInputStream = null;
//    private static Socket socket = null;
//
//    private static Logger rootLogger;
//
//
//    private static ServerSocket serverSocket = null;
//
//    static {
//        PropertyHandler propertyHandler = new PropertyHandler();
//        serverPort = propertyHandler.getServerPort();
//        serverHost = propertyHandler.getServerHost();
//
//        localServerHost = propertyHandler.getLocalServerHost();
//        localClientHost = propertyHandler.getLocalClientHost();
//        localPort = propertyHandler.getLocalPort();
//
//        rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
//    }
//
//
//    public static void main(String args[]) throws IOException {
//        start();
//    }
//
////    public static void start() throws IOException {
////        InputStreamReader inputStreamReaderForKeyboard = new InputStreamReader(System.in);
////        BufferedReader keyboard = new BufferedReader(inputStreamReaderForKeyboard);
////        int userPattern = getUserRole(keyboard);
////        if(userPattern == 1) {
////            try {
////                setupConnection(serverHost, serverPort, localClientHost, localPort);
////                serverRegistration(CLIENT_PATTERN);
////            } catch (IOException e) {
////                rootLogger.error("Exception : " + e);
////            }
////        }
////        if(userPattern == 0) {
////            try {
////                setupConnection(serverHost, serverPort, localServerHost, localPort);
////                serverRegistration(AGENT_PATTERN);
////            } catch (IOException e) {
////                rootLogger.error("Exception : " + e);
////            }
////        }
////    }
//
//    private static int getUserRole (BufferedReader keyboard) throws IOException {
//        String line;
//        int userPattern;
//        while(true) {
//            line = keyboard.readLine();
//            userPattern = line.contains(AGENT_PATTERN) ? 0 :
//                    line.contains(CLIENT_PATTERN) ? 1 : -1;
//            if(userPattern != -1) {
//                break;
//            }
//        }
//        return userPattern;
//    }
//
//    protected static void setupServer(final byte[] host, final int port, final String key) {
//        new Thread(() -> {
//            int adminPort;
//            byte agentHost[] = new byte[4];
//            try {
//                InetAddress serverAddress = InetAddress.getByAddress(host);
//                serverSocket = new ServerSocket(port, 0, serverAddress);
//                    socket = serverSocket.accept();
//                    rootLogger.info(socket.getInetAddress().toString());
//                    setupInputOutputStreams(socket);
//                    if(key.equals(AGENT_PATTERN)) {
//                        setupAgentConnection(socket);
//                    } else {
//                        adminPort = dataInputStream.readInt();
//                        dataInputStream.read(agentHost);
//                        socket.close();
//                        serverSocket.close();
//                        startConversation(agentHost, adminPort);
//                }
//            } catch(Exception e) {
//                rootLogger.error("Exception : " + e);
//            }
//        }).start();
//    }
//
//    protected static void setupAgentConnection(Socket socket) throws IOException {
//        setupReceiverThread();
//        try {
//            InputStream  sin  = socket.getInputStream();
//            DataInputStream  dis = new DataInputStream (sin );
//            String line;
//            while(true) {
//                line = dis.readUTF();
//                System.out.println(line);
//                if (line.equalsIgnoreCase("quit")) {
//                    socket.close();
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            socket.close();
//            serverSocket.close();
//            setupConnection(serverHost, serverPort, localServerHost, localPort);
//            serverRegistration(AGENT_PATTERN);
//        }
//    }
//
//    protected static void setupReceiverThread () {
//        Thread receiverThread = new Thread(() -> {
//            String line;
//            BufferedReader consoleReader = new BufferedReader(
//                    new InputStreamReader(System.in)
//            );
//            try {
//                while (true) {
//                    line = consoleReader.readLine();
//                    if (line.endsWith("quit")) {
//                        socket.close();
//                        break;
//                    }
//                    dataOutputStream.writeUTF(line);
//                    dataOutputStream.flush();
//                }
//            } catch (Exception e) {
//                rootLogger.error("Exception : " + e);
//            }
//        });
//        receiverThread.start();
//    }
//
//    protected static void setupSenderThread () {
//        Thread senderThread = new Thread(() -> {
//            String line;
//            try {
//                while (true) {
//                    line = dataInputStream.readUTF();
//                    System.out.println(line);
//                    if (line.endsWith("quit")) {
//                        socket.close();
//                        break;
//                    }
//                }
//            } catch (Exception e) {
//                rootLogger.error("Exception : " + e);
//            }
//        });
//        senderThread.start();
//    }
//
//    protected static void setupConnection(byte[] serverIP, int serverPort, byte[] localIP, int localPort) throws IOException {
//        InetAddress serverAddress = InetAddress.getByAddress(serverIP);
//        InetAddress localAddress = InetAddress.getByAddress(localIP);
//        socket = new Socket(serverAddress, serverPort, localAddress, localPort);
//        setupInputOutputStreams(socket);
//    }
//
//    protected static void serverRegistration(final String key) {
//        try{
//            Thread receiverThread = new Thread(() -> {
//                try {
//                    dataOutputStream.writeUTF(key);
//                    socket.close();
//                    if(key.equals(CLIENT_PATTERN)) {
//                        setupServer(localClientHost, localPort, CLIENT_PATTERN);
//                    }
//                    if(key.equals(AGENT_PATTERN)) {
//                        setupServer(localServerHost, localPort, AGENT_PATTERN);
//                    }
//                } catch (Exception e) {
//                    rootLogger.error("Exception : " + e);
//                }
//            });
//            receiverThread.start();
//        } catch (Exception e) {
//            rootLogger.error("Exception : " + e);
//        }
//    }
//
//    protected static void setupInputOutputStreams(Socket socket) throws IOException {
//        dataOutputStream = new DataOutputStream(
//                socket.getOutputStream()
//        );
//        dataInputStream = new DataInputStream(
//                socket.getInputStream()
//        );
//    }
//
//    protected static void startConversation (byte[] agentHost, int adminPort) throws IOException {
//        setupConnection(agentHost, adminPort, localClientHost, localPort);
//        setupReceiverThread();
//        setupSenderThread();
//    }
//
//
//}

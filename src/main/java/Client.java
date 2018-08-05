import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    private static final int serverPort  = 6666; // открываемый порт сервера
    private static final byte[] serverHost  = new byte[] {127, 0, 0, 6};

    private static final int localServerPort  = 7666; // открываемый порт локального сервера
    private static final byte[] localServerHost  = new byte[] {127, 0, 0, 7};

    private static final int localClientPort  = 5666; // открываемый порт локального сервера для клиента
    private static final byte[] localClientHost  = new byte[] {127, 0, 0, 5};

    private static final String AGENT_PATTERN  = "agent";
    private static final String CLIENT_PATTERN  = "client";

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private static Socket socket = null;

    private static ServerSocket serverSocket = null;


    public static void main(String args[]) throws IOException {
        String line;
        int userPattern;
        InputStreamReader inputStreamReaderForKeyboard = new InputStreamReader(System.in);
        BufferedReader keyboard = new BufferedReader(inputStreamReaderForKeyboard);
        while(true) {
            line = keyboard.readLine();
            userPattern = line.contains(AGENT_PATTERN) ? 0 :
                    line.contains(CLIENT_PATTERN) ? 1 : -1;
            if(userPattern != -1) {
                break;
            }
        }
        if(userPattern == 1) {
            try {
                setupConnection(serverHost, serverPort, localClientHost, localClientPort);
                serverRegistration(CLIENT_PATTERN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(userPattern == 0) {
            try {
                setupConnection(serverHost, serverPort, localServerHost, localServerPort);
                serverRegistration(AGENT_PATTERN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setupServer(final byte[] host, final int port, final String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int adminPort = 0;
                byte agentHost[] = new byte[4];
                try {
                    InetAddress serverAddress = InetAddress.getByAddress(host);
                    serverSocket = new ServerSocket(port, 0, serverAddress);
                        socket = serverSocket.accept();
                        setupInputOutputStreams(socket);
                        if(key.equals(AGENT_PATTERN)) {
                            setupAgentConnection(socket);
                        } else {
                            adminPort = dataInputStream.readInt();
                            dataInputStream.read(agentHost);
                            socket.close();
                            serverSocket.close();
                            startConversation(agentHost, adminPort);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void setupAgentConnection(Socket socket) throws IOException {
        setupReceiverThread();
        try {
            InputStream  sin  = socket.getInputStream();
            DataInputStream  dis = new DataInputStream (sin );
            String line = null;
            while(true) {
                line = dis.readUTF();
                System.out.println(line);
                if (line.equalsIgnoreCase("quit")) {
                    socket.close();
                    break;
                }
            }
        } catch (Exception e) {
            socket.close();
            serverSocket.close();
            setupConnection(serverHost, serverPort, localServerHost, localServerPort);
            serverRegistration(AGENT_PATTERN);
        }
    }

    private static void setupReceiverThread () {
        Thread receiverThread = new Thread(new Runnable() {
            public void run() {
                String line = null;
                BufferedReader consoleReader = new BufferedReader(
                        new InputStreamReader(System.in)
                );
                try {
                    while (true) {
                        line = consoleReader.readLine();
                        if (line.endsWith("quit")) {
                            socket.close();
                            break;
                        }
                        dataOutputStream.writeUTF(line);
                        dataOutputStream.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiverThread.start();
    }

    private static void setupSenderThread () {
        Thread senderThread = new Thread(new Runnable() {
            public void run() {
                String line = null;
                try {
                    while (true) {
                        line = dataInputStream.readUTF();
                        System.out.println(line);
                        if (line.endsWith("quit")) {
                            socket.close();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        senderThread.start();
    }

    private static void setupConnection(byte[] serverIP, int serverPort, byte[] localIP, int localPort) throws IOException {
        InetAddress serverAddress = InetAddress.getByAddress(serverIP);
        InetAddress localAddress = InetAddress.getByAddress(localIP);
        socket = new Socket(serverAddress, serverPort, localAddress, localPort);
        setupInputOutputStreams(socket);
    }

    private static void serverRegistration(final String key) {
        try{
            Thread receiverThread = new Thread(new Runnable(){
                public void run() {
                    try {
                        dataOutputStream.writeUTF(key);
                        socket.close();
                        if(key.equals(CLIENT_PATTERN)) {
                            setupServer(localClientHost, localClientPort, CLIENT_PATTERN);
                        }
                        if(key.equals(AGENT_PATTERN)) {
                            setupServer(localServerHost, localServerPort, AGENT_PATTERN);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            receiverThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupInputOutputStreams(Socket socket) throws IOException {
        dataOutputStream = new DataOutputStream(
                socket.getOutputStream()
        );
        dataInputStream = new DataInputStream(
                socket.getInputStream()
        );
    }

    private static void startConversation (byte[] agentHost, int adminPort) throws IOException {
        setupConnection(agentHost, adminPort, localClientHost, localClientPort);
        setupReceiverThread();
        setupSenderThread();
    }


}

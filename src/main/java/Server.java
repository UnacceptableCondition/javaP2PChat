import java.io.*;
import java.net.*;

public class Server {
    private static final int serverPort  = 6666; // открываемый порт сервера
    private static final  byte[] serverHost  = new byte[] {127, 0, 0, 6};

    private static UserQueue agentQueue;
    private static UserQueue clientQueue;


    static {
        agentQueue = new UserQueue();
        clientQueue = new UserQueue();
    }

    public static void main(String[] arg) {
        ServerSocket srvSocket = null;
        new ConnectionManager().start();
        try {
            try {
                InetAddress serverAddress = InetAddress.getByAddress(serverHost);
                srvSocket = new ServerSocket(serverPort, 0, serverAddress);
                while(true) {
                    Socket socket = srvSocket.accept();
                    System.out.println(socket.getInetAddress() + " " + socket.getPort());
                    new UserRequestHandler().setSocket(socket);
                }
            } catch(Exception e) {
                System.out.println("Exception : " + e);
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

    @Override
    public  void finalize() {
        ConnectionManager.close();
    }
}
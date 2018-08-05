import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectionManager extends Thread {

    private static boolean isExecute = false;

    public void run () {
        isExecute = true;
        System.out.println("connectionManager setup");
        while(isExecute) {
            System.out.println(Server.getAgentQueue().hasFreeUser() + " " + Server.getClientQueue().hasFreeUser());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Server.getAgentQueue().hasFreeUser() &&
                    Server.getClientQueue().hasFreeUser()) {
                try {
                    System.out.println("setup new connection");
                    setupConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized static void setupConnection () throws IOException {
        User client = Server.getClientQueue().getUser();
        User agent = Server.getAgentQueue().getUser();

        InetAddress clientAddress = InetAddress.getByAddress(client.getHost());
        Socket clientSocket = new Socket(clientAddress, client.getPort());

        DataOutputStream dataOutputStream = new DataOutputStream(
                clientSocket.getOutputStream()
        );

        dataOutputStream.writeInt(agent.getPort());
        dataOutputStream.write(agent.getHost());

        dataOutputStream.flush();
        clientSocket.close();
    }

    public static void close() {
        isExecute = false;
    }



}

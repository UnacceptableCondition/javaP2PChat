package commons.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectionManager {

    public static void checkNewConnection () throws IOException {
        if(hasFreeUserCouple()) {
            setupConnection();
        }
    }

    private static boolean hasFreeUserCouple () {
        return (Server.getAgentQueue().hasFreeUser() &&
                Server.getClientQueue().hasFreeUser());
    }

    private synchronized static void setupConnection () throws IOException {
        Server.getLogger().info("Setup new connection");
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

}

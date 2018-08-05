import java.io.DataInputStream;
import java.net.Socket;

public class UserRequestHandler extends Thread {

    private Socket socket;

    public void setSocket(Socket socket)
    {
        this.socket = socket;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream (
                    socket.getInputStream()
            );
            String line;
            while(true) {
                line = dataInputStream.readUTF();
                if(line.equalsIgnoreCase( "client")) {
                    Server.getClientQueue().registerUser(
                            socket.getInetAddress().getAddress() ,
                            socket.getPort()
                    );
                } else if (line.equalsIgnoreCase("agent")) {
                    Server.getAgentQueue().registerUser(
                            socket.getInetAddress().getAddress() ,
                            socket.getPort()
                    );
                }
                socket.close();
                break;
            }
        } catch(Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}

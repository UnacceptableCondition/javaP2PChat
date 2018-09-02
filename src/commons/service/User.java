package commons.service;

public class User {

    private int port;
    private byte host[];

    public User (byte host[], int port) {
        this.host = host;
        this.port = port;
    }

    public byte[] getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(byte[] host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}

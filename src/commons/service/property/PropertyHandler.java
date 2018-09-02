package property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyHandler {

    private static final String PATH_TO_PROPERTIES = "src/main/resources/config.properties";

    private byte serverHost[];
    private byte localServerHost[];
    private byte localClientHost[];

    private int serverPort;
    private int localPort;

    private static Properties prop;

    public PropertyHandler () {
        setup();
    }

    private void setup() {
        FileInputStream fileInputStream;
        prop = new Properties();

        try {
            fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
            prop.load(fileInputStream);
            this.readPropertiesFile();

        } catch (IOException e) {
            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружен");
            e.printStackTrace();
        }
    }

    private void readPropertiesFile () {
        serverHost = readIP("serverHost", ",");
        serverPort = readPort("serverPort");

        localServerHost = readIP("localServerHost", ",");
        localClientHost = readIP("localClientHost", ",");
        localPort = readPort("localPort");
    }

    private  byte[] serverHostStringToByte (String serverHost[]) {
        byte hosts[] = new byte[4];
        for(int i = 0; i < 4; i++) {
            hosts[i] = Byte.parseByte(serverHost[i]);
        }
        return hosts;
    }

    private  byte[] readIP(String ipPropertyName, String splitRegex) {
        String currentProperty[] = prop.getProperty(ipPropertyName).split(splitRegex);
        return serverHostStringToByte(currentProperty);
    }

    private  int readPort(String portPropertyName) {
        String currentProperty = prop.getProperty(portPropertyName);
        return Integer.parseInt(currentProperty);

    }

    public byte[] getServerHost() {
        return serverHost;
    }

    public byte[] getLocalServerHost() {
        return localServerHost;
    }

    public byte[] getLocalClientHost() {
        return localClientHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getLocalPort() {
        return localPort;
    }
}
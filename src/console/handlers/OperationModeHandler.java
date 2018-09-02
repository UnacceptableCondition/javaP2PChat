package console.handlers;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface OperationModeHandler {
    void setup() throws DeploymentException, URISyntaxException, InterruptedException, IOException, EncodeException;
}

package commons.model;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {


    @Override
    public String encode(Message message) throws EncodeException {
        return Json.createObjectBuilder()
                .add("content", message.getContent())
                .add("from", message.getFrom())
                .add("type", message.getType())
                .build()
                .toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
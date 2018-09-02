package commons.model;



import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.Date;

public class MessageDecoder implements Decoder.Text<Message> {


    @Override
    public Message decode(final String s) throws DecodeException {
        Message chatMessage = new Message();
        JsonObject obj = Json.createReader(new StringReader(s))
                .readObject();
        chatMessage.setContent(obj.getString("content"));
        chatMessage.setFrom(obj.getString("from"));
        chatMessage.setType(obj.getString("type"));
        chatMessage.setDate(new Date());
        return chatMessage;
    }

    @Override
    public boolean willDecode(String s) {
        return (true);
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
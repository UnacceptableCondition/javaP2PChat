package commons.endPoints;

import console.handlers.MessageHandler;

public class HandlerEndPoint {
    protected static MessageHandler messageHandler;

    public static void addMessageHandler(MessageHandler msgHandler) {
        messageHandler = msgHandler;

    }
}

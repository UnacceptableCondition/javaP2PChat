package console.handlers.implementation;


import console.ChatCondition;
import console.handlers.MessageHandler;
import commons.model.ClientAppStates;

import java.util.Date;

public class MessageHandlerFactory {

    public static MessageHandler getHandler(String mode) {
        MessageHandler handler = null;
        switch (mode) {
            case "registry" :
               handler = registryHandler;
               break;
            case "correspondence" :
               handler = correspondenceHandler;
               break;
        }
        return handler;
    }

    private static  MessageHandler registryHandler = (message) -> {
        System.out.println("registryHandler");
        switch (message.getType()) {
            case ChatCondition.AGENT_REGISTRY_PATTERN:
                ChatCondition.state = ClientAppStates.PENDING_AGENT;
                break;
            case ChatCondition.CLIENT_REGISTRY_PATTERN:
                ChatCondition.setAgentUri(message.getContent());
                ChatCondition.state = ClientAppStates.CONVERSATION_CLIENT;
                break;
        }
        ChatCondition.getCDL().countDown();
    };

    private static  MessageHandler correspondenceHandler = (message) -> {
        System.out.println(new Date() + ": "+ message.getFrom() + ": " + message.getContent());
    };


}

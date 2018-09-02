package commons.model;

public enum ClientAppStates {
    INIT,
    AGENT,
    CLIENT,
    PENDING_AGENT,
    PENDING_CLIENT,
    CONVERSATION_AGENT, // as agent
    CONVERSATION_CLIENT, // as console
    CLOSE;
}

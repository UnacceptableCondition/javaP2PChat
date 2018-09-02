package commons.model;

import javax.websocket.Session;

public class User {

    private String name;
    private Session session;
    // 1 = console, 0 = agent
    private int role;
    private String serverUri;

    public User(String name, Session session, int role) {
        this.role = role;
        this.name = name;
        this.session = session;
    }

    public User(String name, Session session, int role, String serverUri) {
        this.role = role;
        this.name = name;
        this.session = session;
        this.serverUri = serverUri;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getServerUri() {
        return serverUri;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }
}

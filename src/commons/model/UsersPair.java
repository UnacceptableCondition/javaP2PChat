package commons.model;

public class UsersPair {
    private User admin;
    private User client;

    public UsersPair(User admin, User client) {
        this.admin = admin;
        this.client = client;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }
}

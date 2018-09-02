package commons.service;

import java.util.*;

public class UserQueue {

    private final Queue<User> users = new LinkedList<>();

    public User getUser () {
        synchronized(users) {
            if(!users.isEmpty()) {
                return users.poll();
            } else {
                return null;
            }
        }
    }

    public boolean hasFreeUser () {
        return !users.isEmpty();
    }

    public void  registerUser (byte[] host, int agentPort) {
        synchronized(users) {
            users.add(new User(host, agentPort));
        }
    }


}

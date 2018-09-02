package commons.service.data;


import commons.model.User;

import javax.websocket.Session;
import java.util.LinkedList;
import java.util.Queue;

public class UserQueue {

    private final Queue<User> users = new LinkedList<>();

    public User getUser () {
            if(!users.isEmpty()) {
                return users.poll();
            } else {
                return null;
            }
    }

    public boolean hasFreeUser () {
        return !users.isEmpty();
    }

    public void  registerUser (String name, Session session, int role, String serverUri) {
        users.add(new User(name, session, role, serverUri));
    }


}

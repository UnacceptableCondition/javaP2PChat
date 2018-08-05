import java.util.*;

public class UserQueue {

    private Queue<User> users = new LinkedList<>();

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

    public void registerUser (byte[] host, int agentPort) {
        users.add(new User(host, agentPort));
    }


}

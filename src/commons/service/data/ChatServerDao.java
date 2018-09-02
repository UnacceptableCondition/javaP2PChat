package commons.service.data;

import commons.model.User;
import commons.model.UsersPair;

import javax.websocket.Session;

public class ChatServerDao implements AbstractChatServerDao {

    private UserQueue agentQueue = new UserQueue();
    private UserQueue clientQueue = new UserQueue();

    @Override
    public synchronized UsersPair pollUsersPair() {
        if(agentQueue.hasFreeUser() && clientQueue.hasFreeUser()) {
            return new UsersPair(
                    agentQueue.getUser(),
                    clientQueue.getUser()
            );
        }
        return null;
    }

    @Override
    public synchronized void saveUser(User user) {
        DataHandler userRegistration = (user.getRole() == 0)
                ? () -> agentQueue.registerUser(
                        user.getName(),
                        user.getSession(),
                        user.getRole(),
                        user.getServerUri()
                    )
                : () -> clientQueue.registerUser(
                        user.getName(),
                        user.getSession(),
                        user.getRole(),
                        user.getServerUri()
                    );
        userRegistration.execute();
    }

    @Override
    public synchronized void saveUser(String name, Session session, int role, String serverUri) {
        DataHandler userRegistration = (role == 0)
                ? () -> agentQueue.registerUser(name, session, role, serverUri)
                : () -> clientQueue.registerUser(name, session, role, serverUri);
        userRegistration.execute();
    }


    private interface DataHandler {
        void execute();
    }
}

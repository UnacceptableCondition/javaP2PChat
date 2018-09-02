package commons.service.data;

import commons.model.User;
import commons.model.UsersPair;

import javax.websocket.Session;

public interface AbstractChatServerDao {
    UsersPair pollUsersPair();
    void saveUser(User user);
    void saveUser(String name, Session session, int role, String serverUri);
}

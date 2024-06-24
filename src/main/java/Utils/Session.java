package Utils;

import Models.User;

public class Session {
    private static Session instance;
    private static User userLoggedIn;

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public static void destroySession() {
        Session.setUserLoggedIn(null);
    }

    public static User getUserLoggedIn() {
        return userLoggedIn;
    }

    public static void setUserLoggedIn(User userLoggedIn) {
        Session.userLoggedIn = userLoggedIn;
    }
}

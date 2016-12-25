package in.pathri.codenvydownload.dao;

/**
 * Created by keerthi on 24-12-2016.
 */
public class LoginData {
    private String username;
    private String password;

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return "Username=" + username + "::" + "password" + password;
    }
}

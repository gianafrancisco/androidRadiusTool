package fransis.org.ar.radiustool.model;

/**
 * Created by francisco on 9/26/16.
 */
public class TestCase {
    private Long id;
    private String name;
    private String address;
    private int authPort;
    private String secret;
    private String userName;
    private String userPassword;

    public TestCase(Long id, String name, String address, int authPort, String secret, String userName, String userPassword) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.authPort = authPort;
        this.secret = secret;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAuthPort() {
        return authPort;
    }

    public void setAuthPort(int authPort) {
        this.authPort = authPort;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

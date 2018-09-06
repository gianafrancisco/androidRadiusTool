package fransis.org.ar.radiustool.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by francisco on 9/26/16.
 */
@Entity
public class TestCase {
    @PrimaryKey
    private Long id;
    private String name;
    private String address;
    @ColumnInfo(name = "auth_port")
    private int authPort;
    private String secret;
    @ColumnInfo(name = "user_name")
    private String userName;
    @ColumnInfo(name = "user_password")
    private String userPassword;

    public TestCase(String name, String address, int authPort, String secret, String userName, String userPassword) {
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

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return name;
    }
}

package fransis.org.ar.radiustool;

/**
 * Created by francisco on 9/14/16.
 */
public class RadiusServiceMock implements RadiusService {
    @Override
    public String auth(String host, int port, String secret, String userName, String userPassword) {
        return "Access-Accept";
    }
}

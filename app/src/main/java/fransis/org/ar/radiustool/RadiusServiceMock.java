package fransis.org.ar.radiustool;

/**
 * Created by francisco on 9/14/16.
 */
public class RadiusServiceMock implements RadiusService {
    @Override
    public String auth(String host, int port, String userName, String userPassword, String secret) {
        return "Access-Accept";
    }
}

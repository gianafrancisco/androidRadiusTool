package fransis.org.ar.radiustool;

/**
 * Created by francisco on 9/14/16.
 */
public interface RadiusService {
    String auth(final String host, final int port, String secret, final String userName, final String userPassword);
}

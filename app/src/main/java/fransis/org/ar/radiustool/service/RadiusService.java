package fransis.org.ar.radiustool.service;

/**
 * Created by francisco on 9/14/16.
 */
public interface RadiusService {
    int REQUEST_RETRIES = 5;
    String ACCESS_ACCEPT = "Access-Accept";
    String ACCESS_REJECT = "Access-Reject";
    String auth(final String host, final int port, String secret, final String userName, final String userPassword);
}

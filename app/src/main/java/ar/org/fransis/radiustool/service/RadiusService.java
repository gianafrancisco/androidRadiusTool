package ar.org.fransis.radiustool.service;

import java.util.HashMap;

/**
 * Created by francisco on 9/14/16.
 */
public interface RadiusService {
    int REQUEST_RETRIES     = 5;
    int REPLY_MESSAGE       = 18;
    int RADIUS_EXCEPTION    = -1;
    int RESPONSE_TYPE       = 257;
    String ACCESS_ACCEPT    = "Access-Accept";
    String ACCESS_REJECT    = "Access-Reject";
    HashMap<Integer, String> auth(final String host, final int port, String secret, final String userName, final String userPassword);
}

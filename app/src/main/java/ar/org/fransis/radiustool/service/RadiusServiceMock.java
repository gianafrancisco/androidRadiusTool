package ar.org.fransis.radiustool.service;

import java.util.HashMap;
import net.sourceforge.jradiusclient.RadiusPacket;
import net.sourceforge.jradiusclient.exception.InvalidParameterException;
import net.sourceforge.jradiusclient.exception.RadiusException;

import ar.org.fransis.radiustool.model.RadiusResponse;

/**
 * Created by francisco on 9/14/16.
 */
public class RadiusServiceMock implements RadiusService {
    @Override
    public HashMap<Integer, String> auth(String host, int port, String secret, String userName, String userPassword) {
        HashMap<Integer, String> ret = new HashMap<>();
        ret.put(RESPONSE_TYPE, "Access-Accept");
        ret.put(REPLY_MESSAGE, "Reply-Message not found.");
        return ret;
    }
}

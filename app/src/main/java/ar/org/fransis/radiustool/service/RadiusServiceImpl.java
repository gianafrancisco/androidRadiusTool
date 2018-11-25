package ar.org.fransis.radiustool.service;

import net.sourceforge.jradiusclient.RadiusAttribute;
import net.sourceforge.jradiusclient.RadiusAttributeValues;
import net.sourceforge.jradiusclient.RadiusClient;
import net.sourceforge.jradiusclient.RadiusPacket;
import net.sourceforge.jradiusclient.exception.InvalidParameterException;
import net.sourceforge.jradiusclient.exception.RadiusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.org.fransis.radiustool.model.RadiusResponse;

/**
 * Created by francisco on 15/09/2016.
 */
public class RadiusServiceImpl implements RadiusService {

    private RadiusClient radiusClient;
    private RadiusPacket radiusPacket;
    private RadiusPacket radiusPacketResponse;

    @Override
    public HashMap<Integer, String> auth(String host, int port, String secret, String userName, String userPassword) {
        StringBuilder reply_message = new StringBuilder();
        HashMap<Integer,String> response = new HashMap<>();
        try {
            radiusClient = new RadiusClient(host, port, 1813, secret);
            List<RadiusAttribute> attributeList = new ArrayList<RadiusAttribute>();
            attributeList.add(new RadiusAttribute(RadiusAttributeValues.USER_NAME, userName.getBytes()));
            attributeList.add(new RadiusAttribute(RadiusAttributeValues.USER_PASSWORD, userPassword.getBytes()));

            byte AuthOnly[] = new byte[4];
            AuthOnly[0] = (byte)RadiusAttributeValues.AUTHENTICATE_ONLY;
            AuthOnly[1] = (byte)0x00;
            AuthOnly[2] = (byte)0x00;
            AuthOnly[3] = (byte)0x00;
            attributeList.add(new RadiusAttribute(RadiusAttributeValues.SERVICE_TYPE, AuthOnly));
            radiusPacket = new RadiusPacket(RadiusPacket.ACCESS_REQUEST, attributeList);
            radiusPacketResponse = radiusClient.authenticate(radiusPacket, REQUEST_RETRIES);

            if(radiusPacketResponse.getPacketType() == (byte)RadiusPacket.ACCESS_ACCEPT){
                response.put(257, ACCESS_ACCEPT);
            }else{
                response.put(257, ACCESS_REJECT);
            }

            try {
                RadiusAttribute rad_reply_message = radiusPacketResponse.getAttribute(18);
                reply_message.append(new String(rad_reply_message.getValue()));
            }catch(RadiusException e) {
                reply_message.append("Reply-Message not found.");
            }

            response.put(REPLY_MESSAGE, reply_message.toString());

        } catch (RadiusException e) {
            response.put(RADIUS_EXCEPTION, e.getMessage());
        } catch (InvalidParameterException e) {
            response.put(RADIUS_EXCEPTION, e.getMessage());
        }
        // RadiusResponse r = new RadiusResponse(radiusPacketResponse);
        return response;
    }
}

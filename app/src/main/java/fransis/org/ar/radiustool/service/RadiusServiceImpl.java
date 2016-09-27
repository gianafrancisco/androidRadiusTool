package fransis.org.ar.radiustool.service;

import net.sourceforge.jradiusclient.RadiusAttribute;
import net.sourceforge.jradiusclient.RadiusAttributeValues;
import net.sourceforge.jradiusclient.RadiusClient;
import net.sourceforge.jradiusclient.RadiusPacket;
import net.sourceforge.jradiusclient.exception.InvalidParameterException;
import net.sourceforge.jradiusclient.exception.RadiusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 15/09/2016.
 */
public class RadiusServiceImpl implements RadiusService {

    public static final int REQUEST_RETRIES = 5;
    private RadiusClient radiusClient;
    private RadiusPacket radiusPacket;
    private RadiusPacket radiusPacketResponse;

    @Override
    public String auth(String host, int port, String secret, String userName, String userPassword) {
        StringBuilder response = new StringBuilder();
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
                response.append("Access-Accept");
            }else{
                response.append("Access-Reject");
            }

        } catch (RadiusException e) {
            response.append(e.getMessage());
        } catch (InvalidParameterException e) {
            response.append(e.getMessage());
        }
        return response.toString();
    }
}

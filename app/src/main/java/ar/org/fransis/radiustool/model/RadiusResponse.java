package ar.org.fransis.radiustool.model;

import com.google.common.collect.Lists;

import net.sourceforge.jradiusclient.RadiusAttribute;
import net.sourceforge.jradiusclient.RadiusPacket;
import net.sourceforge.jradiusclient.exception.InvalidParameterException;
import net.sourceforge.jradiusclient.exception.RadiusException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ar.org.fransis.radiustool.service.RadiusService;

public class RadiusResponse {
    private RadiusPacket response;

    public RadiusResponse(RadiusPacket response) {
        this.response = response;
    }

    public String getPacketType() {
        if(response.getPacketType() == (byte)RadiusPacket.ACCESS_ACCEPT){
            return RadiusService.ACCESS_ACCEPT;
        }else{
            return RadiusService.ACCESS_REJECT;
        }
    }

    public List getAttributes() throws InvalidParameterException, RadiusException {
        List list = new LinkedList();
        Collection<RadiusAttribute> c = response.getAttributes();
        for (RadiusAttribute a: c) {
            list.add(a);
        }
        return list;
    }
}

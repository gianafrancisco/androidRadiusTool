package ar.org.fransis.radiustool;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

import ar.org.fransis.radiustool.model.RadiusResponse;
import ar.org.fransis.radiustool.service.RadiusService;
import ar.org.fransis.radiustool.service.RadiusServiceMock;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RadiusServiceUnitTest {
    private RadiusService radiusService = new RadiusServiceMock();

    @Test
    public void auth_test() throws Exception {

        HashMap<Integer, String> response = radiusService.auth("localhost", 1812, "pass1", "secret", "user1");

        Assert.assertThat(response.get(RadiusService.RESPONSE_TYPE), is("Access-Accept"));
    }
}
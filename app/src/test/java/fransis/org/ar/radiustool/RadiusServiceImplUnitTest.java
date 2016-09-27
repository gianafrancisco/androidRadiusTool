package fransis.org.ar.radiustool;

import org.junit.Assert;
import org.junit.Test;

import fransis.org.ar.radiustool.service.RadiusService;
import fransis.org.ar.radiustool.service.RadiusServiceImpl;

import static org.hamcrest.core.Is.is;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RadiusServiceImplUnitTest {
    private RadiusService radiusService = new RadiusServiceImpl();

    @Test
    public void auth_test() throws Exception {

        String response = radiusService.auth("ec2-54-70-110-136.us-west-2.compute.amazonaws.com", 1812, "920bd7827374bf5d04ae783907a09c4e", "DSNW00000000", "DSNW00000000");

        Assert.assertThat(response, is("Access-Accept"));
    }
}
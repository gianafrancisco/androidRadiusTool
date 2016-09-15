package fransis.org.ar.radiustool;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RadiusServiceUnitTest {
    private RadiusService radiusService = new RadiusServiceMock();

    @Test
    public void auth_test() throws Exception {

        String response = radiusService.auth("localhost", 1812,"secret", "user1", "pass1");

        Assert.assertThat(response, is("Access-Accept"));
    }
}
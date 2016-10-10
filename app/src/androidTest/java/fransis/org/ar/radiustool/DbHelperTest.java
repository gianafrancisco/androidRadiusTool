package fransis.org.ar.radiustool;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fransis.org.ar.radiustool.model.TestCase;
import fransis.org.ar.radiustool.store.TestCaseDbHelper;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 10/8/16.
 */
@RunWith(AndroidJUnit4.class)
public class DbHelperTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private TestCaseDbHelper testCaseDbHelper = null;
    private MainActivity mActivity;

    public DbHelperTest() {
        super(MainActivity.class);

    }



    @Override
    @Before
    public void setUp() throws Exception {

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        Context applicationContext = mActivity.getApplicationContext();
        applicationContext.deleteDatabase("TestCase.db");
        testCaseDbHelper = new TestCaseDbHelper(applicationContext);
        super.setUp();
    }

    @Test
    public void testInsert() throws Exception {

        String name = "prueba";
        String address = "127.0.0.1";
        int authPort = 1812;
        String secret = "secret";
        String user = "user";
        String pass = "pass";
        TestCase testCase = testCaseDbHelper.insert(name, address, authPort, secret, user, pass);
        Assert.assertThat(testCase.getName(), is(name));
        Assert.assertThat(testCase.getAddress(), is(address));
        Assert.assertThat(testCase.getAuthPort(), is(authPort));
        Assert.assertThat(testCase.getSecret(), is(secret));
        Assert.assertThat(testCase.getUserName(), is(user));
        Assert.assertThat(testCase.getUserPassword(), is(pass));
        List<TestCase> all = testCaseDbHelper.getAll();
        Assert.assertThat(all.size(), is(1));

    }

    @Test
    public void testGetAll() throws Exception {

        String name = "prueba";
        String address = "127.0.0.1";
        int authPort = 1812;
        String secret = "secret";
        String user = "user";
        String pass = "pass";
        testCaseDbHelper.insert(name, address, authPort, secret, user, pass);
        Assert.assertThat(testCaseDbHelper.getAll().size(), is(1));
        TestCase testCase = testCaseDbHelper.getAll().get(0);

        Assert.assertThat(testCase.getName(), is(name));
        Assert.assertThat(testCase.getAddress(), is(address));
        Assert.assertThat(testCase.getAuthPort(), is(authPort));
        Assert.assertThat(testCase.getSecret(), is(secret));
        Assert.assertThat(testCase.getUserName(), is(user));
        Assert.assertThat(testCase.getUserPassword(), is(pass));

    }

    @Test
    public void testDelete() throws Exception {

        String name = "prueba";
        String address = "127.0.0.1";
        int authPort = 1812;
        String secret = "secret";
        String user = "user";
        String pass = "pass";
        TestCase testCase = testCaseDbHelper.insert(name, address, authPort, secret, user, pass);
        Assert.assertThat(testCaseDbHelper.getAll().size(), is(1));

        testCaseDbHelper.remove(testCase);
        Assert.assertThat(testCaseDbHelper.getAll().size(), is(0));

    }

    @Override
    @After
    public void tearDown() throws Exception {
        testCaseDbHelper.removeAll();
        super.tearDown();
    }
}

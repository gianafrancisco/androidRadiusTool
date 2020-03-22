package ar.org.fransis.radiustool;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ar.org.fransis.radiustool.model.TestCase;
import ar.org.fransis.radiustool.store.TestCaseDB;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 10/8/16.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTableTestCaseTest {

    private ar.org.fransis.radiustool.dao.TestCase mTestCaseDAO;
    private TestCaseDB mDatabase;

    @Before
    public void setUp() throws Exception {

        Context applicationContext = InstrumentationRegistry.getInstrumentation().getContext();

        mDatabase = Room.inMemoryDatabaseBuilder(applicationContext, TestCaseDB.class).build();
        mTestCaseDAO = mDatabase.testCaseDao();
    }

    @Test
    public void testInsert() throws Exception {

        String name = "prueba";
        String address = "127.0.0.1";
        int authPort = 1812;
        String secret = "secret";
        String user = "user";
        String pass = "pass";

        TestCase tc = new TestCase(name, address, authPort, secret, user, pass);
        List<TestCase> all = mTestCaseDAO.getAll();
        Assert.assertThat(all.size(), is(0));
        mTestCaseDAO.insert(tc);
        all = mTestCaseDAO.getAll();
        Assert.assertThat(all.size(), is(1));

    }

    @Test
    public void testUpdate() throws Exception {

        String name = "prueba";
        String address = "127.0.0.1";
        int authPort = 1812;
        String secret = "secret";
        String user = "user";
        String pass = "pass";

        TestCase testCase = new TestCase(name, address, authPort, secret, user, pass);
        List<TestCase> all = mTestCaseDAO.getAll();
        Assert.assertThat(all.size(), is(0));
        testCase.setId(mTestCaseDAO.insert(testCase));
        all = mTestCaseDAO.getAll();
        TestCase tc = all.get(0);
        Assert.assertThat(all.size(), is(1));
        Assert.assertThat(testCase.getName(), is(tc.getName()));
        Assert.assertThat(testCase.getAddress(), is(tc.getAddress()));
        Assert.assertThat(testCase.getAuthPort(), is(tc.getAuthPort()));
        Assert.assertThat(testCase.getSecret(), is(tc.getSecret()));
        Assert.assertThat(testCase.getUserName(), is(tc.getUserName()));
        Assert.assertThat(testCase.getUserPassword(), is(tc.getUserPassword()));

        testCase.setName("prueba-1");
        mTestCaseDAO.update(testCase);

        all = mTestCaseDAO.getAll();
        tc = all.get(0);
        Assert.assertThat(all.size(), is(1));
        Assert.assertThat(testCase.getName(), is(tc.getName()));
        Assert.assertThat(testCase.getAddress(), is(tc.getAddress()));
        Assert.assertThat(testCase.getAuthPort(), is(tc.getAuthPort()));
        Assert.assertThat(testCase.getSecret(), is(tc.getSecret()));
        Assert.assertThat(testCase.getUserName(), is(tc.getUserName()));
        Assert.assertThat(testCase.getUserPassword(), is(tc.getUserPassword()));

    }

    @Test
    public void testGetAll() throws Exception {

        TestCase tc = new TestCase("prueba", "127.0.0.1", 1812, "secret", "user", "pass");
        TestCase tc1 = new TestCase("prueba-1", "127.0.0.1", 1812, "secret", "user", "pass");
        mTestCaseDAO.insert(tc);
        mTestCaseDAO.insert(tc1);
        Assert.assertThat(mTestCaseDAO.getAll().size(), is(2));
        TestCase testCase = mTestCaseDAO.getAll().get(0);

        Assert.assertThat(testCase.getName(), is(tc.getName()));
        Assert.assertThat(testCase.getAddress(), is(tc.getAddress()));
        Assert.assertThat(testCase.getAuthPort(), is(tc.getAuthPort()));
        Assert.assertThat(testCase.getSecret(), is(tc.getSecret()));
        Assert.assertThat(testCase.getUserName(), is(tc.getUserName()));
        Assert.assertThat(testCase.getUserPassword(), is(tc.getUserPassword()));

    }

    @Test
    public void testDelete() throws Exception {
        TestCase tc = new TestCase("prueba", "127.0.0.1", 1812, "secret", "user", "pass");
        TestCase tc1 = new TestCase("prueba-1", "127.0.0.1", 1812, "secret", "user", "pass");
        tc.setId(mTestCaseDAO.insert(tc));
        mTestCaseDAO.insert(tc1);
        Assert.assertThat(mTestCaseDAO.getAll().size(), is(2));
        mTestCaseDAO.delete(tc);
        Assert.assertThat(mTestCaseDAO.getAll().size(), is(1));

        TestCase testCase = mTestCaseDAO.getAll().get(0);

        Assert.assertThat(testCase.getName(), is(tc1.getName()));
        Assert.assertThat(testCase.getAddress(), is(tc1.getAddress()));
        Assert.assertThat(testCase.getAuthPort(), is(tc1.getAuthPort()));
        Assert.assertThat(testCase.getSecret(), is(tc1.getSecret()));
        Assert.assertThat(testCase.getUserName(), is(tc1.getUserName()));
        Assert.assertThat(testCase.getUserPassword(), is(tc1.getUserPassword()));

    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }
}

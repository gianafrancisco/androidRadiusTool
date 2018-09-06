package fransis.org.ar.radiustool;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fransis.org.ar.radiustool.model.TestCase;
import fransis.org.ar.radiustool.store.TestCaseDB;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 10/8/16.
 */
@RunWith(AndroidJUnit4.class)
public class DbTest {

    private fransis.org.ar.radiustool.dao.TestCase dao;
    private TestCaseDB db;

    @Before
    public void setUp() throws Exception {

        Context applicationContext = InstrumentationRegistry.getTargetContext();

        db = Room.inMemoryDatabaseBuilder(applicationContext, TestCaseDB.class).build();
        dao = db.testCaseDao();
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
        List<TestCase> all = dao.getAll();
        Assert.assertThat(all.size(), is(0));
        dao.insert(tc);
        all = dao.getAll();
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
        List<TestCase> all = dao.getAll();
        Assert.assertThat(all.size(), is(0));
        testCase.setId(dao.insert(testCase));
        all = dao.getAll();
        TestCase tc = all.get(0);
        Assert.assertThat(all.size(), is(1));
        Assert.assertThat(testCase.getName(), is(tc.getName()));
        Assert.assertThat(testCase.getAddress(), is(tc.getAddress()));
        Assert.assertThat(testCase.getAuthPort(), is(tc.getAuthPort()));
        Assert.assertThat(testCase.getSecret(), is(tc.getSecret()));
        Assert.assertThat(testCase.getUserName(), is(tc.getUserName()));
        Assert.assertThat(testCase.getUserPassword(), is(tc.getUserPassword()));

        testCase.setName("prueba-1");
        dao.update(testCase);

        all = dao.getAll();
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
        dao.insert(tc);
        dao.insert(tc1);
        Assert.assertThat(dao.getAll().size(), is(2));
        TestCase testCase = dao.getAll().get(0);

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
        tc.setId(dao.insert(tc));
        dao.insert(tc1);
        Assert.assertThat(dao.getAll().size(), is(2));
        dao.delete(tc);
        Assert.assertThat(dao.getAll().size(), is(1));

        TestCase testCase = dao.getAll().get(0);

        Assert.assertThat(testCase.getName(), is(tc1.getName()));
        Assert.assertThat(testCase.getAddress(), is(tc1.getAddress()));
        Assert.assertThat(testCase.getAuthPort(), is(tc1.getAuthPort()));
        Assert.assertThat(testCase.getSecret(), is(tc1.getSecret()));
        Assert.assertThat(testCase.getUserName(), is(tc1.getUserName()));
        Assert.assertThat(testCase.getUserPassword(), is(tc1.getUserPassword()));

    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }
}

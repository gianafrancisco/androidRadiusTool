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

import ar.org.fransis.radiustool.model.Result;
import ar.org.fransis.radiustool.model.TestCase;
import ar.org.fransis.radiustool.store.TestCaseDB;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 10/8/16.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTableResultTest {

    private ar.org.fransis.radiustool.dao.Result mResultDAO;
    private TestCaseDB mDatabase;

    @Before
    public void setUp() throws Exception {

        Context applicationContext = InstrumentationRegistry.getInstrumentation().getContext();

        mDatabase = Room.inMemoryDatabaseBuilder(applicationContext, TestCaseDB.class).build();
        mResultDAO = mDatabase.resultDao();
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
        Result result = new Result(tc,"test-msg", "ACCEPT", 100L);
        Assert.assertThat(mResultDAO.getAll().size(), is(0));
        mResultDAO.insert(result);
        List<Result> list = mResultDAO.getAll();
        Assert.assertThat(list.size(), is(1));
        Assert.assertThat(list.get(0).getReplyMessage(), is(result.getReplyMessage()));
        Assert.assertThat(list.get(0).getResponseTime(), is(result.getResponseTime()));
        Assert.assertThat(list.get(0).getResponseType(), is(result.getResponseType()));
        Assert.assertThat(list.get(0).getTestName(), is(result.getTestName()));


    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }
}

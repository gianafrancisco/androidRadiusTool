package fransis.org.ar.radiustool.store;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import fransis.org.ar.radiustool.model.TestCase;

@Database(entities = {TestCase.class}, version = 1)
public abstract class TestCaseDB extends RoomDatabase {
    public abstract fransis.org.ar.radiustool.dao.TestCase testCaseDao();
}

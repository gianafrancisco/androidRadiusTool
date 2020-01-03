package ar.org.fransis.radiustool.store;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.org.fransis.radiustool.model.TestCase;

@Database(entities = {TestCase.class}, version = 1)
public abstract class TestCaseDB extends RoomDatabase {
    public abstract ar.org.fransis.radiustool.dao.TestCase testCaseDao();
}
